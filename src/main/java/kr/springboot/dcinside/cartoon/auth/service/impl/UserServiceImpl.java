package kr.springboot.dcinside.cartoon.auth.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.springboot.dcinside.cartoon.auth.domain.EmailAuth;
import kr.springboot.dcinside.cartoon.auth.domain.Profile;
import kr.springboot.dcinside.cartoon.auth.domain.Role;
import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.AuthUserCreateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserDisplayNameUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserPasswordUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserProfilePictureUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignInRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignUpRequest;
import kr.springboot.dcinside.cartoon.auth.dto.response.ApiResponse;
import kr.springboot.dcinside.cartoon.auth.dto.response.JwtAuthenticationResponse;
import kr.springboot.dcinside.cartoon.auth.exception.BadRequestException;
import kr.springboot.dcinside.cartoon.auth.exception.EmailAlreadyExistsException;
import kr.springboot.dcinside.cartoon.auth.exception.ResourceNotFoundException;
import kr.springboot.dcinside.cartoon.auth.exception.UsernameAlreadyExistsException;
import kr.springboot.dcinside.cartoon.auth.feign.client.UserServiceClient;
import kr.springboot.dcinside.cartoon.auth.repo.EmailAuthRepository;
import kr.springboot.dcinside.cartoon.auth.repo.UserRepository;
import kr.springboot.dcinside.cartoon.auth.service.EmailService;
import kr.springboot.dcinside.cartoon.auth.service.JwtTokenProvider;
import kr.springboot.dcinside.cartoon.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final String lbServiceName = "AUTH-SERVICE";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final EmailAuthRepository emailAuthRepository;

    @Override
    public JwtAuthenticationResponse authenticateUser(SignInRequest signInRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return JwtAuthenticationResponse.builder()
                .accessToken(tokenProvider.generateToken(authentication))
                .refreshToken(tokenProvider.generateRefreshToken())
                .build();

    }

    @Override
    public ApiResponse signUpUser(SignUpRequest signUpRequest) {
        log.info("???????????? ?????? ID -> ({})", signUpRequest.getUsername());

        String authUuid = UUID.randomUUID().toString();

        User user = User
                .builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .userProfile(Profile
                        .builder()
                        .displayName(signUpRequest.getName())
                        .build())
                .build();

        try {
            registerUser(user);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        emailAuthSave(user, authUuid);

        return ApiResponse.builder()
                .success(true)
                .message("???????????? ??????!")
                .build();
    }

    @Override
    public void emailAuthSave(User user, String uuid) {

        emailAuthRepository.save(EmailAuth.builder()
                .username(user.getUsername())
                .auth(false)
                .uuid(uuid)
                .build());

        emailService.sendAuthMail(user.getEmail(), uuid);

        log.info("?????? {} , ?????? ?????? ?????? ?????? => {}", user.getEmail(), uuid);

    }

    @Override
    public ApiResponse emailAuth(String uuid) {

        emailAuthRepository.findByUuid(uuid)
                .map(emailAuth -> {
                    emailAuth.setAuth(true);
                    emailAuthRepository.save(emailAuth);
                    userRepository.findByUsername(emailAuth.getUsername())
                            .map(user -> {
                                user.setActive(true);
                                userRepository.save(user);
                                return user;
                            });
                    return emailAuth;
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("%s??? ????????? ???????????? ????????? ??????.", uuid))
                );

        return ApiResponse.builder()
                .success(true)
                .message("????????? ?????? ??????!")
                .build();
    }

    @Override
    public void registerUser(User user) {
        log.info(" {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("????????? {}??? ?????? ????????????.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("????????? %s??? ?????? ????????????.", user.getUsername()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("????????? {}??? ?????? ????????????.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("????????? %s??? ?????? ????????????.", user.getEmail()));
        }

        user = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .active(false) // User service is true
                .userProfile(user.getUserProfile())
                .roles(new HashSet<>() {{
                    add(Role.USER);
                }})
                .build();

        userRepository.save(user);
        String jsonAuthUser = "";
        try {
            jsonAuthUser = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            log.error("???????????? json to string failed!! id is => {}", user.getUsername());
        }

        String test = userServiceClient.createUser(AuthUserCreateFeignRequest.builder()
                .jsonAuthUser(jsonAuthUser)
                .lbServiceName(lbServiceName)
                .build());
        if (test.equals("bad")) {
            log.error("USER-SERVICE CREATE USER FAILURE!!! id is => {}",
                    user.getUsername());
            throw new RuntimeException("????????????");
        }

    }


    @Override
    public boolean updateUserProfilePictureUri(UserProfilePictureUpdateFeignRequest userProfilePictureUpdateFeignRequest) {

        log.info("?????? ????????? ?????? ???????????? user id is => {}", userProfilePictureUpdateFeignRequest.getId());

        if (!userProfilePictureUpdateFeignRequest.getLbServiceName().equals("USER-SERVICE")) return false;

        userRepository.findById(userProfilePictureUpdateFeignRequest.getId())
                .map(user -> {
                    user.getUserProfile().setProfilePictureUrl(userProfilePictureUpdateFeignRequest.getProfilePictureUri());
                    userRepository.save(user);
                    return user;
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("%s??? ???????????? ????????? ??????.", userProfilePictureUpdateFeignRequest.getId()))
                );

        return true;
    }

    @Override
    public boolean updateUserPassword(UserPasswordUpdateFeignRequest userPasswordUpdateFeignRequest) {
        log.info("?????? ???????????? ???????????? user id is => {}", userPasswordUpdateFeignRequest.getId());

        if (!userPasswordUpdateFeignRequest.getLbServiceName().equals("USER-SERVICE")) return false;

        userRepository.findById(userPasswordUpdateFeignRequest.getId())
                .map(user -> {
                    user.setPassword(userPasswordUpdateFeignRequest.getPassword());
                    userRepository.save(user);
                    return user;
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("%s??? ???????????? ????????? ??????.", userPasswordUpdateFeignRequest.getId()))
                );

        return true;
    }

    @Override
    public boolean updateUserDisplayName(UserDisplayNameUpdateFeignRequest userDisplayNameUpdateFeignRequest) {

        log.info("?????? ????????? ???????????? user id is => {}", userDisplayNameUpdateFeignRequest.getId());

        if (!userDisplayNameUpdateFeignRequest.getLbServiceName().equals("USER-SERVICE")) return false;

        userRepository.findById(userDisplayNameUpdateFeignRequest.getId())
                .map(user -> {
                    user.getUserProfile().setDisplayName(userDisplayNameUpdateFeignRequest.getDisplayName());
                    userRepository.save(user);
                    return user;
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("%s??? ???????????? ????????? ??????.", userDisplayNameUpdateFeignRequest.getId()))
                );

        return true;
    }
}
