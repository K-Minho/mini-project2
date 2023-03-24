package shop.mtcoding.jobara.employee;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.employee.dto.EmployeeJoinBuilder;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeJoinReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeUpdateReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeUpdateBuilder;
import shop.mtcoding.jobara.user.vo.UserVo;

@RequiredArgsConstructor
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class EmployeeControllerTest {

      private final MockMvc mvc;

      private final ObjectMapper om;

      private MockHttpSession mockSession;

      @BeforeEach
      public void setUp() {
            UserVo principal = new UserVo();
            principal.setId(1);
            principal.setUsername("ssar");
            principal.setRole("employee");
            principal.setProfile(null);
            mockSession = new MockHttpSession();
            mockSession.setAttribute("loginUser", principal);
      }

      // 현재 보류
      @Test
      public void join_test() throws Exception {
            // given
            EmployeeJoinReqDto employeeJoinReqDto = EmployeeJoinBuilder.makejoinReqDto("asdf", "1234", "asdf@asdf.asd");
            String requestBody = om.writeValueAsString(employeeJoinReqDto);
            // when
            System.out.println("req:" + requestBody);
            ResultActions resultActions = mvc.perform(
                        post("/joinEmployee").content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE));
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseBody);

            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }

      @Test
      public void update_test() throws Exception {
            // given
            int id = 1;
            EmployeeUpdateReqDto employeeUpdateReqDto = EmployeeUpdateBuilder.makeUpdateReqDto("1234", "asdf@asdf.asd",
                        "부산광역시", "null", "null", "null", "null", 1);
            String requestBody = om.writeValueAsString(employeeUpdateReqDto);

            // when
            ResultActions resultActions = mvc.perform(post("/employee/update/" +
                        id).content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).session(mockSession));

            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }
}
