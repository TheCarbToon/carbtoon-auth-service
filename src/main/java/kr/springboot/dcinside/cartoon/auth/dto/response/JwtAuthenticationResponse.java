package kr.springboot.dcinside.cartoon.auth.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class JwtAuthenticationResponse {

    @NonNull
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(@NonNull String accessToken) {
        this.accessToken = accessToken;
    }

    //    @Builder
//    public JwtAuthenticationResponse(@NonNull String accessToken, String tokenType) {
//        this.accessToken = accessToken;
//        this.tokenType = tokenType;
//    }

}
