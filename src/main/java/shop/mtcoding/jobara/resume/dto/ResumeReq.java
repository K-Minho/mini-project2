package shop.mtcoding.jobara.resume.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class ResumeReq {

    @Getter
    @Setter
    public static class ResumeSaveReq {
        private String title;
        @NotEmpty(message = "내용을 입력해주세요")
        private String content;
    }

    @Getter
    @Setter
    public static class ResumeUpdateReq {
        private Integer id;
        private String title;
        @NotEmpty(message = "내용을 입력해주세요")
        private String content;
    }
}
