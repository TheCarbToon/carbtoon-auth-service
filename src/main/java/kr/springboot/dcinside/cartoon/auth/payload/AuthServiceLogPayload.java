package kr.springboot.dcinside.cartoon.auth.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.springboot.dcinside.cartoon.auth.domain.User;
import lombok.*;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthServiceLogPayload {

    @JsonProperty("log_uuid")
    private String logUUID;

    @JsonProperty("log_type")
    private Object logType;

    private String ip;

    private String uri;

    private String method;

    private Map<String, String> headers;

    private String query;

    @JsonProperty("request_user")
    private LogUserInfo requestUser;

    @JsonProperty("content_body")
    private Object contentBody;

    @Builder
    public AuthServiceLogPayload(String logUUID, Map<String, String> headers, String uri, String query, String method, String ip, User requestUser, Object contentBody, Object logType) {
        this.logUUID = logUUID;
        this.uri = uri;
        this.ip = ip;
        this.method = method;
        this.query = query;
        this.headers = headers;
        this.requestUser = new LogUserInfo(requestUser);
        this.contentBody = contentBody;
        this.logType = logType;
    }

    @NoArgsConstructor
    @Getter
    @ToString
    private static class LogUserInfo {

        private String id;
        private String email;
        private String username;

        public LogUserInfo(User user) {

            if (user == null) {
                user = User.builder()
                        .id("none")
                        .username("none")
                        .email("none")
                        .build();
            }

            this.id = user.getId();
            this.email = user.getEmail();
            this.username = user.getUsername();

        }

    }


}
