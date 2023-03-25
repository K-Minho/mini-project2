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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.love.dto.LoveReq.LoveSaveReqDto;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class LoveControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    String employeeJwtToken;
    String companyJwtToken;

    @BeforeEach
    public void setUp() throws Exception {

            // employee test용
            MockHttpServletRequestBuilder employeeLoginRequest = post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"ssar\", \"password\":\"1234\"}");
            MvcResult employeeLoginResult = mvc.perform(employeeLoginRequest).andReturn();

            // 로그인 응답에서 토큰 추출하기
            employeeJwtToken = employeeLoginResult.getResponse().getHeader("Authorization");

            // company test용
            MockHttpServletRequestBuilder companyLoginRequest = post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"cos\", \"password\":\"1234\"}");
            MvcResult companyLoginResult = mvc.perform(companyLoginRequest).andReturn();

            // 로그인 응답에서 토큰 추출하기
            companyJwtToken = companyLoginResult.getResponse().getHeader("Authorization");
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
                .header("Authorization", "Bearer " + employeeJwtToken));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.msg").value("좋아요 성공"));
        // resultActions.andExpect(jsonPath("$.msg").value("좋아요 내역이 존재합니다."));
    }

    @Test
    public void loveCancel_test() throws Exception {
        // given
        Integer loveId = 1;
        // Integer loveId = 3;

        // when
        ResultActions resultActions = mvc.perform(delete("/loves/" + loveId)
        .header("Authorization", "Bearer " + employeeJwtToken));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.msg").value("좋아요취소 성공"));
        // resultActions.andExpect(jsonPath("$.msg").value("좋아요 내역이 존재하지 않습니다"));
    }
}
