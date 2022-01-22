package kr.springboot.dcinside.cartoon.auth.service;

import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserDisplayNameUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserPasswordUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.feign.request.UserProfilePictureUpdateFeignRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignInRequest;
import kr.springboot.dcinside.cartoon.auth.dto.request.SignUpRequest;
import kr.springboot.dcinside.cartoon.auth.dto.response.ApiResponse;
import kr.springboot.dcinside.cartoon.auth.dto.response.JwtAuthenticationResponse;

public interface UserService {

    void registerUser(User user);

    ApiResponse signUpUser(SignUpRequest signUpRequest);

    JwtAuthenticationResponse authenticateUser(SignInRequest signInRequest);

    boolean updateUserProfilePictureUri(UserProfilePictureUpdateFeignRequest userProfilePictureUpdateFeignRequest);

    boolean updateUserPassword(UserPasswordUpdateFeignRequest userPasswordUpdateFeignRequest);

    boolean updateUserDisplayName(UserDisplayNameUpdateFeignRequest userDisplayNameUpdateFeignRequest);

}
