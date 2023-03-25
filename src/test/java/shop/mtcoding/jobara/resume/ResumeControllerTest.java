package shop.mtcoding.jobara.resume;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.resume.dto.ResumeReq.ResumeSaveReq;
import shop.mtcoding.jobara.resume.dto.ResumeReq.ResumeUpdateReq;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class ResumeControllerTest {

      @Autowired
      private MockMvc mvc;

      @Autowired
      ObjectMapper om;

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
      public void resumeList_test() throws Exception {
            // given

            // when

            ResultActions resultActions = mvc.perform(
                        get("/employee/resume/list").contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "Bearer " + employeeJwtToken));

            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseBody);
            // then
            resultActions.andExpect(status().is2xxSuccessful());

      }

      @Test
      public void resumeSave_test() throws Exception {
            // given
            ResumeSaveReq resume = new ResumeSaveReq();
            resume.setTitle("제목1");
            resume.setContent("내용1");
            String requestBody = om.writeValueAsString(resume);
            // when
            ResultActions resultActions = mvc.perform(
                        post("/employee/resume/save").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "Bearer " + employeeJwtToken));
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            System.out.println("resp:" + responseBody);
            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }

      @Test
      public void resumeUpdate_test() throws Exception {
            // given
            int id = 1;
            ResumeUpdateReq resume = new ResumeUpdateReq();
            resume.setId(id);
            resume.setTitle("제목1 수정");
            resume.setContent("내용1 수정");
            String requestBody = om.writeValueAsString(resume);
            // when
            ResultActions resultActions = mvc.perform(
                        put("/employee/resume/" + id).content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "Bearer " + employeeJwtToken));
            // then
            resultActions.andExpect(status().is2xxSuccessful());
      }


      @Test
      public void resumeDelete_test() throws Exception {
            // given
            int id = 1;
            int id2 = 2; // 본인것이 아닌 이력서
            // when
            ResultActions resultActions = mvc.perform(delete("/employee/resume/" + id).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "Bearer " + employeeJwtToken));
            ResultActions resultActions2 = mvc.perform(delete("/employee/resume/" + id2).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", "Bearer " + employeeJwtToken));

            // then
            resultActions.andExpect(status().is2xxSuccessful());
            resultActions2.andExpect(status().is4xxClientError()); // 본인 것이 아닌 이력서 삭제 요청시
      }

}
