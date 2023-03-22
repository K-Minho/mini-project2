package shop.mtcoding.jobara.apply;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import shop.mtcoding.jobara.user.UserController;
import shop.mtcoding.jobara.user.UserService;
import shop.mtcoding.jobara.user.vo.UserVo;

@WebMvcTest(UserController.class)
public class ApplyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

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
}
