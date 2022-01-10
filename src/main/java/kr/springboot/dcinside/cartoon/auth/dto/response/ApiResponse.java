package kr.springboot.dcinside.cartoon.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse {

    private Boolean success;
    private String message;

    @Builder
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
