package shop.mtcoding.jobara.apply;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import shop.mtcoding.jobara.apply.dto.ApplyJoinBoardAndResumeBuilder;
import shop.mtcoding.jobara.apply.dto.ApplyJoinBoardAndUserBuilder;
import shop.mtcoding.jobara.apply.dto.ApplyReq.ApplyDecideReqDto;
import shop.mtcoding.jobara.apply.dto.ApplyReq.ApplyReqDto;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndResume;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndUser;
import shop.mtcoding.jobara.user.vo.UserVo;

@WebMvcTest(ApplyController.class)
public class ApplyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private ApplyService applyService;

    private MockHttpSession mockSession;

    @BeforeEach
    public void setUp() {
        UserVo principal = new UserVo();
        principal.setId(1);
        principal.setUsername("ssar");
        principal.setRole("employee");
        principal.setProfile(null);
        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", principal);
    }

    @Test
    public void apply_test() throws Exception {
        // given
        ApplyReqDto applyReqDto = new ApplyReqDto(3, 1);

        // when
        ResultActions resultActions = mvc.perform(post("/apply")
                .content(om.writeValueAsString(applyReqDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(mockSession));
        String resp = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + resp);

        // verify
        resultActions.andExpect(status().is2xxSuccessful());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.msg").value("지원 성공"));
    }

    @Test
    public void companyApplyList_test() throws Exception {
        // given
        Integer id = 6;

        // mock
        List<ApplyJoinBoardAndUser> applyListPS = new ArrayList<>();
        ApplyJoinBoardAndUser applyJoinBoardAndUser = ApplyJoinBoardAndUserBuilder.makeApplyJoinBoardAndUser(1, "0",
                ApplyJoinBoardAndUserBuilder.makeUser(1, "김일"),
                ApplyJoinBoardAndUserBuilder.makeBoard(1, "제목1"),
                ApplyJoinBoardAndUserBuilder.makeResume(1));
        applyListPS.add(applyJoinBoardAndUser);
        applyJoinBoardAndUser = ApplyJoinBoardAndUserBuilder.makeApplyJoinBoardAndUser(2, "0",
                ApplyJoinBoardAndUserBuilder.makeUser(2, "김이"),
                ApplyJoinBoardAndUserBuilder.makeBoard(2, "제목2"),
                ApplyJoinBoardAndUserBuilder.makeResume(2));
        applyListPS.add(applyJoinBoardAndUser);
        given(applyService.getApplyForCompany(id)).willReturn(applyListPS);

        // when
        ResultActions resultActions = mvc.perform(get("/company/" + id + "/apply"));
        String resp = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + resp);

        // then
        resultActions.andExpect(status().is2xxSuccessful());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.msg").value("지원자 리스트 불러오기 성공"));
        resultActions.andExpect(jsonPath("$.data[0].id").value(1));
        resultActions.andExpect(jsonPath("$.data[0].state").value("0"));
        resultActions.andExpect(jsonPath("$.data[0].board.id").value(1));
        resultActions.andExpect(jsonPath("$.data[0].board.title").value("제목1"));
        resultActions.andExpect(jsonPath("$.data[0].user.id").value(1));
        resultActions.andExpect(jsonPath("$.data[0].user.realName").value("김일"));
        resultActions.andExpect(jsonPath("$.data[0].resume.id").value(1));
    }

    @Test
    public void decideApplyment_test() throws Exception {
        // given
        Integer id = 1;
        ApplyDecideReqDto applyDecideReqDto = new ApplyDecideReqDto(2, -1);

        // when
        ResultActions resultActions = mvc.perform(put("/board/" + id + "/apply")
                .content(om.writeValueAsString(applyDecideReqDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(mockSession));
        String resp = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + resp);
        // then
        resultActions.andExpect(status().is2xxSuccessful());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.msg").value("불합격 처리 완료"));
    }
    
    @Test
    public void employeeApplyList_test() throws Exception {
        // given
        Integer id = 6;

        // mock
        List<ApplyJoinBoardAndResume> applyListPS = new ArrayList<>();
        ApplyJoinBoardAndResume applyJoinBoardAndResume = ApplyJoinBoardAndResumeBuilder.makeApplyJoinBoardAndResume(1,
                0,
                ApplyJoinBoardAndResumeBuilder.makeUser(1),
                ApplyJoinBoardAndResumeBuilder.makeBoard(1, "공고제목1"),
                ApplyJoinBoardAndResumeBuilder.makeResume(1, "이력서제목1"));
        applyListPS.add(applyJoinBoardAndResume);
        applyJoinBoardAndResume = ApplyJoinBoardAndResumeBuilder.makeApplyJoinBoardAndResume(2, 0,
                ApplyJoinBoardAndResumeBuilder.makeUser(2),
                ApplyJoinBoardAndResumeBuilder.makeBoard(2, "공고제목2"),
                ApplyJoinBoardAndResumeBuilder.makeResume(2, "이력서제목2"));
        applyListPS.add(applyJoinBoardAndResume);
        given(applyService.getApplyForEmployee(id)).willReturn(applyListPS);

        // when
        ResultActions resultActions = mvc.perform(get("/employee/" + id + "/apply"));
        String resp = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + resp);

        // then
        resultActions.andExpect(status().is2xxSuccessful());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.msg").value("지원 기업 리스트 불러오기 성공"));
        resultActions.andExpect(jsonPath("$.data[0].id").value(1));
        resultActions.andExpect(jsonPath("$.data[0].state").value(0));
        resultActions.andExpect(jsonPath("$.data[0].board.id").value(1));
        resultActions.andExpect(jsonPath("$.data[0].board.title").value("공고제목1"));
        resultActions.andExpect(jsonPath("$.data[0].user.id").value(1));
        resultActions.andExpect(jsonPath("$.data[0].resume.id").value(1));
        resultActions.andExpect(jsonPath("$.data[0].resume.title").value("이력서제목1"));
    }
}
