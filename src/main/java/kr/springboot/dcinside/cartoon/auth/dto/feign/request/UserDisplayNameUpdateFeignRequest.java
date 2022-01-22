package kr.springboot.dcinside.cartoon.auth.dto.feign.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserDisplayNameUpdateFeignRequest {

    @JsonProperty("displayName")
    private String displayName;

    private String id;

    @JsonProperty("lbServiceName")
    private String lbServiceName;

    public UserDisplayNameUpdateFeignRequest() {
        // jackson library가 빈 생성자가 없는 모델을 생성하는 방법을 모릅니다.
    }

    @Builder
    public UserDisplayNameUpdateFeignRequest(String displayName, String id, String lbServiceName) {
        this.displayName = displayName;
        this.id = id;
        this.lbServiceName = lbServiceName;
    }

}
