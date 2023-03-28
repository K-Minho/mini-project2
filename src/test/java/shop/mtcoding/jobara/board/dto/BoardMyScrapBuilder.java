package shop.mtcoding.jobara.board.dto;



public class BoardMyScrapBuilder extends BoardMyScrapListRespDto {

    public static BoardMyScrapListRespDto makeMyScrapRespForTest(Integer id, String title, String dday, CompanyDto company, UserDto user, LoveDto love) {

        BoardMyScrapListRespDto mockMyScrapListRespDto = new BoardMyScrapListRespDto();

        mockMyScrapListRespDto.setId(id);
        mockMyScrapListRespDto.setTitle(title);
        mockMyScrapListRespDto.setDday(dday);
        mockMyScrapListRespDto.setCompany(company);
        mockMyScrapListRespDto.setUser(user);
        mockMyScrapListRespDto.setLove(love);

        return mockMyScrapListRespDto;
    }

    public static CompanyDto makeCompanyDtoForTest(String companyName) {

        CompanyDto mockComapanyDto = new CompanyDto();

        mockComapanyDto.setCompanyName(companyName);;
        return mockComapanyDto;
    }

    public static UserDto makeUserDtoForTest(Integer id, String profile) {

        UserDto mockUserDto = new UserDto();

        mockUserDto.setId(id);
        mockUserDto.setProfile(profile);

        return mockUserDto;
    }

    public static LoveDto makeLoveDtoForTest(Integer id, Integer boardId, Integer userId, String css) {

        LoveDto mockLoveDto = new LoveDto();

        mockLoveDto.setId(id);
        mockLoveDto.setBoardId(boardId);
        mockLoveDto.setUserId(userId);
        mockLoveDto.setCss(css);

        return mockLoveDto;
    }



}
