package yoon.capstone.application.security;

import lombok.Getter;
import yoon.capstone.application.enums.Role;

@Getter
public class JwtAuthentication {

    private long memberIdx;

    private String email;

    private String refreshToken;

    private Role role;

    public JwtAuthentication(long idx, String email, String token, Role role){
        this.memberIdx = idx;
        this.email = email;
        this.refreshToken = token;
        this.role = role;
    }

}
