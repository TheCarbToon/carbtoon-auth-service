package kr.springboot.dcinside.cartoon.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
public class EmailAuth {

    @Id
    private String id;

    private String username;

    private String uuid;

    private boolean auth;

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    @Builder
    public EmailAuth(String id, String username, String uuid, boolean auth) {
        this.id = id;
        this.username = username;
        this.uuid = uuid;
        this.auth = auth;
    }

}
