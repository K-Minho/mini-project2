package shop.mtcoding.jobara.board;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.board.dto.BoardInsertReqBuilder;
import shop.mtcoding.jobara.board.dto.BoardMyListBuilder;
import shop.mtcoding.jobara.board.dto.BoardMyListRespDto;
import shop.mtcoding.jobara.board.dto.BoardMyScrapBuilder;
import shop.mtcoding.jobara.board.dto.BoardMyScrapListRespDto;
import shop.mtcoding.jobara.board.dto.BoardUpdateFormRespBuilder;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertReqDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.jobara.board.dto.BoardUpdateFormRespDto;
import shop.mtcoding.jobara.board.dto.BoardUpdateReqBuilder;
import shop.mtcoding.jobara.common.config.auth.LoginUser;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper om;

        @MockBean
        private BoardService boardService;
    
        private MockHttpSession employeeMockSession;
        private MockHttpSession companyMockSession;

        // String employeeJwtToken;
        // String companyJwtToken;

        // @BeforeEach
        // public void setUp() throws Exception {

        //         // employee test용
        //         MockHttpServletRequestBuilder employeeLoginRequest = post("/login")
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content("{\"username\":\"ssar\", \"password\":\"1234\"}");
        //         MvcResult employeeLoginResult = mvc.perform(employeeLoginRequest).andReturn();

        //         // 로그인 응답에서 토큰 추출하기
        //         employeeJwtToken = employeeLoginResult.getResponse().getHeader("Authorization");

        //         // company test용
        //         MockHttpServletRequestBuilder companyLoginRequest = post("/login")
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content("{\"username\":\"cos\", \"password\":\"1234\"}");
        //         MvcResult companyLoginResult = mvc.perform(companyLoginRequest).andReturn();

        //         // 로그인 응답에서 토큰 추출하기
        //         companyJwtToken = companyLoginResult.getResponse().getHeader("Authorization");
        // }

        @BeforeEach
        public void setUp() {
            LoginUser employeePrincipal = new LoginUser(1, "employee");
            employeeMockSession = new MockHttpSession();
            employeeMockSession.setAttribute("loginUser", employeePrincipal);
            
            LoginUser companyPrincipal = new LoginUser(6, "company");
            companyMockSession = new MockHttpSession();
            companyMockSession.setAttribute("loginUser", companyPrincipal);
        }

        @Test
        public void myBoardDelete() throws Exception {
                // given
                Integer boardId = 1;

                // when
                // doNothing().when(boardService).deleteMyBoard(boardId, principalId);

                ResultActions resultActions = mvc.perform(delete("/company/boards/" + boardId)
                                .session(companyMockSession));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.msg").value("공고 삭제 성공"));
        }

        @Test
        public void myScrapBoardList_test() throws Exception {
                // given
                Integer userId = 1;

                // mock
                List<BoardMyScrapListRespDto> myScrapBoardListPS = new ArrayList<>();
                BoardMyScrapListRespDto myScrapBoard1 = BoardMyScrapBuilder.makeMyScrapRespForTest(1, "mock제목1", "32일", 
                        BoardMyScrapBuilder.makeCompanyDtoForTest("모크회사1"), 
                        BoardMyScrapBuilder.makeUserDtoForTest(6, "/images/모크프로필1.png"),
                        BoardMyScrapBuilder.makeLoveDtoForTest(1, 1, 1, "fa-solid"));
                BoardMyScrapListRespDto myScrapBoard2 = BoardMyScrapBuilder.makeMyScrapRespForTest(2, "mock제목2", "21일", 
                BoardMyScrapBuilder.makeCompanyDtoForTest("모크회사2"), 
                BoardMyScrapBuilder.makeUserDtoForTest(6, "/images/모크프로필2.png"),
                BoardMyScrapBuilder.makeLoveDtoForTest(2, 2, 1, "fa-solid"));

                myScrapBoardListPS.add(myScrapBoard1);
                myScrapBoardListPS.add(myScrapBoard2);

                given(boardService.getMyScrapBoardList(1, 1)).willReturn(myScrapBoardListPS);


                // when
                ResultActions resultActions = mvc.perform(get("/employee/boards/myScrapList/" + userId)
                .session(employeeMockSession));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data[1].company.companyName").value("모크회사2"));
        }

        @Test
        public void myBoardList_test() throws Exception {
                // given
                Integer companyId = 6;

                // mock
                List<BoardMyListRespDto> myBoardListPS = new ArrayList<>();
                BoardMyListRespDto myBoard1 = BoardMyListBuilder.makeMyBoardRespForTest(1, "mock제목1", "32일", 
                       BoardMyListBuilder.makeCompanyDtoForTest("모크회사1"), 
                       BoardMyListBuilder.makeUserDtoForTest(6, "/images/모크프로필1.png"));
                BoardMyListRespDto myBoard2 = BoardMyListBuilder.makeMyBoardRespForTest(2, "mock제목2", "21일", 
                       BoardMyListBuilder.makeCompanyDtoForTest("모크회사2"), 
                       BoardMyListBuilder.makeUserDtoForTest(7, "/images/모크프로필2.png"));

                       myBoardListPS.add(myBoard1);
                       myBoardListPS.add(myBoard2);

                given(boardService.getMyBoardList(6, 6)).willReturn(myBoardListPS);

                // when
                ResultActions resultActions = mvc.perform(get("/company/boards/myList/" + companyId)
                .session(companyMockSession));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data[0].title").value("mock제목1"));
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
                                .session(companyMockSession));

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
                                .session(companyMockSession));

                // then
                resultActions.andExpect(status().isCreated());
        }

        @Test
        public void updateForm_test() throws Exception {
                // given
                Integer boardId = 1;

                // mock
                BoardUpdateFormRespDto boardUpdateFormRespDto = BoardUpdateFormRespBuilder.makeUpdateFormRespForTest(boardId, "모크제목", "모크내용", "모크career", "모크education", "모크jobType", "모크favor", "모크deadline", 1, new ArrayList<>(Arrays.asList(1, 2, 3)));


                given(boardService.getUpdateFormInfo(1, 6)).willReturn(boardUpdateFormRespDto);

                // when
                ResultActions resultActions = mvc.perform(get("/company/boards/updateForm/" + boardId)
                .session(companyMockSession));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                // System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data.title").value("모크제목"));
        }

        @Test
        public void list_test() throws Exception {
                // given
                Integer page = 0;
                String keyword = "lang";

                // mock

                // when
                ResultActions resultActions = mvc.perform(get("/boards?page=" + page)
                .session(employeeMockSession));

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
                .session(companyMockSession));

                // then
                resultActions.andExpect(status().isOk());
        }

        @Test
        public void detail_test() throws Exception {
                // given
                Integer boardId = 1;

                // mock


                // when
                ResultActions resultActions = mvc.perform(get("/boards/" + boardId)
                .session(companyMockSession));

                String responseBody = resultActions.andReturn().getResponse().getContentAsString();
                System.out.println("테스트 : " + responseBody);

                // then
                resultActions.andExpect(status().isOk());
                resultActions.andExpect(jsonPath("$.data.resume[0].id").value(1));
        }

}
