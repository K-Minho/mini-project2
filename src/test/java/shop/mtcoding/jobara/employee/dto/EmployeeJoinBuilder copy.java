package shop.mtcoding.jobara.employee.dto;

import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeJoinReqDto;

public class EmployeeJoinBuilder extends EmployeeJoinReqDto {

    public static EmployeeJoinReqDto makejoinReqDto(String username, String password, String email) {
        EmployeeJoinReqDto makejoinReqDto = new EmployeeJoinReqDto();
        makejoinReqDto.setUsername(username);
        makejoinReqDto.setPassword(password);
        makejoinReqDto.setEmail(email);

        return makejoinReqDto;
    }

}
