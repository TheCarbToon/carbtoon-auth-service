package kr.springboot.dcinside.cartoon.auth.payload;

import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.messaging.AuthServiceLogType;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class AuthServiceLogPayload {

    private String _class;
    private String uri;
    private String job;
    private String userId;
    private String userName;
    private String userEmail;
    private Object data;
    private AuthServiceLogType logType;

    @Builder
    public AuthServiceLogPayload(AuthServiceLogType logType, String _class, String uri, String job, String userId, String userName, String userEmail, Object data) {
        this.logType = logType;
        this._class = _class;
        this.uri = uri;
        this.job = job;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.data = data;
    }

    public static AuthServiceLogPayload convertTo(
            User user,
            String uri,
            String job,
            AuthServiceLogType authServiceLogType,
            String _class,
            Object data) {

        if (user == null) {
            user = User.builder()
                    .id("none")
                    .username("none")
                    .email("none")
                    .build();
        }

        return AuthServiceLogPayload.builder()
                .logType(authServiceLogType)
                ._class(_class)
                .uri(uri)
                .job(job)
                .userId(user.getId())
                .userName(user.getUsername())
                .userEmail(user.getEmail())
                .data(data)
                .build();

    }

}
