package kr.springboot.dcinside.cartoon.auth.feign.client;

import kr.springboot.dcinside.cartoon.auth.dto.feign.request.AuthUserCreateFeignRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping(value = "/users/create", produces = MediaType.APPLICATION_JSON_VALUE)
    String createUser(@RequestBody AuthUserCreateFeignRequest userCreate);

}
