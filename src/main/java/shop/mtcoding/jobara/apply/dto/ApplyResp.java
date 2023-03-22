package shop.mtcoding.jobara.apply.dto;

import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class ApplyResp {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    public static class ApplyJoinBoardAndUser {
        private Integer id;
        private String state;
        private Timestamp createdAt;
        private BoardDto board;
        private UserDto user;
        private ResumeDto resume;

        @Getter
        @Setter(AccessLevel.PROTECTED)
        public static class BoardDto {
            private Integer id;
            private String title;
        }

        @Getter
        @Setter(AccessLevel.PROTECTED)
        public static class UserDto {
            private Integer id;
            private String realName;
        }

        @Getter
        @Setter(AccessLevel.PROTECTED)
        public static class ResumeDto {
            private Integer id;
        }
    }

    @Getter
    @Setter(AccessLevel.PROTECTED)
    public static class ApplyJoinBoardAndResume {
        private Integer id;
        private Integer state;
        private Timestamp createdAt;
        private BoardDto board;
        private UserDto user;
        private ResumeDto resume;

        @Getter
        @Setter(AccessLevel.PROTECTED)
        public static class BoardDto {
            private Integer id;
            private String title;
        }

        @Getter
        @Setter(AccessLevel.PROTECTED)
        public static class UserDto {
            private Integer id;
        }

        @Getter
        @Setter(AccessLevel.PROTECTED)
        public static class ResumeDto {
            private Integer id;
            private String title;
        }
    }

    @Getter
    @Setter
    public static class MailDto {
        private Integer id;
        private Integer userId;
        private Integer boardId;
        private Integer resumeId;
        private Integer state;
        private String boardTitle;
    }
}
