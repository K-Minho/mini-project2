package shop.mtcoding.jobara.board;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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

import shop.mtcoding.jobara.board.dto.BoardInsertReqBuilder;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertReqDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.jobara.board.dto.BoardUpdateReqBuilder;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {
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
        public void myBoardDelete() throws Exception {
                // given
                Integer boardId = 1;

                // when
                ResultActions resultActions = mvc.perform(delete("/company/boards/" + boardId)
                                .header("Authorization", "Bearer " + companyJwtToken));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.msg").value("게시글을 삭제하였습니다"));
        }

        @Test
        public void myScrapBoardList_test() throws Exception {
                // given
                Integer userId = 1;

                // when
                ResultActions resultActions = mvc.perform(get("/employee/boards/myScrapList/" + userId)
                                .header("Authorization", "Bearer " + employeeJwtToken));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data[0].company.companyName").value("스윙비(Swingvy)"));
        }

        @Test
        public void myBoardList_test() throws Exception {
                // given
                Integer companyId = 6;

                // when
                ResultActions resultActions = mvc.perform(get("/company/boards/myList/" + companyId)
                                .header("Authorization", "Bearer " + companyJwtToken));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data[0].title").value("인공지능 (AI Solution) 개발"));
        }

        @Test
        public void update_test() throws Exception {
                // given
                BoardUpdateReqDto boardUpdateReqDto = BoardUpdateReqBuilder.makeBoardUpdateReqForTest(1, "포스트맨 저장제목",
                                "포스트맨 저장내용", "1년이상 ~ 3년미만", "4년 대졸이상", "인턴", "2023-04-09", "근면성실", 6,
                                Arrays.asList(1, 2, 3));
                Integer boardId = 1;

                // when
                ResultActions resultActions = mvc.perform(put("/company/boards/" + boardId)
                                .content(om.writeValueAsString(boardUpdateReqDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Authorization", "Bearer " + companyJwtToken));

                // then
                // resultActions.andExpect(jsonPath("$.errors[0].field").value("title"));
                resultActions.andExpect(status().isCreated());
        }

        @Test
        public void save_test() throws Exception {
                // given
                BoardInsertReqDto boardInsertReqDto = BoardInsertReqBuilder.makeBoardInsertReqForTest("포스트맨 저장제목",
                                "포스트맨 저장내용",
                                "1년이상 ~ 3년미만",
                                "4년 대졸이상", "인턴", "2023-04-09", "근면성실", 6, Arrays.asList(1, 2, 3));
                ;
                // System.out.println("테스트 : " + om.writeValueAsString(boardInsertReqDto));

                // when
                ResultActions resultActions = mvc.perform(post("/company/boards")
                                .content(om.writeValueAsString(boardInsertReqDto))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Authorization", "Bearer " + companyJwtToken));

                // then
                resultActions.andExpect(status().isCreated());
        }

        @Test
        public void updateForm_test() throws Exception {
                // given
                Integer boardId = 1;

                // when
                ResultActions resultActions = mvc.perform(get("/company/boards/updateForm/" + boardId)
                                .header("Authorization", "Bearer " + companyJwtToken));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data.title").value("인공지능 솔루션 (AI Solution) 개발"));
        }

        @Test
        public void list_test() throws Exception {
                // given
                Integer page = 0;
                String keyword = "lang";
                // when
                ResultActions resultActions = mvc.perform(get("/boards?page=" + page)
                                .header("Authorization", "Bearer " + employeeJwtToken));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data.board[0].user.id").value(15));
        }

        @Test
        public void main_test() throws Exception {
                // given

                // when
                ResultActions resultActions = mvc.perform(get("/")
                                .header("Authorization", "Bearer " + companyJwtToken));

                // then
                resultActions.andExpect(status().isOk());
        }

        @Test
        public void detail_test() throws Exception {
                // given
                Integer boardId = 1;
                // when
                ResultActions resultActions = mvc.perform(get("/boards/" + boardId)
                                .header("Authorization", "Bearer " + companyJwtToken));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                // resultActions.andExpect(jsonPath("$.data.resume[0].id").value(1));
        }

}
