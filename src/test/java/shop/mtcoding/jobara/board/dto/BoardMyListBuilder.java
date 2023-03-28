package shop.mtcoding.jobara.board.dto;

import java.util.List;

public class BoardMyListBuilder extends BoardMyListRespDto {

    public static BoardMyListRespDto makeMyBoardRespForTest(Integer id, String title, String dday, CompanyDto company, UserDto user) {

        BoardMyListRespDto mockBoardMyListRespDto = new BoardMyListRespDto();

        mockBoardMyListRespDto.setId(id);
        mockBoardMyListRespDto.setTitle(title);
        mockBoardMyListRespDto.setDday(dday);
        mockBoardMyListRespDto.setCompany(company);
        mockBoardMyListRespDto.setUser(user);

        return mockBoardMyListRespDto;
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



}
