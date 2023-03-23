package shop.mtcoding.jobara.employee.dto;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class EmployeeReq {

      @Getter
      @Setter
      public static class EmployeeJoinReqDto {
            private String username;
            private String password;
            private String email;
      }

      @Getter
      @Setter
      public static class EmployeeLoginReqDto {
            private String username;
            private String password;
      }

      @Getter
      @Setter
      public static class EmployeeUpdateReqDto {
            private String password;
            private String email;
            private String address;
            private String detailAddress;
            private String tel;
            private EmployeeDto employeeDto;

            @Getter
            @Setter
            public static class EmployeeDto {
                  private String realName;
                  private String education;
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
