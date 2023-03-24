package shop.mtcoding.jobara.employee;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.common.util.RedisService;
import shop.mtcoding.jobara.employee.dto.EmployeeJoinBuilder;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeJoinReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeUpdateReqDto;
import shop.mtcoding.jobara.user.vo.UserVo;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class EmployeeControllerTest {

      @Autowired
      private MockMvc mvc;

      @Autowired
      private ObjectMapper om;

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
            // int id = 1;
            // String requestBody = om.writeValueAsString();

            // MockMultipartFile file = new MockMultipartFile(
            // "profile", // 파라미터 이름은 프론트엔드에서 정해진 대로 "profile"로 설정합니다.
            // "filename.txt", // 파일 이름은 테스트를 위해 아무 값이나 설정합니다.
            // "image/jpeg", // 파일 타입은 이미지 파일인 jpeg로 설정합니다.
            // "Test data".getBytes() // 파일 내용은 테스트를 위해 아무 값이나 설정합니다.
            // );

            // when
            // ResultActions resultActions = mvc.perform(post("/employee/update/" +
            // id).content(requestBody)
            // .contentType(MediaType.APPLICATION_JSON_VALUE).session(mockSession));

            // then
            // resultActions.andExpect(status().is3xxRedirection());
      }
}
