package kr.springboot.dcinside.cartoon.auth.service;

import kr.springboot.dcinside.cartoon.auth.domain.CartoonUserDetails;
import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignInRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignUpRequest;
import kr.springboot.dcinside.cartoon.auth.dto.response.ApiResponse;
import kr.springboot.dcinside.cartoon.auth.dto.response.JwtAuthenticationResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Optional;

public interface UserService {

    void registerUser(User user);

    ApiResponse signUpUser(SignUpRequest signUpRequest, CartoonUserDetails userDetails);

    JwtAuthenticationResponse authenticateUser(SignInRequest signInRequest);

}
