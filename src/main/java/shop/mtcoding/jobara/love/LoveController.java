package shop.mtcoding.jobara.love;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.common.aop.EmployeeCheck;
import shop.mtcoding.jobara.common.config.auth.LoginUser;
import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.love.dto.LoveReq.LoveSaveReqDto;

@RequiredArgsConstructor
@Controller
public class LoveController {

    private final HttpSession session;

    private final LoveService loveService;


    @PostMapping("/loves")
    @EmployeeCheck
    public ResponseEntity<?> save(@RequestBody LoveSaveReqDto loveSaveReqDto) {
        // 1. 기능 : 좋아요 요청에 따른 관련 요청 데이터 Service에 전달
        // 2. Arguments :
        // - LoveSaveReqDto (boardId, 좋아요 요청한 게시물의 id값)
        // 3. Return :
        // - 좋아요 성공이 되면 insert된 love의 PK값을 View에 전달하여 View에서 활용

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        // 유효성 검사
        if (loveSaveReqDto.getBoardId() == null) {
            throw new CustomApiException("boardId를 전달해 주세요");
        }

        LoginUser principal = (LoginUser) session.getAttribute("loginUser");        
        int loveId = loveService.insertLove(loveSaveReqDto.getBoardId(), principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요 성공", loveId), HttpStatus.CREATED);
    }

    @DeleteMapping("/loves/{id}")
    @EmployeeCheck
    public ResponseEntity<?> cancel(@PathVariable Integer id) {
        // 1. 기능 : 좋아요 취소 요청에 따른 관련 데이터 Service에 전달
        // 2. Arguments :
        // - PathVariable : id, 취소하려는 love의 id이며, PK이다.
        // 3. Return :
        // - 좋아요 취소가 되면 0값을 View에 전달하여 View에서 활용

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        LoginUser principal = (LoginUser) session.getAttribute("loginUser");

        loveService.deleteLove(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요취소 성공", 0), HttpStatus.OK);
    }

}
