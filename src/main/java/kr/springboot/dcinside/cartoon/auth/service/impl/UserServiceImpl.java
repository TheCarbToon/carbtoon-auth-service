package kr.springboot.dcinside.cartoon.auth.service.impl;

import kr.springboot.dcinside.cartoon.auth.config.KafkaConfig;
import kr.springboot.dcinside.cartoon.auth.domain.CartoonUserDetails;
import kr.springboot.dcinside.cartoon.auth.domain.Profile;
import kr.springboot.dcinside.cartoon.auth.domain.Role;
import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignInRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignUpRequest;
import kr.springboot.dcinside.cartoon.auth.dto.response.ApiResponse;
import kr.springboot.dcinside.cartoon.auth.dto.response.JwtAuthenticationResponse;
import kr.springboot.dcinside.cartoon.auth.exception.BadRequestException;
import kr.springboot.dcinside.cartoon.auth.exception.EmailAlreadyExistsException;
import kr.springboot.dcinside.cartoon.auth.exception.UsernameAlreadyExistsException;
import kr.springboot.dcinside.cartoon.auth.messaging.AuthServiceLogType;
import kr.springboot.dcinside.cartoon.auth.messaging.KafkaLogProducer;
import kr.springboot.dcinside.cartoon.auth.messaging.UserEventType;
import kr.springboot.dcinside.cartoon.auth.payload.AuthServiceLogPayload;
import kr.springboot.dcinside.cartoon.auth.payload.UserEventPayload;
import kr.springboot.dcinside.cartoon.auth.repo.UserRepository;
import kr.springboot.dcinside.cartoon.auth.service.JwtTokenProvider;
import kr.springboot.dcinside.cartoon.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
//    private final UserEventSender userEventSender;
    private final KafkaLogProducer kafkaLogProducer;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

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
    public ApiResponse signUpUser(SignUpRequest signUpRequest,
                                  CartoonUserDetails userDetails) {
        log.info("회원가입 요청 ID -> ({})", signUpRequest.getUsername());
        kafkaLogProducer.send(KafkaConfig.AUTH_TOPIC, AuthServiceLogPayload.convertTo(userDetails,
                "/register",
                "auth",
                AuthServiceLogType.REQUEST,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                signUpRequest));

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

        return ApiResponse.builder()
                .success(true)
                .message("회원가입 성공!")
                .build();
    }

    @Override
    public void registerUser(User user) {
        log.info(" {}", user.getUsername());


        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("아이디 {}가 이미 있습니다.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("아이디 %s가 이미 있습니다.", user.getUsername()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("이메일 {}가 이미 있습니다.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("이메일 %s가 이미 있습니다.", user.getEmail()));
        }

        user = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .active(true)
                .userProfile(user.getUserProfile())
                .roles(new HashSet<>() {{
                    add(Role.USER);
                }})
                .build();

        User savedUser = userRepository.save(user);
//        userEventSender.sendUserCreated(savedUser);
//        kafkaLogProducer.send(KafkaConfig.USER_EVENT_TOPIC, savedUser);
        kafkaLogProducer.send(KafkaConfig.AUTH_TOPIC, UserEventPayload.convertTo(savedUser, UserEventType.CREATED));

    }

}
