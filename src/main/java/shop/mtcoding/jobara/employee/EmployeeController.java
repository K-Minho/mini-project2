package shop.mtcoding.jobara.employee;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.board.dto.BoardResp.PagingDto;
import shop.mtcoding.jobara.common.aop.EmployeeCheck;
import shop.mtcoding.jobara.common.aop.EmployeeCheckApi;
import shop.mtcoding.jobara.common.config.auth.LoginUser;
import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeJoinReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeTechUpdateReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeUpdateReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeResp.EmployeeAndResumeRespDto;
import shop.mtcoding.jobara.employee.dto.EmployeeResp.EmployeeUpdateRespDto;

@RequiredArgsConstructor
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    private final HttpSession session;

    @GetMapping("/joinForm")
    public String joinForm() {
        return "employee/joinForm";
    }

    @PutMapping("/employee/{id}")
    @EmployeeCheck
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id,
            @RequestBody @Valid EmployeeUpdateReqDto employeeUpdateReqDto, BindingResult bindingResult) {
        // 1. 기능 : 구직자 회원 정보를 수정 요청을 하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 해당 유저의 id이며 PK이다.
        // - employeeUpdateReqDto : 내부 요소들은 전부 String 타입이다.
        // password : 최소 2 ~ 최대 16,
        // email, address : 최소 2 ~ 최대 32,
        // detailAddress : 최소 2 ~ 최대 64,
        // employeeDto( 내부 요소들은 전부 String 타입이다.
        // realName, education, career : 최소 2 ~ 최대 16,
        // )

        // 3. Return : 없음.

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        employeeService.updateEmpolyee(employeeUpdateReqDto, id);
        return new ResponseEntity<>(new ResponseDto<>(1, "수정 완료", null),
                HttpStatus.valueOf(201));
    }

    @PutMapping("/employee/update/tech/{id}")
    @EmployeeCheckApi
    public ResponseEntity<?> update(@PathVariable int id,
            @RequestBody @Valid EmployeeTechUpdateReqDto employeeTechUpdateReqDto, Model model,
            BindingResult bindingResult) {
        // 1. 기능 : 구직자 회원의 관심/보유 기술 정보를 수정 요청을 하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 해당 유저의 id이며 PK이다.
        // - employeeTechUpdateReqDto : checkedValues 체크박스에서 체크된 각각의 value들이 들어있는
        // List이다. List 내부의 요소들은 int 타입이다.

        // 3. Return : 없음

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        if (employeeTechUpdateReqDto.getCheckedValues() != null) {
            employeeService.updateEmpolyeeTech(employeeTechUpdateReqDto.getCheckedValues(), id);
        }
        return new ResponseEntity<>(new ResponseDto<>(1, "기술 수정 완료", null),
                HttpStatus.valueOf(201));
    }

    @GetMapping("/user/{id}/resume/{resumeId}")
    public ResponseEntity<?> employeeDetail(@PathVariable int id, @PathVariable int resumeId, Model model) {
        // 1. 기능 : 구직자 회원 정보중 특정 이력서의 조회 요청을 하는 메서드
        // 2. Arguments :
        // - PathVariable :
        // id, 해당 유저의 id이며 PK이다.
        // resumeid, 해당 유저가 작성한 이력서의 id이며 PK이다.
        //

        // 3. Return : employee 타입 EmployeeAndResumeRespDto
        // id : Integer 타입 PK이다.
        // email, address : 최소 2 ~ 최대 32,
        // detailAddress : 최소 2 ~ 최대 64,
        // tel : 최소 2 ~ 최대 16,
        // profile : 최소 2 ~ 최대 65536,
        // role : 필수값이 아님
        // createdAt : 필수값이 아님, timestamp 형식이다.
        // employeeDto( 내부 요소들은 전부 String 타입이다.
        // realName, education, career : 최소 2 ~ 최대 16,
        // )
        // resumeDto( 내부 요소들은 전부 String 타입이다.
        // title : 최소 2 ~ 최대 32,
        // content : 최소 2 ~ 최대 65536,
        // )
        // employeeTech : List 타입, 내부의 요소들은 String 타입이다.

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        EmployeeAndResumeRespDto employeePS = employeeService.getEmployee(id, resumeId);
        List<String> employeeTechPS = employeeService.getEmployeeTech(id);
        model.addAttribute("employee", employeePS);
        model.addAttribute("employeeTech", employeeTechPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "상세 페이지", model), HttpStatus.valueOf(200));
    }

    @GetMapping("/employee/{id}")
    @EmployeeCheck
    public ResponseEntity<?> updateForm(@PathVariable int id, Model model) {
        // 1. 기능 : 구직자 회원의 회원 정보 수정 페이지로 이동하는 요청을 하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 해당 유저의 id이며 PK이다.

        // 3. Return : employeeDto EmployeeUpdateRespDto 타입
        // id : Integer 타입 PK이다.
        // email, address : 최소 2 ~ 최대 32,
        // detailAddress : 최소 2 ~ 최대 64,
        // tel : 최소 2 ~ 최대 16,
        // profile : 최소 2 ~ 최대 65536,
        // role : 필수값이 아님
        // createdAt : 필수값이 아님, timestamp 형식이다.
        // employeeDto( 내부 요소들은 전부 String 타입이다.
        // realName, education, career : 최소 2 ~ 최대 16,
        // )
        // resumeDto( 내부 요소들은 전부 String 타입이다.
        // title : 최소 2 ~ 최대 32,
        // content : 최소 2 ~ 최대 65536,
        // )
        // employeeSkill : List 타입, 내부의 요소들은 Integer 타입이다.

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        EmployeeUpdateRespDto employeeUpdateRespDto = employeeService.getEmployeeUpdateRespDto(id);
        List<Integer> employeeSkill = employeeService.getSkillForDetail(id);
        model.addAttribute("employeeDto", employeeUpdateRespDto);
        model.addAttribute("employeeSkill", employeeSkill);
        return new ResponseEntity<>(new ResponseDto<>(1, "수정 페이지", model), HttpStatus.valueOf(200));
    }

    @GetMapping("/list")
    public @ResponseBody ResponseEntity<?> employeeList(Model model, Integer page) {
        // 1. 기능 : 구직자 회원 가입 요청을 하는 메서드
        // 2. Arguments :
        // - employeeJoinReqDto EmployeeJoinReqDto타입 내부 요소들은 전부 String 타입이다.
        // username, password : 최소 2 ~ 최대 16,
        // email : 최소 2 ~ 최대 32

        // 3. Return : employee 타입 EmployeeAndResumeRespDto
        // id : Integer 타입 PK이다.
        // email, address : 최소 2 ~ 최대 32,
        // detailAddress : 최소 2 ~ 최대 64,
        // tel : 최소 2 ~ 최대 16,
        // profile : 최소 2 ~ 최대 65536,
        // role : 필수값이 아님
        // createdAt : 필수값이 아님, timestamp 형식이다.
        // employeeDto( 내부 요소들은 전부 String 타입이다.
        // realName, education, career : 최소 2 ~ 최대 16,
        // )
        // resumeDto( 내부 요소들은 전부 String 타입이다.
        // title : 최소 2 ~ 최대 32,
        // content : 최소 2 ~ 최대 65536,
        // )
        // employeeTech : List 타입, 내부의 요소들은 String 타입이다.

        // 추가적으로 기업유저의 로그인일경우 recommendEmployeeListPS를 추가한다.
        // 타입은 EmployeeAndResumeRespDto로 같다.

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        PagingDto pagingPS = employeeService.getEmployee(page);
        model.addAttribute("pagingDto", pagingPS);
        if (loginUser != null) {
            if (loginUser.getRole().equals("company")) {
                List<EmployeeAndResumeRespDto> recommendEmployeeListPS = employeeService
                        .getRecommendEmployee(loginUser.getId());
                model.addAttribute("recommendEmployeeList", recommendEmployeeListPS);
            }
        }
        return new ResponseEntity<>(new ResponseDto<>(1, "목록 페이지", model), HttpStatus.valueOf(200));
    }

    @PostMapping("/joinEmployee")
    public ResponseEntity<?> join(@RequestBody @Valid EmployeeJoinReqDto employeeJoinReqDto,
            BindingResult bindingResult) {
        // 1. 기능 : 구직자 회원 가입 요청을 하는 메서드
        // 2. Arguments :
        // - employeeJoinReqDto EmployeeJoinReqDto타입 내부 요소들은 전부 String 타입이다.
        // username, password : 최소 2 ~ 최대 16,
        // email : 최소 2 ~ 최대 32

        // 3. Return : 없음

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        employeeService.insertEmployee(employeeJoinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "가입 완료", null), HttpStatus.valueOf(201));
    }

    @GetMapping("/user/{id}")
    public @ResponseBody ResponseEntity<?> employeeDetail(@PathVariable int id, Model model) {
        // 1. 기능 : 구직자 상세보기 페이지를 호출 하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 해당 유저의 id이며 PK이다.

        // 3. Return : employee 타입 EmployeeAndResumeRespDto
        // id : Integer 타입 PK이다.
        // email, address : 최소 2 ~ 최대 32,
        // detailAddress : 최소 2 ~ 최대 64,
        // tel : 최소 2 ~ 최대 16,
        // profile : 최소 2 ~ 최대 65536,
        // role : 필수값이 아님
        // createdAt : 필수값이 아님, timestamp 형식이다.
        // employeeDto( 내부 요소들은 전부 String 타입이다.
        // realName, education, career : 최소 2 ~ 최대 16,
        // )
        // resumeDto( 내부 요소들은 전부 String 타입이다.
        // title : 최소 2 ~ 최대 32,
        // content : 최소 2 ~ 최대 65536,
        // )
        // employeeTech : List 타입, 내부의 요소들은 String 타입이다.

        // 작성자 : 강민호
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        EmployeeAndResumeRespDto employeePS = employeeService.getEmployee(id);
        List<String> employeeTechPS = employeeService.getEmployeeTech(id);
        model.addAttribute("employee", employeePS);
        model.addAttribute("employeeTech", employeeTechPS);
        return new ResponseEntity<>(new ResponseDto<>(1, "상세 페이지", model), HttpStatus.valueOf(200));
    }
}