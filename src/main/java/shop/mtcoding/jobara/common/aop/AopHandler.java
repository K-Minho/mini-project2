package shop.mtcoding.jobara.common.aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shop.mtcoding.jobara.common.config.auth.LoginUser;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.common.ex.CustomException;

@Aspect
@Component
public class AopHandler {

    @Autowired
    HttpSession session;

    @Pointcut("@annotation(shop.mtcoding.jobara.common.aop.CompanyCheck)")
    public void CompanyCheck() {
    }

    @Before("CompanyCheck()")
    public void CompanyCheck(JoinPoint joinPoint) {
        LoginUser token = (LoginUser) session.getAttribute("loginUser");
        if (token == null) {
            throw new CustomApiException("인증 토큰이 존재하지 않습니다.");
        }
        if (!token.getRole().equals("company")) {
            throw new CustomApiException("기업회원이 아닙니다.");
        }
    }

    @Pointcut("@annotation(shop.mtcoding.jobara.common.aop.CompanyCheckApi)")
    public void CompanyCheckApi() {
    }

    @Before("CompanyCheckApi()")
    public void CompanyCheckApi(JoinPoint joinPoint) {
        LoginUser token = (LoginUser) session.getAttribute("loginUser");
        if (token == null) {
            throw new CustomApiException("인증 토큰이 존재하지 않습니다.");
        }
        if (!token.getRole().equals("company")) {
            throw new CustomApiException("기업회원이 아닙니다.");
        }
    }

    @Pointcut("@annotation(shop.mtcoding.jobara.common.aop.EmployeeCheck)")
    public void EmployeeCheck() {
    }

    @Before("EmployeeCheck()")
    public void EmployeeCheck(JoinPoint joinPoint) {
        LoginUser token = (LoginUser) session.getAttribute("loginUser");
        if (token == null) {
            throw new CustomException("인증 토큰이 존재하지 않습니다.");
        }
        if (!token.getRole().equals("employee")) {
            throw new CustomException("구직자회원이 아닙니다.");
        }
    }

    @Pointcut("@annotation(shop.mtcoding.jobara.common.aop.EmployeeCheckApi)")
    public void EmployeeCheckApi() {
    }

    @Before("EmployeeCheckApi()")
    public void EmployeeCheckApi(JoinPoint joinPoint) {
        LoginUser token = (LoginUser) session.getAttribute("loginUser");
        if (token == null) {
            throw new CustomApiException("인증 토큰이 존재하지 않습니다.");
        }
        if (!token.getRole().equals("employee")) {
            throw new CustomApiException("구직자회원이 아닙니다.");
        }
    }
}
