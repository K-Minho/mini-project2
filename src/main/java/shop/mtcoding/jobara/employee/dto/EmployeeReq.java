package shop.mtcoding.jobara.employee.dto;

import java.util.ArrayList;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class EmployeeReq {

      @Getter
      @Setter
      public static class EmployeeJoinReqDto {
            @NotEmpty
            @Min(2)
            @Max(16)
            private String username;
            @NotEmpty
            @Min(2)
            @Max(16)
            private String password;
            @NotEmpty
            @Min(2)
            @Max(32)
            private String email;
      }

      @Getter
      @Setter
      public static class EmployeeLoginReqDto {
            @NotEmpty
            @Min(2)
            @Max(16)
            private String username;
            @NotEmpty
            @Min(2)
            @Max(16)
            private String password;
      }

      @Getter
      @Setter
      public static class EmployeeUpdateReqDto {
            @NotEmpty
            @Min(2)
            @Max(16)
            private String password;
            @NotEmpty
            @Min(2)
            @Max(32)
            private String email;
            @NotEmpty
            @Min(2)
            @Max(32)
            private String address;
            @NotEmpty
            @Min(2)
            @Max(64)
            private String detailAddress;
            @NotEmpty
            @Min(2)
            @Max(16)
            private String tel;
            @NotEmpty
            private EmployeeDto employeeDto;

            @Getter
            @Setter
            public static class EmployeeDto {

                  @NotEmpty
                  @Min(2)
                  @Max(16)
                  private String realName;
                  @NotEmpty
                  @Min(2)
                  @Max(16)
                  private String education;
                  @NotEmpty
                  @Min(2)
                  @Max(16)
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
            @NotEmpty
            private Integer employeeId;
            private ArrayList<Integer> checkLang;

            public EmployeeInsertSkillReqDto(Integer employeeId, ArrayList<Integer> checkLang) {
                  this.employeeId = employeeId;
                  this.checkLang = checkLang;
            }

      }
}
