package shop.mtcoding.jobara.company;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.common.util.Hash;
import shop.mtcoding.jobara.common.util.MyBase64Decoder;
import shop.mtcoding.jobara.common.util.Verify;
import shop.mtcoding.jobara.company.dto.CompanyReq.CompanyJoinReqDto;
import shop.mtcoding.jobara.company.dto.CompanyReq.CompanyUpdateReqDto;
import shop.mtcoding.jobara.company.dto.CompanyResp.CompanyInfo;
import shop.mtcoding.jobara.company.model.Company;
import shop.mtcoding.jobara.company.model.CompanyRepository;
import shop.mtcoding.jobara.user.model.User;
import shop.mtcoding.jobara.user.model.UserRepository;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CompanyInfo getCompany(Integer principalId) {
        User user = userRepository.findById(principalId);
        Company company = companyRepository.findByUserId(principalId);
        CompanyInfo companyInfo = new CompanyInfo(user, company);
        return companyInfo;
    }

    @Transactional
    public void insertCompany(CompanyJoinReqDto companyJoinReqDto) {
        // @PostMapping("/joinCompany")에 의해 호출됨.
        // 기능 : Controller에서 회원 가입 요청 데이터를 받아서 DB에 해당 회원 정보 저장
        // 사용되는 요소 : Hash 함수 인코딩
        // 진행 과정 :
        // 1. 입력받은 username과 동일한 회원이 존재하는지 확인
        //  - findByUsername 메서드를 통해 데이터(회원)가 존재하는지 확인한다.
        //  - 존재한다면, 예외를 처리한다. (msg: "이미 존재하는 유저네임 입니다.")
        //  - companyJoinReqDto.getUsername()는 해당 공고가 존재하는지 여부를 확인하는데 사용된다.
        // 2. salt 생성 및 Hash 함수로 비밀번호 암호화 
        //  - Hash.makeSalt() : SHA1PRNG방식으로 임의의 값을 생성한다. salt를 사용하여 비밀번호를 재암호화 하기 위하여 사용.
        //  - Hash.encode() : 입력 받은 비밀번호와 salt값을 SHA-256 방식으로 암호화한다. 비밀번호 암호화를 위해 사용.
        // 3. 해당 지원 DB에 저장
        //  - 위 과정을 거친 뒤 DB에 insert한다.
        //  - DB 데이터 처리 과정에서 예외가 발생하면 예외 처리한다. (msg: "서버 오류: 회원 가입 실패")

        // 작성자 : 김태훈
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        Verify.isNotEqualApi(
                userRepository.findByUsername(companyJoinReqDto.getUsername()), null, "이미 존재하는 유저네임 입니다.",
                HttpStatus.BAD_REQUEST);
        String salt = Hash.makeSalt();
        String hashPassword = Hash.encode(companyJoinReqDto.getPassword() + salt);
        User user = new User(companyJoinReqDto, hashPassword, salt);
        try {
            userRepository.insertForCompany(user);
            Company company = new Company(user.getId(), companyJoinReqDto.getCompanyName(),
                    companyJoinReqDto.getCompanyNumb());
            companyRepository.insert(company);
        } catch (Exception e) {
            throw new CustomApiException("서버 오류: 회원 가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateCompany(CompanyUpdateReqDto companyUpdateReqDto, Integer principalId) {
        // @PutMapping("/company/{id}")에 의해 호출됨.
        // 기능 : Controller에서 회원 수정 요청 데이터를 받아서 DB에 해당 회원 정보 수정
        // 사용되는 요소 : Hash 함수 인코딩
        // 진행 과정 :
        // 1. profile을 파일로 저장하기 위한 디코딩
        //  - MyBase64Decoder.saveImage() : 문자열 형태가 base64형태로 작성되었는지 여부 확인, 
        //  - 해당 문자열을 base64로 디코딩하여 파일로 저장,
        //  - 저장된 경로값을 Service로 전달한다.
        // 2. salt 생성 및 Hash 함수로 비밀번호 암호화 
        //  - Hash.makeSalt() : SHA1PRNG방식으로 임의의 값을 생성한다. salt를 사용하여 비밀번호를 재암호화 하기 위하여 사용.
        //  - Hash.encode() : 입력 받은 비밀번호와 salt값을 SHA-256 방식으로 암호화한다. 비밀번호 암호화를 위해 사용.
        // 3. 해당 지원 DB에 저장
        //  - 위 과정을 거친 뒤 DB에 insert한다.
        //  - DB 데이터 처리 과정에서 예외가 발생하면 예외 처리한다. (msg: "서버 오류 : 회원 수정 실패")

        // 작성자 : 김태훈
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        String profilePath;
        try {
            profilePath = MyBase64Decoder.saveImage(companyUpdateReqDto.getProfile());
        } catch (Exception e) {
            throw new CustomApiException("서버 오류 : 파일 변환 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String salt = Hash.makeSalt();
        String hashPassword = Hash.encode(companyUpdateReqDto.getPassword() + salt);
        User user = new User(companyUpdateReqDto, principalId, profilePath, hashPassword, salt);
        Company company = new Company(companyUpdateReqDto, principalId);
        try {
            userRepository.updateById(user);
            companyRepository.updateByUserId(company);
        } catch (Exception e) {
            throw new CustomApiException("서버 오류 : 회원 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
