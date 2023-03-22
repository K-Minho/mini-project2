package shop.mtcoding.jobara.board.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BoardReq {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class BoardUpdateReqDto {
        private Integer id;
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

        private List<Integer> checkedValues;

        public BoardUpdateReqDto(Integer id, String title, String content, String careerString, String educationString,
                String jobTypeString, String deadline, String favor, Integer userId, List<Integer> checkedValues) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.careerString = careerString;
            this.educationString = educationString;
            this.jobTypeString = jobTypeString;
            this.deadline = deadline;
            this.favor = favor;
            this.userId = userId;
            this.checkedValues = checkedValues;
        }

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

        public BoardInsertReqDto(String title, String content, String careerString,
                String educationString, String jobTypeString, String deadline, String favor, Integer userId,
                List<Integer> checkLang) {
            this.title = title;
            this.content = content;
            this.careerString = careerString;
            this.educationString = educationString;
            this.jobTypeString = jobTypeString;
            this.deadline = deadline;
            this.favor = favor;
            this.userId = userId;
            this.checkLang = checkLang;
        }

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
