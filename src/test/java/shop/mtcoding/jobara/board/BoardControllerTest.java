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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.board.dto.BoardInsertReqBuilder;
import shop.mtcoding.jobara.board.dto.BoardUpdateReqBuilder;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertReqDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.jobara.user.vo.UserVo;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {
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
    public void myBoardDelete() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions resultActions = mvc.perform(delete("/boards/" + boardId)
                .session(employeeMockSession));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        // System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.msg").value("게시글을 삭제하였습니다"));
    }

    @Test
    public void myScrapBoardList_test() throws Exception {
        // given
        Integer userId = 6;

        // when
        ResultActions resultActions = mvc.perform(get("/boards/scrapList/" + userId)
                .session(employeeMockSession));
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
        ResultActions resultActions = mvc.perform(get("/boards/boardList/" + companyId)
                .session(companyMockSession));
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
                "포스트맨 저장내용", "1년이상 ~ 3년미만",
                "4년 대졸이상", "인턴", "2023-04-09", "근면성실", 6, Arrays.asList(1, 2, 3));
        ;
        Integer boardId = 1;

        // when
        ResultActions resultActions = mvc.perform(put("/boards/" + boardId)
                .content(om.writeValueAsString(boardUpdateReqDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(companyMockSession));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void save_test() throws Exception {
        // given
        BoardInsertReqDto boardInsertReqDto = BoardInsertReqBuilder.makeBoardInsertReqForTest("포스트맨 저장제목", "포스트맨 저장내용",
                "1년이상 ~ 3년미만",
                "4년 대졸이상", "인턴", "2023-04-09", "근면성실", 6, Arrays.asList(1, 2, 3));
        ;
        // System.out.println("테스트 : " + om.writeValueAsString(boardInsertReqDto));

        // when
        ResultActions resultActions = mvc.perform(post("/boards")
                .content(om.writeValueAsString(boardInsertReqDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(companyMockSession));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void updateForm_test() throws Exception {
        // given
        Integer boardId = 4;

        // when
        ResultActions resultActions = mvc.perform(get("/boards/updateForm/" + boardId)
                .session(companyMockSession));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        // System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.title").value("JAVA백엔드주니어개발자"));
    }

    @Test
    public void list_test() throws Exception {
        // given
        Integer page = 0;
        String keyword = "lang";
        // when
        ResultActions resultActions = mvc.perform(get("/boards/list?page=" + page)
                .session(employeeMockSession));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        // System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.board[0].user.userId").value(15));
    }

    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/")
                .session(employeeMockSession));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void detail_test() throws Exception {
        // given
        Integer boardId = 1;
        // when
        ResultActions resultActions = mvc.perform(get("/boards/" + boardId)
                .session(employeeMockSession));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        // System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.resume[0].id").value(1));
    }

}
