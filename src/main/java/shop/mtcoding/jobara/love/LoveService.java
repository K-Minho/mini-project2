package shop.mtcoding.jobara.love;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.jobara.board.model.Board;
import shop.mtcoding.jobara.board.model.BoardRepository;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.love.dto.LoveResp.LoveDetailRespDto;
import shop.mtcoding.jobara.love.model.Love;
import shop.mtcoding.jobara.love.model.LoveRepository;
import shop.mtcoding.jobara.user.vo.UserVo;

@Service
public class LoveService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    LoveRepository loveRepository;

    @Transactional
    public int insertLove(int boardId, int userId) {
        // @PostMapping("/loves")에 의해 호출됨.
        // 기능 : 좋아요 요청에 따라 boardId와 userId를 활용하여 DB에 저장
        // 진행 과정 :
        // 1. 좋아요 내역이 존재하는지 확인 (PostMan을 이용한 악용 방지)
        // 2. Controller의 pathVariable로 넘어온 boardId가 존재하는지 게시물 PK값인지 체크 (PostMan을 이용한 악용 방지)
        // 3. DB에 love 정보 저장

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        Love lovePS = loveRepository.findByBoardIdAndUserId(boardId, userId);
        if (lovePS != null) {
            throw new CustomApiException("좋아요 내역이 존재합니다.");
        }

        // 게시글 존재 여부 체크
        Board boardPS = boardRepository.findById(boardId);
        if (boardPS == null) {
            throw new CustomApiException("게시글이 존재하지 않습니다");
        }

        Love love = new Love();
        love.setBoardId(boardId);
        love.setUserId(userId);
        loveRepository.insert(love);
        return love.getId();
    }

    // @Transactional(readOnly = true)
    // public LoveDetailRespDto getLove(int boardId, UserVo principal) {

    //     Love lovePS = loveRepository.findByBoardIdAndUserId(boardId, principal.getId());

    //     if (lovePS == null) {
    //         return new LoveDetailRespDto(0, "");
    //     } else {
    //         return new LoveDetailRespDto(lovePS.getId(), "fa-solid");
    //     }

    // }

    @Transactional
    public void deleteLove(int id, int userId) {
        // @DeleteMapping("/loves/{id}")에 의해 호출됨.
        // 기능 : 좋아요 취소 요청에 따라 loveId와 userId를 활용하여 DB에서 삭제
        // 진행 과정 :
        // 1. 좋아요 내역이 존재하는지 확인 (PostMan을 이용한 악용 방지)
        // 2. 좋아요 내역에 있는 userId와 로그인한 userId를 비교하여 권한 체크
        // 3. DB에서 love 정보 삭제

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        // 좋아요 존재 여부 체크
        Love lovePS = loveRepository.findById(id);
        if (lovePS == null) {
            throw new CustomApiException("좋아요 내역이 존재하지 않습니다");
        }
        // 권한 체크
        if (lovePS.getUserId() != userId) {
            throw new CustomApiException("좋아요 취소 권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        int res = loveRepository.deleteById(id);
        if (res != 1) {
            throw new CustomApiException("서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
