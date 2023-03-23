package shop.mtcoding.jobara.employee.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.jobara.common.util.DateParse;

public class EmployeeResp {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeUpdateRespDto {
        private Integer id;
        private String password;
        private String email;
        private String address;
        private String detailAddress;
        private String tel;
        private EmployeeDto employeeDto;

        @Getter
        @Setter
        @AllArgsConstructor
        public static class EmployeeDto {
            private String realName;
            private String education;
            private Integer career;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeAndResumeRespDto {
        private Integer id;
        private String email;
        private String address;
        private String detailAddress;
        private String tel;
        private String profile;
        private String role;
        private Timestamp createdAt;
        private EmployeeDto employeeDto;
        private ResumeDto resumeDto;

        public String getCreatedAtToString() {
            return DateParse.format(createdAt);
        }

        @Getter
        @Setter
        public static class EmployeeDto {
            private String realName;
            private String education;
            private Integer career;
        }

        @Getter
        @Setter
        public static class ResumeDto {
            private String title;
            private String content;
        }
    }

}
