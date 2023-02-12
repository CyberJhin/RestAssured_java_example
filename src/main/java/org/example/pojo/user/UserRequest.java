package org.example.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserRequest {
    private String email;
    private String name;
    private String password;
}
