package shop.mtcoding.jobara.board.dto;

import java.util.List;

import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;

public class BoardUpdateReqBuilder extends BoardUpdateReqDto {

    public static BoardUpdateReqDto makeBoardUpdateReqForTest(Integer id, String title, String content,
            String careerString, String educationString,
            String jobTypeString, String deadline, String favor, Integer userId, List<Integer> checkedValues) {

        BoardUpdateReqDto mockBoardUpdateReqDto = new BoardUpdateReqDto();

        mockBoardUpdateReqDto.setId(id);
        mockBoardUpdateReqDto.setTitle(title);
        mockBoardUpdateReqDto.setCareerString(careerString);
        mockBoardUpdateReqDto.setEducationString(educationString);
        mockBoardUpdateReqDto.setJobTypeString(jobTypeString);
        mockBoardUpdateReqDto.setDeadline(deadline);
        mockBoardUpdateReqDto.setFavor(favor);
        mockBoardUpdateReqDto.setUserId(userId);
        mockBoardUpdateReqDto.setCheckedValues(checkedValues);

        return mockBoardUpdateReqDto;
    }

}
