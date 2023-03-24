package shop.mtcoding.jobara.employee.dto;

import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeJoinReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeUpdateReqDto;

public class EmployeeUpdateBuilder extends EmployeeJoinReqDto {

    public static EmployeeUpdateReqDto makejoinReqDto(String username, String password, String email) {
        EmployeeUpdateReqDto makejoinReqDto = new EmployeeUpdateReqDto();

        return makejoinReqDto;
    }

}
