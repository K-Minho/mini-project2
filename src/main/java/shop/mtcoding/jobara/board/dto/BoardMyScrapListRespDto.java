package shop.mtcoding.jobara.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardMyScrapListRespDto {
    private Integer id;
    private String title;
    private String dday;

    private CompanyDto company;
    private UserDto user;
    private LoveDto love;

    @Getter
    @Setter
    public static class CompanyDto {
        private String companyName;
    }
    
    @Getter
    @Setter
    public static class UserDto {
        private Integer id;
        private String profile;
    }

    @Getter
    @Setter
    public static class LoveDto {
        private Integer id;
        private Integer boardId;
        private Integer userId;
        private String css;

    }
}
