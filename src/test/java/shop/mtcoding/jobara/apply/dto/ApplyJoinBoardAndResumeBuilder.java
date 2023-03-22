package shop.mtcoding.jobara.apply.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndResume;

public class ApplyJoinBoardAndResumeBuilder extends ApplyJoinBoardAndResume {

    public static ApplyJoinBoardAndResume makeApplyJoinBoardAndResume(Integer id, Integer state, UserDto user,
            BoardDto board, ResumeDto resume) {
        ApplyJoinBoardAndResume mockApplyJoinBoardAndResume = new ApplyJoinBoardAndResume();
        mockApplyJoinBoardAndResume.setId(id);
        mockApplyJoinBoardAndResume.setState(state);
        mockApplyJoinBoardAndResume.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        mockApplyJoinBoardAndResume.setUser(user);
        mockApplyJoinBoardAndResume.setBoard(board);
        mockApplyJoinBoardAndResume.setResume(resume);
        return mockApplyJoinBoardAndResume;
    }

    public static UserDto makeUser(Integer id) {
        UserDto mockUser = new UserDto();
        mockUser.setId(id);
        return mockUser;
    }

    public static BoardDto makeBoard(Integer id, String title) {
        BoardDto mockBoard = new BoardDto();
        mockBoard.setId(id);
        mockBoard.setTitle(title);
        return mockBoard;
    }

    public static ResumeDto makeResume(Integer id, String title) {
        ResumeDto mockResume = new ResumeDto();
        mockResume.setId(id);
        mockResume.setTitle(title);
        return mockResume;
    }
}
