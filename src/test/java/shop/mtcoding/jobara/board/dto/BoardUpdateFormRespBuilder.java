package shop.mtcoding.jobara.board.dto;

import java.util.List;

public class BoardUpdateFormRespBuilder extends BoardUpdateFormRespDto {

    public static BoardUpdateFormRespDto makeUpdateFormRespForTest(Integer id, String title, String content, String career, String education, String jobType,
    String favor, String deadline, Integer userId ,List<Integer> skill) {

        BoardUpdateFormRespDto mockBoardUpdateFormRespDto = new BoardUpdateFormRespDto();

        mockBoardUpdateFormRespDto.setId(id);
        mockBoardUpdateFormRespDto.setTitle(title);
        mockBoardUpdateFormRespDto.setContent(content);
        mockBoardUpdateFormRespDto.setCareer(career);
        mockBoardUpdateFormRespDto.setEducation(education);
        mockBoardUpdateFormRespDto.setJobType(jobType);
        mockBoardUpdateFormRespDto.setFavor(favor);
        mockBoardUpdateFormRespDto.setUserId(userId);
        mockBoardUpdateFormRespDto.setSkill(skill);;

        return mockBoardUpdateFormRespDto;
    }
}
