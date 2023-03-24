package shop.mtcoding.jobara.company.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class CompanyReq {

    @Getter
    @Setter
    public static class CompanyJoinReqDto {
        @NotEmpty(message = "유저네임 입력하세요")
        @Size(min = 2, max = 16)
        private String username;
        @NotEmpty(message = "암호를 입력하세요")
        @Size(min = 2, max = 32)
        private String password;
        @NotEmpty(message = "이메일을 입력하세요")
        @Size(min = 2, max = 32)
        private String email;
        @NotEmpty(message = "주소를 입력하세요")
        @Size(min = 2, max = 32)
        private String address;
        @NotEmpty(message = "상세 주소를 입력하세요")
        @Size(min = 2, max = 64)
        private String detailAddress;
        @NotEmpty(message = "사업자 등록 번호를 입력하세요")
        @Size(min = 2, max = 16)
        private Long companyNumb;
        @NotEmpty(message = "회사 이름을 입력하세요")
        @Size(min = 1, max = 16)
        private String companyName;
    }

    @Getter
    @Setter
    public static class CompanyUpdateReqDto {
        @NotEmpty(message = "암호를 입력하세요")
        @Size(min = 2, max = 32)
        private String password;
        @NotEmpty(message = "이메일을 입력하세요")
        @Size(min = 2, max = 32)
        private String email;
        @NotEmpty(message = "주소를 입력하세요")
        @Size(min = 2, max = 32)
        private String address;
        @NotEmpty(message = "상세 주소를 입력하세요")
        @Size(min = 2, max = 64)
        private String detailAddress;
        @NotEmpty(message = "전화 번호를 입력하세요")
        @Size(min = 2, max = 64)
        private String tel;
        @NotEmpty(message = "사진을 등록하세요")
        private String profile;
        @NotEmpty(message = "회사 이름을 입력하세요")
        @Size(min = 1, max = 16)
        private String companyName;
        @NotEmpty(message = "회사 규모를 입력하세요")
        @Size(min = 1, max = 8)
        private String companyScale; 
        @NotEmpty(message = "회사 업종을 입력하세요")
        @Size(min = 1, max = 16)
        private String companyField; 
    }
}
