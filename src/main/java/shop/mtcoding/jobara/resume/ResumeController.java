package shop.mtcoding.jobara.resume;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.common.aop.EmployeeCheck;
import shop.mtcoding.jobara.common.aop.EmployeeCheckApi;
import shop.mtcoding.jobara.common.config.auth.LoginUser;
import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.resume.dto.ResumeReq.ResumeSaveReq;
import shop.mtcoding.jobara.resume.dto.ResumeReq.ResumeUpdateReq;
import shop.mtcoding.jobara.resume.model.Resume;

@RestController
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final HttpSession session;

    @PutMapping("/employee/resume/{id}")
    @EmployeeCheckApi
    public ResponseEntity<?> updateResume(@PathVariable Integer id, @Valid @RequestBody ResumeUpdateReq resumeUpdateReq,
            BindingResult bindingResult) {
        // 1. 기능 : 구직자 회원의 이력서 수정 요청을 하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 해당 이력서의 id이며 PK이다.
        // - resumeUpdateReq ResumeUpdateReq
        // id : 수정 요청을 하는 해당 이력서의 id이다 . Integer 타입 PK이다.
        // title : 필수값이 아님. 없을시 "무제"로 등록 됨.
        // content : 최소 2 ~ 최대 65546

        // 3. Return : 없음

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -
        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        if (resumeUpdateReq.getTitle().isEmpty() || resumeUpdateReq.getTitle().isBlank()) {
            resumeUpdateReq.setTitle("무제");
        }
        resumeService.updateResume(user.getId(), resumeUpdateReq);
        return new ResponseEntity<>(new ResponseDto<>(1, "작성 완료", null), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/{id}")
    @EmployeeCheck
    public ResponseEntity<?> saveResumeForm(@PathVariable("id") Integer id, Model model) {
        // 1. 기능 : 구직자 회원의 이력서 상세보기를 요청을 하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 해당 이력서의 id이며 PK이다.

        // 3. Return : Resume
        // id : Integer
        // userId : Integer 
        // title : String
        // content : String
        // createdAt : Timestamp

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -
        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        Resume resumePS = resumeService.findById(user.getId(), id);
        model.addAttribute("resume", resumePS);
        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 상세보기", model), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/list")
    @EmployeeCheck
    public ResponseEntity<?> resumeList(Model model) {

        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        List<Resume> resumeListPS = resumeService.findByUserId(user.getId());
        model.addAttribute("resumeList", resumeListPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 목록", model), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/saveForm")
    @EmployeeCheck
    public String saveForm(Model model) {

        return "resume/saveForm";
    }

    @PostMapping("/employee/resume/save")
    @EmployeeCheckApi
    public ResponseEntity<?> saveResume(@RequestBody @Valid ResumeSaveReq resumeSaveReq, BindingResult bindingResult) {

        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        if (resumeSaveReq.getTitle().isEmpty()) {
            resumeSaveReq.setTitle("무제");
        }
        resumeService.saveResume(user.getId(), resumeSaveReq);
        return new ResponseEntity<>(new ResponseDto<>(1, "작성 완료", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/employee/resume/{id}")
    @EmployeeCheckApi
    public ResponseEntity<?> deleteResume(@PathVariable int id) {

        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        resumeService.deleteResume(id, user.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제 완료", null), HttpStatus.OK);
    }
}
