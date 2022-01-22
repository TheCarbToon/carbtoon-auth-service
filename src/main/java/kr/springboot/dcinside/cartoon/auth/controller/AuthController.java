package kr.springboot.dcinside.cartoon.auth.controller;

import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserDisplayNameUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserPasswordUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserProfilePictureUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignInRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignUpRequest;
import kr.springboot.dcinside.cartoon.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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


    /**
     * 로그인 & 토큰 발급
     *
     * @param signInRequest
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(
                userService.authenticateUser(signInRequest));
    }

    /**
     * 회원가입
     *
     * @param signUpRequest
     * @return
     */
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(
            @Valid @RequestBody SignUpRequest signUpRequest) {

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(signUpRequest.getUsername()).toUri();

        return ResponseEntity
                .created(location) // restful api location.... must
                .body(userService.signUpUser(signUpRequest));

    }

    /**
     * TODO 리프레시 토큰으로 새로운 발급 만들어야함
     *
     * @param refreshToken
     * @return
     */
    @GetMapping("/newtoken/{refreshToken}")
    public ResponseEntity<?> newToken(
            @PathVariable String refreshToken) {
        return ResponseEntity.ok(refreshToken);
    }

    /**
     * 테스트용
     *
     * @return
     */
    @GetMapping(value = "/secure")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getSecure() {
        return ResponseEntity.ok("secure pass!");
    }

    @PutMapping(value = "/users/displayname", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateUserDisplayName(
            @RequestBody UserDisplayNameUpdateFeignRequest userDisplayNameUpdateFeignRequest) {
        return userService.updateUserDisplayName(userDisplayNameUpdateFeignRequest);
    }

    @PutMapping(value = "/users/password", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateUserDisplayName(
            @RequestBody UserPasswordUpdateFeignRequest userPasswordUpdateFeignRequest) {
        return userService.updateUserPassword(userPasswordUpdateFeignRequest);
    }

    @PutMapping(value = "/users/profile-picture", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateUserDisplayName(
            @RequestBody UserProfilePictureUpdateFeignRequest userProfilePictureUpdateFeignRequest) {
        return userService.updateUserProfilePictureUri(userProfilePictureUpdateFeignRequest);
    }

}
