package kr.springboot.dcinside.cartoon.auth.dto.feign.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserCreateFeignResponse {

    String jsonAuthUser;

    String lbServiceName;

    @Builder
    public AuthUserCreateFeignResponse(String jsonAuthUser, String lbServiceName) {
        this.jsonAuthUser = jsonAuthUser;
        this.lbServiceName = lbServiceName;
    }

}
