package shop.mtcoding.jobara.resume;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.resume.dto.ResumeReq.ResumeSaveReq;
import shop.mtcoding.jobara.resume.dto.ResumeReq.ResumeUpdateReq;
import shop.mtcoding.jobara.resume.model.Resume;
import shop.mtcoding.jobara.user.vo.UserVo;

@RestController
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    public UserVo setPrincipal() {
        return new UserVo(1, "ssar", "", "employee");
    }

    @PutMapping("/employee/resume/{id}")
    public ResponseEntity<?> updateResume(@PathVariable Integer id, @Valid @RequestBody ResumeUpdateReq resumeUpdateReq,
            BindingResult bindingResult) {
        UserVo principal = setPrincipal();
        if (resumeUpdateReq.getTitle().isEmpty() || resumeUpdateReq.getTitle().isBlank()) {
            resumeUpdateReq.setTitle("무제");
        }
        resumeService.updateResume(principal.getId(), resumeUpdateReq);
        return new ResponseEntity<>(new ResponseDto<>(1, "작성 완료", null), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/{id}")
    public ResponseEntity<?> saveResumeForm(@PathVariable("id") Integer id, Model model, BindingResult bindingResult) {
        UserVo principal = setPrincipal();
        Resume resumePS = resumeService.findById(principal.getId(), id);
        model.addAttribute("resume", resumePS);
        return new ResponseEntity<>(new ResponseDto<>(1, "", model), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/list")
    public ResponseEntity<?> resumeList(Model model) {

        UserVo principal = setPrincipal();
        List<Resume> resumeListPS = resumeService.findByUserId(principal.getId());
        model.addAttribute("resumeList", resumeListPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "", model), HttpStatus.OK);
    }

    @GetMapping("/employee/resume/saveForm")
    public String saveForm(Model model) {

        return "resume/saveForm";
    }

    @PostMapping("/employee/resume/save")
    public ResponseEntity<?> saveResume(@RequestBody @Valid ResumeSaveReq resumeSaveReq, BindingResult bindingResult) {

        UserVo principal = setPrincipal();
        if (resumeSaveReq.getTitle().isEmpty()) {
            resumeSaveReq.setTitle("무제");
        }
        resumeService.saveResume(principal.getId(), resumeSaveReq);
        return new ResponseEntity<>(new ResponseDto<>(1, "작성 완료", resumeSaveReq), HttpStatus.CREATED);
    }

    @DeleteMapping("/employee/resume/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable int id) {

        UserVo principal = setPrincipal();
        resumeService.deleteResume(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제 완료", null), HttpStatus.OK);
    }
}
