package shop.mtcoding.jobara.company;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.common.aop.CompanyCheck;
import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.common.util.Verify;
import shop.mtcoding.jobara.company.dto.CompanyReq.CompanyJoinReqDto;
import shop.mtcoding.jobara.company.dto.CompanyReq.CompanyUpdateReqDto;
import shop.mtcoding.jobara.company.dto.CompanyResp.CompanyInfo;
import shop.mtcoding.jobara.user.vo.UserVo;

@Controller
@RequiredArgsConstructor
public class CompanyConetroller {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private HttpSession session;

    @GetMapping("/company/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        UserVo principal = (UserVo) session.getAttribute("principal");
        CompanyInfo companyInfo = companyService.getCompany(principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "기업 회원 정보 불러오기 성공", companyInfo), HttpStatus.OK);
    }

    @PostMapping("/company/join")
    public ResponseEntity<?> join(@RequestBody CompanyJoinReqDto companyJoinReqDto) {
        Verify.validateApiString(companyJoinReqDto.getUsername(), "유저네임을 입력하세요.");
        Verify.validateApiString(companyJoinReqDto.getPassword(), "암호를 입력하세요.");
        Verify.validateApiString(companyJoinReqDto.getEmail(), "이메일을 입력하세요.");
        companyService.insertCompany(companyJoinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "기업 회원 가입 성공", null), HttpStatus.CREATED);
    }

    @PostMapping("/company/update")
    @CompanyCheck
    public ResponseEntity<?> update(CompanyUpdateReqDto companyUpdateReqDto) {
        UserVo principal = (UserVo) session.getAttribute("principal");

        Verify.validateApiString(companyUpdateReqDto.getPassword(), "암호를 입력하세요.");
        Verify.validateApiString(companyUpdateReqDto.getEmail(), "이메일을 입력하세요.");
        Verify.validateApiString(companyUpdateReqDto.getCompanyName(), "회사 이름을 입력하세요.");
        Verify.validateApiString(companyUpdateReqDto.getAddress(), "주소를 입력하세요.");
        Verify.validateApiString(companyUpdateReqDto.getDetailAddress(), "상세 주소를 입력하세요.");
        Verify.validateApiString(companyUpdateReqDto.getCompanyScale(), "회사 규모란을 선택하세요.");
        Verify.validateApiString(companyUpdateReqDto.getCompanyField(), "회사 업종란을 선택하세요.");
        Verify.validateApiString(companyUpdateReqDto.getTel(), "전화번호를 입력하세요.");

        UserVo UserVoPS = companyService.updateCompany(companyUpdateReqDto, principal.getId());
        session.setAttribute("principal", UserVoPS);

        return new ResponseEntity<>(new ResponseDto<>(1, "기업 회원 수정 성공", null), HttpStatus.CREATED);
    }
}
