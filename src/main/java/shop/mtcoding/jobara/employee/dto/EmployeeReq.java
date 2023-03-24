package shop.mtcoding.jobara.employee.dto;

import java.util.ArrayList;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class EmployeeReq {

      @Getter
      @Setter
      public static class EmployeeJoinReqDto {
            @NotEmpty(message = "유저이름을 입력해주세요.")
            @Size(min = 2, max = 16)
            private String username;
            @NotEmpty(message = "비밀번호를 입력해주세요.")
            @Size(min = 2, max = 16)
            private String password;
            @NotEmpty(message = "이메일을 입력해주세요.")
            @Size(min = 2, max = 32)
            private String email;
      }

      @Getter
      @Setter
      public static class EmployeeLoginReqDto {
            @NotEmpty(message = "유저이름을 입력해주세요.")
            @Size(min = 2, max = 16)
            private String username;
            @NotEmpty(message = "비밀번호를 입력해주세요.")
            @Size(min = 2, max = 16)
            private String password;
      }

      @Getter
      @Setter
      public static class EmployeeUpdateReqDto {
            @NotEmpty(message = "비밀번호를 입력해주세요.")
            @Size(min = 2, max = 16)
            private String password;
            @NotEmpty(message = "이메일을 입력해주세요.")
            @Size(min = 2, max = 32)
            private String email;
            @NotEmpty(message = "주소를 입력해주세요.")
            @Size(min = 2, max = 32)
            private String address;
            @NotEmpty(message = "상세 주소를 입력해주세요.")
            @Size(min = 2, max = 64)
            private String detailAddress;
            @NotEmpty(message = "전화번호를 입력해주세요.")
            @Size(min = 2, max = 16)
            private String tel;
            private EmployeeDto employeeDto;

            @Getter
            @Setter
            @AllArgsConstructor
            public static class EmployeeDto {

                  @NotEmpty(message = "실명을 입력해주세요.")
                  @Size(min = 2, max = 16)
                  private String realName;
                  @NotEmpty(message = "학력을 입력해주세요.")
                  @Size(min = 2, max = 16)
                  private String education;
                  @NotEmpty(message = "경력을 입력해주세요.")
                  @Size(min = 2, max = 16)
                  private Integer career;

            }
      }

      @Getter
      @Setter
      public static class EmployeeTechUpdateReqDto {
            private ArrayList<Integer> checkedValues;
      }

      @NoArgsConstructor
      @Getter
      @Setter
      public static class EmployeeInsertSkillReqDto {

            private Integer employeeId;
            private ArrayList<Integer> checkLang;

            public EmployeeInsertSkillReqDto(Integer employeeId, ArrayList<Integer> checkLang) {
                  this.employeeId = employeeId;
                  this.checkLang = checkLang;
            }

      }
}
