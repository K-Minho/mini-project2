package shop.mtcoding.jobara.board.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BoardReq {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class BoardUpdateReqDto {

        private Integer id;

        @NotEmpty(message = "제목을 입력하세요")
        private String title;

        @NotEmpty(message = "내용을 입력하세요")
        private String content;

        private Integer career;

        @NotEmpty(message = "경력을 선택하세요")
        private String careerString;

        private Integer education;

        @NotEmpty(message = "학력을 선택하세요")
        private String educationString;

        private Integer jobType;

        @NotEmpty(message = "직무를 선택하세요")
        private String jobTypeString;

        @NotEmpty(message = "마감 날짜를 선택하세요")
        private String deadline;

        @Length(max = 16, message = "우대사항은 최대 16자까지 입력 가능합니다")
        private String favor;

        private Integer userId;

        @NotEmpty(message = "선호 기술을 한 가지 이상 선택하세요")
        private List<Integer> checkedValues;

        // private Integer id;
        // private String title;
        // private String content;

        // private Integer career;
        // private String careerString;
        // private Integer education;
        // private String educationString;
        // private Integer jobType;
        // private String jobTypeString;
        // private String deadline;

        // private String favor;
        // private Integer userId;

        // private List<Integer> checkedValues;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BoardInsertReqDto {
        private String title;
        private String content;

        private Integer career;
        private String careerString;
        private Integer education;
        private String educationString;
        private Integer jobType;
        private String jobTypeString;
        private String deadline;

        private String favor;
        private Integer userId;
        private List<Integer> checkLang;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class BoardInsertSkillReqDto {
        private Integer boardId;
        private List<Integer> checkLang;

        public BoardInsertSkillReqDto(Integer boardId, List<Integer> checkLang) {
            this.boardId = boardId;
            this.checkLang = checkLang;
        }

    }
}
