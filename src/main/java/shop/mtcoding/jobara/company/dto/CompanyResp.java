package shop.mtcoding.jobara.company.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.jobara.company.model.Company;
import shop.mtcoding.jobara.user.model.User;

public class CompanyResp {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    @NoArgsConstructor
    public static class CompanyInfo {
        private Integer id;
        private String username;
        private String email;
        private String address;
        private String detailAddress;
        private String tel;
        private CompanyDto company;

        @Getter
        @Setter(AccessLevel.PROTECTED)
        public static class CompanyDto {
            private String companyName;
            private String companyScale;
            private String companyField;

            public CompanyDto(Company company) {
                this.companyName = company.getCompanyName();
                this.companyScale = company.getCompanyScale();
                this.companyField = company.getCompanyField();
            }
        }

        public CompanyInfo(User user, Company company) {
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.address = user.getAddress();
            this.detailAddress = user.getDetailAddress();
            this.tel = user.getTel();
            this.company = new CompanyDto(company);
        }
    }
}
