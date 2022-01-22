package kr.springboot.dcinside.cartoon.auth.dto.feign.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPasswordUpdateFeignRequest {

    private String password;

    private String id;

    private String lbServiceName;

    public UserPasswordUpdateFeignRequest() {
    }

    @Builder
    public UserPasswordUpdateFeignRequest(String password, String id, String lbServiceName) {
        this.password = password;
        this.id = id;
        this.lbServiceName = lbServiceName;
    }

}
