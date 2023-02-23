package shop.mtcoding.jobara.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.jobara.dto.company.CompanyReq.CompanyLoginReqDto;
import shop.mtcoding.jobara.ex.CustomException;
import shop.mtcoding.jobara.model.Company;
import shop.mtcoding.jobara.service.CompanyService;
import shop.mtcoding.jobara.util.Verify;

@Controller
public class CompanyController {

      @Autowired
      private CompanyService companyService;

      @Autowired
      private HttpSession session;

      @GetMapping("/company/joinForm")
      public String joinForm() {
            return "company/joinForm";
      }

      @GetMapping("/company/loginForm")
      public String loginForm() {
            return "company/loginForm";
      }

      @GetMapping("/company/updateForm")
      public String updateForm() {
            return "company/updateForm";
      }

      @PostMapping("/company/login")
      public String login(CompanyLoginReqDto companyLoginReqDto){
            Verify.validateStiring(companyLoginReqDto.getUsername(), "유저네임을 입력하세요.");
            Verify.validateStiring(companyLoginReqDto.getPassword(), "암호을 입력하세요.");
            Company coPrincipal = companyService.getCompany(companyLoginReqDto);
            session.setAttribute("coPrincipal", coPrincipal);
            return "redirect:/";
      }

}
