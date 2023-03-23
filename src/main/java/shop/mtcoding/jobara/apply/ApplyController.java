package shop.mtcoding.jobara.apply;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import shop.mtcoding.jobara.apply.dto.ApplyReq.ApplyDecideReqDto;
import shop.mtcoding.jobara.apply.dto.ApplyReq.ApplyReqDto;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndResume;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndUser;
import shop.mtcoding.jobara.common.aop.CompanyCheck;
import shop.mtcoding.jobara.common.aop.CompanyCheckApi;
import shop.mtcoding.jobara.common.aop.EmployeeCheck;
import shop.mtcoding.jobara.common.aop.EmployeeCheckApi;
import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.common.util.Verify;
import shop.mtcoding.jobara.user.vo.UserVo;

@RestController
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private HttpSession session;

    @PostMapping("/employee/apply")
    @EmployeeCheckApi
    public ResponseEntity<?> apply(@RequestBody ApplyReqDto applyReqDto) {
        UserVo principal = (UserVo) session.getAttribute("principal");
        applyService.insertApply(applyReqDto, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "지원 성공", null), HttpStatus.CREATED);
    }

    @GetMapping("/company/{id}/apply")
    @CompanyCheck
    public ResponseEntity<?> companyApplyList(@PathVariable Integer id) {
        List<ApplyJoinBoardAndUser> applyListPS = applyService.getApplyForCompany(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "지원자 리스트 불러오기 성공", applyListPS), HttpStatus.OK);
    }

    @PutMapping("/company/apply/{id}")
    @CompanyCheckApi
    public @ResponseBody ResponseEntity<?> decideApplyment(@PathVariable int id,
            @RequestBody ApplyDecideReqDto applyDecideReqDto) {
        Verify.validateApiObject(applyDecideReqDto.getUserId(), "처리할 유저 Id를 입력하세요.");
        Verify.validateApiObject(applyDecideReqDto.getState(), "처리할 결과 코드를 입력하세요.");
        applyService.approveApply(applyDecideReqDto, id);
        if (applyDecideReqDto.getState() == 1) {
            return new ResponseEntity<>(new ResponseDto<>(1, "합격 처리 완료", null), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseDto<>(1, "불합격 처리 완료", null), HttpStatus.CREATED);
        }
    }

    @GetMapping("/employee/{id}/apply")
    @EmployeeCheck
    public ResponseEntity<?> employeeApplyList(@PathVariable Integer id) {
        List<ApplyJoinBoardAndResume> applyListPS = applyService.getApplyForEmployee(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "지원 기업 리스트 불러오기 성공", applyListPS), HttpStatus.OK);
    }
}
