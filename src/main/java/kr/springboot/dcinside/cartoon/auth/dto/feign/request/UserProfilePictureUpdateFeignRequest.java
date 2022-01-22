package kr.springboot.dcinside.cartoon.auth.dto.feign.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfilePictureUpdateFeignRequest {

    private String profilePictureUri;

    private String id;

    private String lbServiceName;

    public UserProfilePictureUpdateFeignRequest() {}

    @Builder
    public UserProfilePictureUpdateFeignRequest(String profilePictureUri, String id, String lbServiceName) {
        this.profilePictureUri = profilePictureUri;
        this.id = id;
        this.lbServiceName = lbServiceName;
    }

}
