package shop.mtcoding.jobara.apply;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndResume;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndUser;
import shop.mtcoding.jobara.apply.model.ApplyRepository;

@MybatisTest
public class ApplyRepositoryTest {

    @Autowired
    private ApplyRepository applyRepository;

    @Test
    public void findByUserIdWithBoardAndResume_test() throws Exception {
        // given
        ObjectMapper om = new ObjectMapper();
        int userId = 1;

        // when
        List<ApplyJoinBoardAndResume> EmployeeApplyRespDtoList = applyRepository.findByUserIdWithBoardAndResume(userId);
        String responseBody = om.writeValueAsString(EmployeeApplyRespDtoList);
        System.out.println("테스트 : " + responseBody);

        // then
        assertThat(EmployeeApplyRespDtoList.get(0).getId()).isEqualTo(1);
        assertThat(EmployeeApplyRespDtoList.get(0).getState()).isEqualTo(-1);
        assertThat(EmployeeApplyRespDtoList.get(0).getBoard().getId()).isEqualTo(1);
        assertThat(EmployeeApplyRespDtoList.get(0).getBoard().getTitle()).isEqualTo("인공지능 솔루션 (AI Solution) 개발");
        assertThat(EmployeeApplyRespDtoList.get(0).getUser().getId()).isEqualTo(1);
        assertThat(EmployeeApplyRespDtoList.get(0).getResume().getId()).isEqualTo(1);
        assertThat(EmployeeApplyRespDtoList.get(0).getResume().getTitle()).isEqualTo("뛰어난 컴퓨터 프로그래머 능력을 펼쳐보이겠습니다.");
    }
    
    @Test
    public void findByUserIdWithBoardAndBoard_test() throws Exception {
        // given
        ObjectMapper om = new ObjectMapper();
        int userId = 6;

        // when
        List<ApplyJoinBoardAndUser> EmployeeApplyRespDtoList = applyRepository.findByUserIdWithBoardAndUser(userId);
        String responseBody = om.writeValueAsString(EmployeeApplyRespDtoList);
        System.out.println("테스트 : " + responseBody);

        // then
        assertThat(EmployeeApplyRespDtoList.get(0).getId()).isEqualTo(1);
        assertThat(EmployeeApplyRespDtoList.get(0).getState()).isEqualTo("-1");
        assertThat(EmployeeApplyRespDtoList.get(0).getBoard().getId()).isEqualTo(1);
        assertThat(EmployeeApplyRespDtoList.get(0).getBoard().getTitle()).isEqualTo("인공지능 솔루션 (AI Solution) 개발");
        assertThat(EmployeeApplyRespDtoList.get(0).getUser().getId()).isEqualTo(1);
        assertThat(EmployeeApplyRespDtoList.get(0).getUser().getRealName()).isEqualTo("박종대");
        assertThat(EmployeeApplyRespDtoList.get(0).getResume().getId()).isEqualTo(1);
    }

}
