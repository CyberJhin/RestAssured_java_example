package org.example.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class SuccessSignInSignUpResponse {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private NestedUserInfo user;
}
