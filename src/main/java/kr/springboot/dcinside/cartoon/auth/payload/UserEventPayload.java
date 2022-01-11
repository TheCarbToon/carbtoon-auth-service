package kr.springboot.dcinside.cartoon.auth.payload;

import kr.springboot.dcinside.cartoon.auth.messaging.UserEventType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserEventPayload {

    private String id;
    private String username;
    private String password;
    private String email;
    private String displayName;
    private String profilePictureUrl;
    private String oldProfilePicUrl;
    private UserEventType eventType;

    @Builder
    public UserEventPayload(String id, String username, String password, String email, String displayName, String profilePictureUrl, String oldProfilePicUrl, UserEventType eventType) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.oldProfilePicUrl = oldProfilePicUrl;
        this.eventType = eventType;
    }

}
