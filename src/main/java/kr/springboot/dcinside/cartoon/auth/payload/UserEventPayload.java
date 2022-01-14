package kr.springboot.dcinside.cartoon.auth.payload;

import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.messaging.UserEventType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserEventPayload {

    private String userId;
    private String userName;
    private String password;
    private String userEmail;
    private String displayName;
    private String profilePictureUrl;
    private String oldProfilePicUrl;
    private UserEventType eventType;

    @Builder
    public UserEventPayload(String userId, String userName, String password, String userEmail, String displayName, String profilePictureUrl, String oldProfilePicUrl, UserEventType eventType) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.userEmail = userEmail;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.oldProfilePicUrl = oldProfilePicUrl;
        this.eventType = eventType;
    }

    public static UserEventPayload convertTo(User user, UserEventType eventType) {

        return UserEventPayload
                .builder()
                .eventType(eventType)
                .userId(user.getId())
                .password(user.getPassword())
                .userName(user.getUsername())
                .userEmail(user.getEmail())
                .displayName(user.getUserProfile().getDisplayName())
                .profilePictureUrl(user.getUserProfile().getProfilePictureUrl())
                .build();

    }

}
