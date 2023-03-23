package shop.mtcoding.jobara.company;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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

import shop.mtcoding.jobara.company.dto.CompanyReq.CompanyJoinReqDto;
import shop.mtcoding.jobara.company.dto.CompanyResp;
import shop.mtcoding.jobara.company.model.Company;
import shop.mtcoding.jobara.user.model.User;
import shop.mtcoding.jobara.user.vo.UserVo;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest extends CompanyResp {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private CompanyService companyService;

    private MockHttpSession mockSession;

    @BeforeEach
    public void setUp() {
        UserVo principal = new UserVo();
        principal.setId(1);
        principal.setUsername("ssar");
        principal.setRole("company");
        principal.setProfile(null);
        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", principal);
    }

    @Test
    public void detail_test() throws Exception {
        // given
        Integer id = 6;

        // mock
        User mockUser = new User(6, "cos",
                "1234", "1234",
                "cos@naver.com", "부산시",
                "부산야호", "01020202020",
                null, "company", Timestamp.valueOf(LocalDateTime.now()));
        Company mockCompany = new Company(1, "부산자동차", "대기업",
                123412322L, "자동차업");
        CompanyInfo companyInfo = new CompanyInfo(mockUser, mockCompany);

        given(companyService.getCompany(id)).willReturn(companyInfo);

        // when
        ResultActions resultActions = mvc.perform(get("/company/" + id));
        String resp = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + resp);

        // verify
        resultActions.andExpect(status().is2xxSuccessful());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.msg").value("기업 회원 정보 불러오기 성공"));
        resultActions.andExpect(jsonPath("$.data.id").value(6));
        resultActions.andExpect(jsonPath("$.data.username").value("cos"));
        resultActions.andExpect(jsonPath("$.data.email").value("cos@naver.com"));
        resultActions.andExpect(jsonPath("$.data.address").value("부산시"));
        resultActions.andExpect(jsonPath("$.data.detailAddress").value("부산야호"));
        resultActions.andExpect(jsonPath("$.data.tel").value("01020202020"));
        resultActions.andExpect(jsonPath("$.data.company.companyName").value("부산자동차"));
        resultActions.andExpect(jsonPath("$.data.company.companyScale").value("대기업"));
        resultActions.andExpect(jsonPath("$.data.company.companyField").value("자동차업"));
    }

    @Test
    public void join_test() throws Exception {
        // given
        CompanyJoinReqDto companyJoinReqDto = new CompanyJoinReqDto();
        companyJoinReqDto.setUsername("ssar");
        companyJoinReqDto.setPassword("1234");
        companyJoinReqDto.setEmail("ssar@naver.com");
        companyJoinReqDto.setAddress("부산시");
        companyJoinReqDto.setDetailAddress("부산야호");
        companyJoinReqDto.setCompanyName("부산자동차");
        companyJoinReqDto.setCompanyNumb(123411234L);

        // when
        ResultActions resultActions = mvc.perform(post("/company/join")
                .content(om.writeValueAsString(companyJoinReqDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(mockSession));
        String resp = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + resp);

        // then
        resultActions.andExpect(status().is2xxSuccessful());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.msg").value("기업 회원 가입 성공"));
    }
}
