package shop.mtcoding.jobara.love;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.love.dto.LoveReq.LoveSaveReqDto;
import shop.mtcoding.jobara.user.vo.UserVo;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class LoveControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private MockHttpSession employeeMockSession;
    private MockHttpSession companyMockSession;

    @BeforeEach
    public void setUp() {
        UserVo employeePrincipal = new UserVo();
        UserVo companyPrincipal = new UserVo();
        employeePrincipal.setId(6);
        employeePrincipal.setUsername("cos");
        employeePrincipal.setRole("company");
        employeePrincipal.setProfile(null);

        companyPrincipal.setId(1);
        companyPrincipal.setUsername("ssar");
        companyPrincipal.setRole("employee");
        companyPrincipal.setProfile(null);

        employeeMockSession = new MockHttpSession();
        companyMockSession = new MockHttpSession();
        employeeMockSession.setAttribute("EmployeePrincipal", employeePrincipal);
        companyMockSession.setAttribute("CompanyPrincipal", companyPrincipal);
    }

    @Test
    public void loveSave_test() throws Exception {
        // given
        LoveSaveReqDto loveSaveReqDto = new LoveSaveReqDto();
        // loveSaveReqDto.setBoardId(1);
        loveSaveReqDto.setBoardId(2);
        String requestBody = om.writeValueAsString(loveSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/loves")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(employeeMockSession));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.msg").value("좋아요성공"));
        // resultActions.andExpect(jsonPath("$.msg").value("좋아요 내역이 존재합니다."));
    }

    @Test
    public void loveCancel_test() throws Exception {
        // given
        Integer loveId = 1;
        // Integer loveId = 3;

        // when
        ResultActions resultActions = mvc.perform(delete("/loves/" + loveId)
                .session(employeeMockSession));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.msg").value("좋아요취소 성공"));
        // resultActions.andExpect(jsonPath("$.msg").value("좋아요 내역이 존재하지 않습니다"));
    }
}
