package shop.mtcoding.jobara.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardMyListRespDto {
    private Integer id;
    private String title;
    private String dday;
    private CompanyDto company;
    private UserDto user;

    @Getter
    @Setter
    public static class CompanyDto {
        private Integer userId;
        private String companyName;
    }

    @Getter
    @Setter
    public static class UserDto {
        private String profile;
    }

}
