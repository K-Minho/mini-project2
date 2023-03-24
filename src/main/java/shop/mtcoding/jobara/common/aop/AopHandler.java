package shop.mtcoding.jobara.common.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.common.ex.CustomException;

@Aspect
@Component
public class AopHandler {

    @Autowired
    HttpServletRequest request;

    @Pointcut("@annotation(shop.mtcoding.jobara.common.aop.CompanyCheck)")
    public void CompanyCheck() {
    }

    @Before("CompanyCheck()")
    public void CompanyCheck(JoinPoint joinPoint) {
        // JWT 토큰을 가져오는 로직
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException("인증 토큰이 존재하지 않습니다.");
        }
        String jwt = token.substring(7);
        // JWT 토큰 파싱 및 검증 로직
        try {
            // JWT 토큰 파싱
            Claims claims = Jwts.parserBuilder().setSigningKey("메타코딩").build().parseClaimsJws(jwt).getBody();

            // JWT 토큰에서 role 정보 추출
            String role = (String) claims.get("role");

            // role 정보가 company 경우에만 허용
            if (!"company".equals(role)) {
                throw new CustomException("기업회원이 아닙니다.");
            }
        } catch (Exception e) {
            throw new CustomException("기업회원이 아닙니다.");
        }
    }

    @Pointcut("@annotation(shop.mtcoding.jobara.common.aop.CompanyCheckApi)")
    public void CompanyCheckApi() {
    }

    @Before("CompanyCheckApi()")
    public void CompanyCheckApi(JoinPoint joinPoint) {
        // JWT 토큰을 가져오는 로직
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException("인증 토큰이 존재하지 않습니다.");
        }
        String jwt = token.substring(7);
        // JWT 토큰 파싱 및 검증 로직
        try {
            // JWT 토큰 파싱
            Claims claims = Jwts.parserBuilder().setSigningKey("메타코딩").build().parseClaimsJws(jwt).getBody();
            // JWT 토큰에서 role 정보 추출
            String role = (String) claims.get("role");

            // role 정보가 company 경우에만 허용
            if (!"company".equals(role)) {
                throw new CustomApiException("기업회원이 아닙니다.");
            }
        } catch (Exception e) {
            throw new CustomApiException("기업회원이 아닙니다.");
        }
    }

    @Pointcut("@annotation(shop.mtcoding.jobara.common.aop.EmployeeCheck)")
    public void EmployeeCheck() {
    }

    @Before("EmployeeCheck()")
    public void EmployeeCheck(JoinPoint joinPoint) {
        // JWT 토큰을 가져오는 로직
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException("인증 토큰이 존재하지 않습니다.");
        }
        String jwt = token.substring(7);
        // JWT 토큰 파싱 및 검증 로직
        try {
            // JWT 토큰 파싱
            Claims claims = Jwts.parserBuilder().setSigningKey("메타코딩").build().parseClaimsJws(jwt).getBody();
            // JWT 토큰에서 role 정보 추출
            String role = (String) claims.get("role");

            // role 정보가 company 경우에만 허용
            if (!"employee".equals(role)) {
                throw new CustomException("일반회원이 아닙니다.");
            }
        } catch (Exception e) {
            throw new CustomException("일반회원이 아닙니다.");
        }
    }

    @Pointcut("@annotation(shop.mtcoding.jobara.common.aop.EmployeeCheckApi)")
    public void EmployeeCheckApi() {
    }

    @Before("EmployeeCheckApi()")
    public void EmployeeCheckApi(JoinPoint joinPoint) {
        // JWT 토큰을 가져오는 로직
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException("인증 토큰이 존재하지 않습니다.");
        }
        String jwt = token.substring(7);
        // JWT 토큰 파싱 및 검증 로직
        try {
            // JWT 토큰 파싱
            Claims claims = Jwts.parserBuilder().setSigningKey("메타코딩").build().parseClaimsJws(jwt).getBody();

            // JWT 토큰에서 role 정보 추출
            String role = (String) claims.get("role");

            // role 정보가 company 경우에만 허용
            if (!"employee".equals(role)) {
                throw new CustomApiException("일반회원이 아닙니다.");
            }
        } catch (Exception e) {
            throw new CustomApiException("일반회원이 아닙니다.");
        }
    }
}
