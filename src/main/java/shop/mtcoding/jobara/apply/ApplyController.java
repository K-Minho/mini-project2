package shop.mtcoding.jobara.apply;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.apply.dto.ApplyReq.ApplyDecideReqDto;
import shop.mtcoding.jobara.apply.dto.ApplyReq.ApplyReqDto;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndResume;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndUser;
import shop.mtcoding.jobara.common.aop.CompanyCheckApi;
import shop.mtcoding.jobara.common.aop.EmployeeCheckApi;
import shop.mtcoding.jobara.common.config.auth.LoginUser;
import shop.mtcoding.jobara.common.dto.ResponseDto;

@RestController
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;
    private final HttpSession session;

    @PostMapping("/employee/apply")
    @EmployeeCheckApi
    public ResponseEntity<?> apply(@RequestBody @Valid ApplyReqDto applyReqDto, BindingResult bindingResult) {
        // 1. 기능 : 구직자가 공고에 구직 지원하는 메소드
        // 2. Arguments :
        // - ApplyReqDto
        //   (boardId, resumeId)
        //    boardId : 최소 1, null
        //    resumeId : 최소 1, null

        // 3. Return :

        // 작성자 : 김태훈
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        applyService.insertApply(applyReqDto, loginUser.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "지원 성공", null), HttpStatus.CREATED);
    }

    @GetMapping("/company/{id}/apply")
    @CompanyCheckApi
    public ResponseEntity<?> companyApplyList(@PathVariable Integer id) {
        // 1. 기능 : 해당 기업 회원의 지원자 목록을 불러오는 메소드
        // 2. Arguments :
        // - PathVariable : id, 해당 기업회원의 id이며, PK이다.

        // 3. Return :
        // - ApplyJoinBoardAndUser
        //   (id, state, createdAt,
        //    board(id, title),
        //    user(id, realName),
        //    resume(id))

        // 작성자 : 김태훈
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        List<ApplyJoinBoardAndUser> applyListPS = applyService.getApplyForCompany(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "지원자 리스트 불러오기 성공", applyListPS), HttpStatus.OK);
    }

    @PutMapping("/company/apply/{id}")
    @CompanyCheckApi
    public @ResponseBody ResponseEntity<?> decideApplyment(@PathVariable int id,
            @RequestBody @Valid ApplyDecideReqDto applyDecideReqDto, BindingResult bindingResult) {
        // 1. 기능 : 해당 기업 회원의 지원자의 합/불합 여부를 수정하는 메소드
        // 2. Arguments :
        // - PathVariable : id, 해당 지원의 id이며, PK이다.
        // - ApplyDecideReqDto
        //   (userId, state)
        //    boardId : 최소 1, null
        //    resumeId : 최소 -1 최대 1, null

        // 3. Return :

        // 작성자 : 김태훈
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        applyService.approveApply(applyDecideReqDto, id);
        if (applyDecideReqDto.getState() == 1) {
            return new ResponseEntity<>(new ResponseDto<>(1, "합격 처리 완료", null), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseDto<>(1, "불합격 처리 완료", null), HttpStatus.CREATED);
        }
    }

    @GetMapping("/employee/{id}/apply")
    @EmployeeCheckApi
    public ResponseEntity<?> employeeApplyList(@PathVariable Integer id) {
        // 1. 기능 : 해당 구직자 회원의 지원 목록을 불러오는 메소드
        // 2. Arguments :
        // - PathVariable : id, 해당 구직자회원의 id이며, PK이다.

        // 3. Return :
        // - ApplyJoinBoardAndResume
        //   (id, state, createdAt,
        //    board(id, title),
        //    user(id),
        //    resume(id, title))

        // 작성자 : 김태훈
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -
        List<ApplyJoinBoardAndResume> applyListPS = applyService.getApplyForEmployee(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "지원 기업 리스트 불러오기 성공", applyListPS), HttpStatus.OK);
    }
}
