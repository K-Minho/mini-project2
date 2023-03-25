package shop.mtcoding.jobara.employee;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.employee.dto.EmployeeJoinBuilder;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeJoinReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeTechUpdateReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeUpdateReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeUpdateBuilder;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class EmployeeControllerTest {

      @Autowired
      private MockMvc mvc;

      @Autowired
      private ObjectMapper om;

      String employeeJwtToken;

      @BeforeEach
      public void setUp() throws Exception {
              MockHttpServletRequestBuilder employeeLoginRequest = post("/login")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content("{\"username\":\"ssar\", \"password\":\"1234\"}");
              MvcResult employeeLoginResult = mvc.perform(employeeLoginRequest).andReturn();

              employeeJwtToken = employeeLoginResult.getResponse().getHeader("Authorization");
      }

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

      // 보류
      @Test
      public void update_test() throws Exception {

            // given
            int id = 1;
            EmployeeUpdateReqDto employeeUpdateReqDto = EmployeeUpdateBuilder.makeUpdateReqDto("1234", "asdf@asdf.asd",
                        "부산광역시", "부산대로", "01011112222", "김실명", "4년제", 1);
            String requestBody = om.writeValueAsString(employeeUpdateReqDto);

            // when
            System.out.println("req:" + requestBody);
            ResultActions resultActions = mvc.perform(put("/employee/" + id).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "Bearer " + employeeJwtToken));
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseBody);

            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }

      @Test
      public void employeeList_test() throws Exception {

            // given
            int page = 1;
            String requestBody = om.writeValueAsString(page);

            // when
            System.out.println("req:" + requestBody);
            ResultActions resultActions = mvc.perform(get("/list").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseBody);

            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }

      @Test
      public void employeeDetail_test() throws Exception {

            // given
            int id = 1;
            int resumeId = 1;

            // when
            ResultActions resultActions = mvc
                        .perform(get("/user/" + id + "/resume/" + resumeId)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE));
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseBody);

            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }

      @Test
      public void updateForm_test() throws Exception {

            // given
            int id = 1;
            
            // when
            ResultActions resultActions = mvc
                        .perform(get("/employee/"+id)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "Bearer " + employeeJwtToken));
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseBody);

            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }

      @Test
      public void detail_test() throws Exception {

            // given
            int id = 1;
            
            // when
            ResultActions resultActions = mvc
                        .perform(get("/user/"+id)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE));
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            
            // then
            System.out.println("resp :" + responseBody);
            resultActions.andExpect(status().is2xxSuccessful());
      }

      @Test
      public void skillUpdate_test() throws Exception {

            // given
            int id = 1;
            EmployeeTechUpdateReqDto employeeTechUpdateReqDto = new EmployeeTechUpdateReqDto();
            ArrayList<Integer> skill = new ArrayList<>();
            skill.add(1);
            skill.add(3);
            skill.add(7);
            employeeTechUpdateReqDto.setCheckedValues(skill);
            String requestBody = om.writeValueAsString(employeeTechUpdateReqDto);
            // when
            ResultActions resultActions = mvc
                        .perform(put("/employee/update/tech/"+id).content(requestBody)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "Bearer " + employeeJwtToken));

            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }
}
