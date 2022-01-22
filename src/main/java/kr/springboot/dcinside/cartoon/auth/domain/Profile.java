package kr.springboot.dcinside.cartoon.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Profile {

    private String displayName;
    private String profilePictureUrl;

    @Builder
    public Profile(String displayName, String profilePictureUrl) {
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
    }

}

