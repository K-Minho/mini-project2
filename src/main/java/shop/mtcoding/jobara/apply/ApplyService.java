package shop.mtcoding.jobara.apply;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.apply.dto.ApplyReq.ApplyDecideReqDto;
import shop.mtcoding.jobara.apply.dto.ApplyReq.ApplyReqDto;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndResume;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndUser;
import shop.mtcoding.jobara.apply.dto.ApplyResp.MailDto;
import shop.mtcoding.jobara.apply.model.Apply;
import shop.mtcoding.jobara.apply.model.ApplyRepository;
import shop.mtcoding.jobara.board.model.Board;
import shop.mtcoding.jobara.board.model.BoardRepository;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.common.util.Mail;
import shop.mtcoding.jobara.common.util.Verify;
import shop.mtcoding.jobara.user.model.User;
import shop.mtcoding.jobara.user.model.UserRepository;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final Mail mail;

    @Transactional
    public void insertApply(ApplyReqDto applyReqDto, Integer principalId) {
        // @PostMapping("/employee/apply")에 의해 호출됨.
        // 기능 : Controller에서 지원 추가 요청을 전달받아서 DB에 해당 지원 저장
        // 사용되는 요소 : 
        // 진행 과정 :
        // 1. 해당 공고가 DB에 존재하는지 확인하기
        //  - findById 메서드를 통해 데이터(공고글)가 존재하는지 확인한다.
        //  - 존재하지 않는다면, 예외를 처리한다. (msg: "존재하지 않는 게시물 입니다.")
        //  - apply.getBoardId()는 해당 공고가 존재하는지 여부를 확인하는데 사용된다.
        // 2. 해당 공고에 동일 회원이 지원했는지 여부 확인
        //  - findByUserIdAndBoardId 메서드를 통해 데이터(지원)가 존재하는지 확인한다.
        //  - 존재한다면, 예외를 처리한다. (msg: "이미 지원한 공고입니다.)
        // 3. 해당 지원 DB에 저장
        //  - 위 과정을 거친 뒤 DB에 insert한다.
        //  - DB 데이터 처리 과정에서 예외가 발생하면 예외 처리한다. (msg: "서버 에러 : 지원 실패")

        // 작성자 : 김태훈
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        Apply apply = new Apply(principalId, applyReqDto);
        Board boardPS = boardRepository.findById(apply.getBoardId());
        Verify.validateApiObject(boardPS, "존재하지 않는 게시물 입니다.");
        Verify.isNotEqualApi(applyRepository.findByUserIdAndBoardId(apply), null, "이미 지원한 공고입니다.",
                HttpStatus.BAD_REQUEST);
        try {
            applyRepository.insert(apply);
        } catch (Exception e) {
            throw new CustomApiException("서버 에러 : 지원 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<ApplyJoinBoardAndUser> getApplyForCompany(Integer principalId) {
        return applyRepository.findByUserIdWithBoardAndUser(principalId);
    }

    @Transactional(readOnly = true)
    public List<ApplyJoinBoardAndResume> getApplyForEmployee(Integer principalId) {
        return applyRepository.findByUserIdWithBoardAndResume(principalId);
    }

    @Transactional
    public void approveApply(ApplyDecideReqDto applyDecideReqDto, int boradId) {
        // @PutMapping("/company/apply/{id}")에 의해 호출됨.
        // 기능 : Controller에서 지원 수정 요청을 전달받아서 DB에 해당 지원 수정
        // 사용되는 요소 : 
        // 진행 과정 :
        // 1. 해당 구직자가 DB에 존재하는지 확인하기
        //  - findById 메서드를 통해 데이터(구직자)가 존재하는지 확인한다.
        //  - 존재하지 않는다면, 예외를 처리한다. (msg: "존재하지 않는 유저입니다.")
        //  - applyDecideReqDto.getUserId()는 해당 회원이 존재하는지 여부를 확인하는데 사용된다.
        // 2. 해당 지원이 DB에 존재하는지 확인하기
        //  - findByUserIdAndBoardId 메서드를 통해 데이터(지원)가 존재하는지 확인한다.
        //  - 존재하지 않는다면, 예외를 처리한다. (msg: "존재하지 않는 지원입니다.")
        // 3. 해당 지원 DB에 저장
        //  - 위 과정을 거친 뒤 DB에 update한다.
        //  - DB 데이터 처리 과정에서 예외가 발생하면 예외 처리한다. (msg: "서버 에러 : 결과 처리 실패")
        // 4. 지원 처리 결과 Mail 전송
        //  - findByIdWithBoardForMail 메서드를 통해 해당 지원 결과를 메일로 전송하기 위해 필요한 데이터를 가져온다.
        //  - sendMail 메서드로 메일을 전송한다.
        //  - 동기화를 위해서 해당 메서드들을 새 스레드에서 실행한다.

        // 작성자 : 김태훈
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        User user = userRepository.findById(applyDecideReqDto.getUserId());
        Verify.validateApiObject(user, "존재하지 않는 유저입니다.");
        Apply apply = new Apply(boradId, applyDecideReqDto.getUserId());
        Apply applyPS = applyRepository.findByUserIdAndBoardId(apply);
        Verify.isEqualApi(applyRepository.findByUserIdAndBoardId(apply), null,
                "존재하지 않는 지원입니다.", HttpStatus.BAD_REQUEST);
        applyPS.setState(applyDecideReqDto.getState());
        try {
            applyRepository.updateById(applyPS);
        } catch (Exception e) {
            throw new CustomApiException("서버 에러 : 결과 처리 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            new Thread(() -> {
                MailDto mailDto = applyRepository.findByIdWithBoardForMail(applyPS.getId());
                mail.sendMail(mailDto, user.getEmail());
            }).start();

        } catch (Exception e) {
            // 로그만 남기고!!
        }
    }

}
