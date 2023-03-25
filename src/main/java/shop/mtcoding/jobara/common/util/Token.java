package shop.mtcoding.jobara.common.util;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.interfaces.DecodedJWT;

import shop.mtcoding.jobara.common.config.auth.JwtProvider;
import shop.mtcoding.jobara.common.config.auth.LoginUser;

public class Token {

    public static LoginUser loginCheck(LoginUser principal, ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String prefixJwt = req.getHeader(JwtProvider.HEADER);
        System.out.println("테스트 : " + prefixJwt);

        if (!(prefixJwt == null)) {
            String jwt = prefixJwt.replace(JwtProvider.TOKEN_PREFIX, "");

            DecodedJWT decodedJWT = JwtProvider.verify(jwt);
            int principalId = decodedJWT.getClaim("id").asInt();
            String role = decodedJWT.getClaim("role").asString();

            principal = LoginUser.builder().id(principalId).role(role).build();
        }

        return principal;
    }
}
