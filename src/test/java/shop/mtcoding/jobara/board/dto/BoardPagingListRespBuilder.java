package shop.mtcoding.jobara.board.dto;

import java.util.List;

import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertReqDto;

public class BoardPagingListRespBuilder extends BoardInsertReqDto {

    public static BoardInsertReqDto makeBoardInsertReqForTest(String title, String content,
            String careerString, String educationString,
            String jobTypeString, String deadline, String favor, Integer userId, List<Integer> checkedValues) {

        BoardInsertReqDto mockBoardInsertReqDto = new BoardInsertReqDto();

        mockBoardInsertReqDto.setTitle(title);
        mockBoardInsertReqDto.setContent(content);
        mockBoardInsertReqDto.setCareerString(careerString);
        mockBoardInsertReqDto.setEducationString(educationString);
        mockBoardInsertReqDto.setJobTypeString(jobTypeString);
        mockBoardInsertReqDto.setDeadline(deadline);
        mockBoardInsertReqDto.setFavor(favor);
        mockBoardInsertReqDto.setUserId(userId);
        mockBoardInsertReqDto.setCheckLang(checkedValues);

        return mockBoardInsertReqDto;
    }

}
