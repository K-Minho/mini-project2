package shop.mtcoding.jobara.common.config.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginUser {
    private Integer id;
    private String role;

    @Builder
    public LoginUser(Integer id, String role) {
        this.id = id;
        this.role = role;
    }
}
