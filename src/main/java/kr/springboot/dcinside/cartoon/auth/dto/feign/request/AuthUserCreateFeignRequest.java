package kr.springboot.dcinside.cartoon.auth.dto.feign.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserCreateFeignRequest {

    String jsonAuthUser;

    String lbServiceName;

    @Builder
    public AuthUserCreateFeignRequest(String jsonAuthUser, String lbServiceName) {
        this.jsonAuthUser = jsonAuthUser;
        this.lbServiceName = lbServiceName;
    }

}
