package kr.springboot.dcinside.cartoon.auth.controller;

import kr.springboot.dcinside.cartoon.auth.domain.Profile;
import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignInRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignUpRequest;
import kr.springboot.dcinside.cartoon.auth.dto.response.ApiResponse;
import kr.springboot.dcinside.cartoon.auth.dto.response.JwtAuthenticationResponse;
import kr.springboot.dcinside.cartoon.auth.exception.BadRequestException;
import kr.springboot.dcinside.cartoon.auth.exception.EmailAlreadyExistsException;
import kr.springboot.dcinside.cartoon.auth.exception.UsernameAlreadyExistsException;
import kr.springboot.dcinside.cartoon.auth.service.JwtTokenProvider;
import kr.springboot.dcinside.cartoon.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;


    /**
     * 로그인 & 토큰 발급
     * @param signInRequest
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

    }

    /**
     * 회원가입
     * @param signUpRequest
     * @return
     */
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("creating user {}", signUpRequest.getUsername());

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
            userService.registerUser(user);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity
                .created(location) // restful api location.... must
                .body(new ApiResponse(true,"굿굿굿!"));
    }

    /**
     * TODO 리프레시 토큰으로 새로운 발급 만들어야함
     * @param refreshToken
     * @return
     */
    @GetMapping("/newtoken/{refreshToken}")
    public ResponseEntity<?> newToken(@PathVariable String refreshToken) {
        return ResponseEntity.ok(refreshToken);
    }

    /**
     * 테스트용
     * @return
     */
    @GetMapping(value = "/secure")
    public String getSecure() {
        return "^o^";
    }

}
