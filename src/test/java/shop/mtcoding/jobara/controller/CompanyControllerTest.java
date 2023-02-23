package shop.mtcoding.jobara.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.jobara.model.Company;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class CompanyControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @Test
    public void login_test() throws Exception {
        // given
        String usernameVal = "cos";
        String passwordVal = "1234";
        StringBuffer sb = new StringBuffer();
        sb.append("username=");
        sb.append(usernameVal);
        sb.append("&password=");
        sb.append(passwordVal);
        String requestBody = sb.toString();

        // when
        ResultActions resultActions = mvc.perform(post("/company/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        HttpSession session = resultActions.andReturn().getRequest().getSession();
        Company coPrincipal = (Company) session.getAttribute("coPrincipal");

        // verify
        resultActions.andExpect(status().is3xxRedirection());
        assertThat(coPrincipal.getUsername()).isEqualTo("cos");
    }
}
