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
    public ResponseEntity<?> updateResume(@PathVariable Integer id, @Valid @RequestBody ResumeUpdateReq resumeUpdateReq,
            BindingResult bindingResult) {

        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        if (resumeUpdateReq.getTitle().isEmpty() || resumeUpdateReq.getTitle().isBlank()) {
            resumeUpdateReq.setTitle("무제");
        }
        resumeService.updateResume(user.getId(), resumeUpdateReq);
        return new ResponseEntity<>(new ResponseDto<>(1, "작성 완료", null), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/{id}")
    public ResponseEntity<?> saveResumeForm(@PathVariable("id") Integer id, Model model) {

        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        Resume resumePS = resumeService.findById(user.getId(), id);
        model.addAttribute("resume", resumePS);
        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 상세보기", model), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/list")
    public ResponseEntity<?> resumeList(Model model) {

        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        List<Resume> resumeListPS = resumeService.findByUserId(user.getId());
        model.addAttribute("resumeList", resumeListPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 목록", model), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/saveForm")
    public String saveForm(Model model) {

        return "resume/saveForm";
    }

    @PostMapping("/employee/resume/save")
    public ResponseEntity<?> saveResume(@RequestBody @Valid ResumeSaveReq resumeSaveReq, BindingResult bindingResult) {

        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        if (resumeSaveReq.getTitle().isEmpty()) {
            resumeSaveReq.setTitle("무제");
        }
        resumeService.saveResume(user.getId(), resumeSaveReq);
        return new ResponseEntity<>(new ResponseDto<>(1, "작성 완료", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/employee/resume/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable int id) {

        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        resumeService.deleteResume(id, user.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제 완료", null), HttpStatus.OK);
    }
}
