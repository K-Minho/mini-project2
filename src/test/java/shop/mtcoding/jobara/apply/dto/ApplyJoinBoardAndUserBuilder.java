package shop.mtcoding.jobara.apply.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndUser;

public class ApplyJoinBoardAndUserBuilder extends ApplyJoinBoardAndUser {

    public static ApplyJoinBoardAndUser makeApplyJoinBoardAndUser(Integer id, String state, UserDto user,
            BoardDto board, ResumeDto resume) {
        ApplyJoinBoardAndUser mockApplyJoinBoardAndUser = new ApplyJoinBoardAndUser();
        mockApplyJoinBoardAndUser.setId(id);
        mockApplyJoinBoardAndUser.setState(state);
        mockApplyJoinBoardAndUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        mockApplyJoinBoardAndUser.setUser(user);
        mockApplyJoinBoardAndUser.setBoard(board);
        mockApplyJoinBoardAndUser.setResume(resume);
        return mockApplyJoinBoardAndUser;
    }

    public static UserDto makeUser(Integer id, String realName) {
        UserDto mockUser = new UserDto();
        mockUser.setId(id);
        mockUser.setRealName(realName);
        return mockUser;
    }

    public static BoardDto makeBoard(Integer id, String title) {
        BoardDto mockBoard = new BoardDto();
        mockBoard.setId(id);
        mockBoard.setTitle(title);
        return mockBoard;
    }

    public static ResumeDto makeResume(Integer id) {
        ResumeDto mockResume = new ResumeDto();
        mockResume.setId(id);
        return mockResume;
    }
}
