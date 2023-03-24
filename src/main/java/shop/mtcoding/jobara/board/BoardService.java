package shop.mtcoding.jobara.board;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.jobara.board.dto.BoardDetailRespDto;
import shop.mtcoding.jobara.board.dto.BoardMyListRespDto;
import shop.mtcoding.jobara.board.dto.BoardMyScrapListRespDto;
import shop.mtcoding.jobara.board.dto.BoardPagingListDto;
import shop.mtcoding.jobara.board.dto.BoardPagingListDto.BoardListDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertReqDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertSkillReqDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.jobara.board.dto.BoardResp.BoardListRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.BoardUpdateRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.MyBoardListRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.MyScrapBoardListRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.PagingDto;
import shop.mtcoding.jobara.board.dto.BoardUpdateFormRespDto;
import shop.mtcoding.jobara.board.model.Board;
import shop.mtcoding.jobara.board.model.BoardRepository;
import shop.mtcoding.jobara.board.model.BoardTechRepository;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.common.ex.CustomException;
import shop.mtcoding.jobara.common.util.CareerParse;
import shop.mtcoding.jobara.common.util.EducationParse;
import shop.mtcoding.jobara.common.util.JobTypeParse;
import shop.mtcoding.jobara.resume.model.Resume;
import shop.mtcoding.jobara.resume.model.ResumeRepository;
import shop.mtcoding.jobara.user.vo.UserVo;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardTechRepository boardTechRepository;

    @Autowired
    ResumeRepository resumeRepository;

    @Transactional(readOnly = true)
    public BoardDetailRespDto getDetail(Integer principalId, Integer boardId) {
        // @GetMapping("/boards/{id}")에 의해 호출됨.
        // 기능 : 게시글 상세보기 View에 필요한 데이터를 가져와 Controller에 전달
        // 사용되는 요소 : skill 파싱, 좋아요(스크랩) 파싱, carrer/jobType/education 파싱
        // 진행 과정 :
        // 1. DB에서 필요 데이터 가져오기
        //  - findBoardDetailByJoin 메서드를 통해 요청 데이터(게시글 상세보기 View)를 가져온다.
        //  - pricipalId은 love 테이블, resume 테이블 join에 사용된다.
        //    (게시글 상세보기 내의 특정 유저 좋아요 활성화 상태, 이력서 가져오는데 활용)
        //  - boardId는 요청 게시물의 board 테이블의 where 절에 거는데 사용된다.
        // 2. 파싱과정
        //  - skillParse : DB에 들어가있는 skill 정보를 STRING_AGG query 문법을 사용해
        //                 한 속성에 1,2,3 문자열로 가져온다. 이 문자열을 List<Integer> skill에 담기위한 파싱
        //  - faSoild : 좋아요한 적이 있다면 LoveDto의 id에 0, css 변수에 "" 빈문자열,
        //                          없다면 LoveDto의 id에 해당 id값, css 변수에 "fa-solid"을 저장
        //                          (View에서는 태그에 .id, .css를 활용해 뿌리기만 할 수 있음)
        //  - parseIntegerInfo : career, jobType, education은 DB에 숫자로 저장되며, View에는 문자열로 뿌려진다.
        //                       View와 DB에서의 표현 방식이 다른것을 파싱한다.
        //                       (이렇게 다르게 저장한 이유는 설계당시 query의 조건절에 걸 때의 용이성과 DB의 부하를 조금이나마 신경쓰자는 취지였음)

        // 작성자 : 이상현
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        BoardDetailRespDto boardDetailPS;

        try {
            boardDetailPS = boardRepository.findBoardDetailByJoin(principalId, boardId);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        boardDetailPS.skillParse(boardDetailPS.getNeedParse());
        boardDetailPS.faSoild();
        boardDetailPS.parseIntegerInfo();

        return boardDetailPS;
    }

    @Transactional(readOnly = true)
    public BoardPagingListDto getListWithJoin(Integer page, String keyword, UserVo uservo) {
        // @GetMapping("/boards")에 의해 호출됨.
        // 기능 : 요청 페이지 또는 특정 키워드에 필요한 게시글 목록 데이터 Controller에 전달,
        //       각 상황에 따른 페이지네이션(페이지 버튼 구현)에 필요한 데이터를 가져와 Controller에 전달
        // 사용되는 요소 : findAllWithJoin() - 페이지에 필요한 목록, pagingWithJoin() - 페이지에 필요한 페이지정보
        // 진행 과정 :
        // 1. page == null 조건 : 타 페이지에서 게시글 목록페이지를 요청시 null값이 들어옴. 해당 값을 1페이지로 지정
        //    (view에서의 1페이지는 계산을 용이하게 하기 위해 0으로 계산, 2~10페이지는 1~9로 표현)
        // 2. if문 조건 - employee role을 가진 유저가 요청 시 좋아요 아이콘 활성화에 필요한 id값 사용
        //              (findAllWithJoin() pagingWithJoin() query 내에서 keyword에 대한 if문 분기 포함)
        // 3. makeBlockInfo() - 페이지 구현에 필요한 값을 일부는 DB에서 가져오고, 일부는 해당 메서드를 통해 계산하여 필드에 저장
        // 4. setBoard() - 게시글 목록과 페이지 정보를 하나의 dto에 담아서 View에 전달

        // 작성자 : 이상현
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        if (page == null) {
            page = 0;
        }

        BoardPagingListDto pagingDtoPS;
        List<BoardListDto> boardsListPS;
        int startNum = page * BoardPagingListDto.ROW;

        if (uservo != null && uservo.getRole().equals("employee")) {
            try {
                boardsListPS = boardRepository.findAllWithJoin(startNum, keyword, BoardPagingListDto.ROW,
                        uservo.getId());
                pagingDtoPS = boardRepository.pagingWithJoin(page, keyword, BoardPagingListDto.ROW, uservo.getId());
            } catch (Exception e) {
                throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            try {
                boardsListPS = boardRepository.findAllWithJoin(startNum, keyword, BoardPagingListDto.ROW, 0);
                pagingDtoPS = boardRepository.pagingWithJoin(page, keyword, BoardPagingListDto.ROW, 0);
            } catch (Exception e) {
                throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        pagingDtoPS.makeBlockInfo(keyword);
        pagingDtoPS.setBoard(boardsListPS);

        return pagingDtoPS;
    }

    @Transactional(readOnly = true)
    public BoardUpdateFormRespDto getUpdateFormInfo(Integer boardId) {
        BoardUpdateFormRespDto boardUpdateFormRespPS;
        try {
            boardUpdateFormRespPS = boardRepository.findUpdateFormInfo(boardId);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        boardUpdateFormRespPS.skillParse(boardUpdateFormRespPS.getNeedParse());

        boardUpdateFormRespPS.parseIntegerInfo();

        return boardUpdateFormRespPS;
    }

    @Transactional(readOnly = true)
    public List<BoardMyListRespDto> getMyBoardList(int coPrincipalId, int userId) {
        // 권한 체크
        if (coPrincipalId != userId) {
        throw new CustomException("공고 리스트 열람 권한이 없습니다.");
        }

        List<BoardMyListRespDto> myBoardListPS;
        try {
            myBoardListPS = boardRepository.findByIdForMyList(coPrincipalId);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return myBoardListPS;
    }

    @Transactional(readOnly = true)
    public List<BoardMyScrapListRespDto> getMyScrapBoardList(int coPrincipalId, int userId) {
        // 권한 체크
        // if (coPrincipalId != userId) {
        // throw new CustomException("공고 리스트 열람 권한이 없습니다.");
        // }

        List<BoardMyScrapListRespDto> myScrapBoardListPS;
        try {
            myScrapBoardListPS = boardRepository.findAllScrapBoardList(coPrincipalId);
        } catch (Exception e) {
            throw new CustomException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return myScrapBoardListPS;
    }

    @Transactional
    public void deleteMyBoard(int boardId, int principalId) {
        Board boardPS = boardRepository.findById(boardId);
        if (boardPS == null) {
        throw new CustomApiException("삭제할 게시물이 존재하지 않습니다");
        }

        if (boardPS.getUserId() != principalId) {
        throw new CustomApiException("게시글 삭제 권한이 없습니다");
        }

        try {
            boardRepository.deleteById(boardId);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 1/2차 경계선 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public PagingDto getListWithPaging(Integer page, String keyword, UserVo uservo) {

        if (page == null) {
            page = 0;
        }

        List<BoardListRespDto> boardsList;
        PagingDto pagingDto;
        int startNum = page * PagingDto.ROW;

        if (uservo != null && uservo.getRole().equals("employee")) {

            boardsList = boardRepository.findAllWithCompany(startNum, keyword, PagingDto.ROW, uservo.getId());
            pagingDto = boardRepository.paging(page, keyword, PagingDto.ROW, uservo.getId());

        } else {

            boardsList = boardRepository.findAllWithCompany(startNum, keyword, PagingDto.ROW, 0);
            pagingDto = boardRepository.paging(page, keyword, PagingDto.ROW, 0);
        }

        // if (boardsList.size() == 0)
        // pagingDto.setNotResult(true);
        pagingDto.makeBlockInfo(keyword);
        pagingDto.setBoardListDtos(boardsList);

        return pagingDto;
    }

    @Transactional(readOnly = true)
    public BoardUpdateRespDto getDetailForUpdate(int id, int coPrincipalId) {
        BoardUpdateRespDto boardDetailPS = boardRepository.findByIdForUpdate(id);

        if (boardDetailPS == null) {
            throw new CustomException("없는 게시물을 수정할 수 없습니다");
        }

        if (boardDetailPS.getUserId() != coPrincipalId) {
            throw new CustomException("수정 권한이 없습니다");
        }

        String career = CareerParse.careerToString(boardDetailPS.getCareer());
        boardDetailPS.setCareerString(career);

        String education = EducationParse.educationToString(boardDetailPS.getEducation());
        boardDetailPS.setEducationString(education);

        String jobType = JobTypeParse.jopTypeToString(boardDetailPS.getJobType());
        boardDetailPS.setJobTypeString(jobType);

        return boardDetailPS;
    }

    @Transactional
    public void updateBoard(BoardUpdateReqDto boardUpdateReqDto, int coPrincipalId) {

        Board boardPS;
        boardPS = boardRepository.findById(boardUpdateReqDto.getId());

        try {
        } catch (Exception e) {
            throw new CustomApiException("없는 게시물을 수정할 수 없습니다");
        }

        if (boardPS.getUserId() != coPrincipalId) {
            throw new CustomApiException("수정 권한이 없습니다");
        }

        int career = CareerParse.careerToInt(boardUpdateReqDto.getCareerString());
        int education = EducationParse.educationToInt(boardUpdateReqDto.getEducationString());
        int jobType = JobTypeParse.jobTypeToInt(boardUpdateReqDto.getJobTypeString());

        Board board = new Board(boardUpdateReqDto.getId(),
                boardUpdateReqDto.getUserId(),
                boardUpdateReqDto.getTitle(),
                boardUpdateReqDto.getContent(),
                career,
                jobType,
                education,
                boardUpdateReqDto.getFavor(),
                boardUpdateReqDto.getDeadline());

        try {
            boardRepository.updateById(board);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public int insertBoard(BoardInsertReqDto boardInsertReqDto, int userId) {

        int career = CareerParse.careerToInt(boardInsertReqDto.getCareerString());
        int education = EducationParse.educationToInt(boardInsertReqDto.getEducationString());
        int jobType = JobTypeParse.jobTypeToInt(boardInsertReqDto.getJobTypeString());

        Board board = new Board(userId, boardInsertReqDto.getTitle(),
                boardInsertReqDto.getContent(),
                career,
                jobType,
                education,
                boardInsertReqDto.getFavor(),
                boardInsertReqDto.getDeadline());

        try {
            boardRepository.insert(board);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return board.getId();
    }

    @Transactional(readOnly = true)
    public List<MyBoardListRespDto> getMyBoard(int coPrincipalId, int userId) {
        // 권한 체크
        if (coPrincipalId != userId) {
            throw new CustomException("공고 리스트 열람 권한이 없습니다.");
        }

        List<MyBoardListRespDto> myBoardListPS;
        try {
            myBoardListPS = boardRepository.findAllByIdWithCompany(coPrincipalId);
        } catch (Exception e) {
            throw new CustomException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return myBoardListPS;
    }

    @Transactional(readOnly = true)
    public List<MyScrapBoardListRespDto> getMyScrapBoard(int coPrincipalId, int userId) {
        // 권한 체크
        if (coPrincipalId != userId) {
            throw new CustomException("공고 리스트 열람 권한이 없습니다.");
        }

        List<MyScrapBoardListRespDto> myScrapBoardListPS;
        try {
            myScrapBoardListPS = boardRepository.findAllScrapByIdWithCompany(coPrincipalId);
        } catch (Exception e) {
            throw new CustomException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return myScrapBoardListPS;
    }

    @Transactional
    public void insertSkill(List<Integer> checkLang, int boardId) {

        BoardInsertSkillReqDto boardInsertSkillReqDto = new BoardInsertSkillReqDto(boardId, checkLang);

        try {
            boardTechRepository.insertSkill(boardInsertSkillReqDto);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public ArrayList<Integer> getSkillForDetail(int boardId) {
        ArrayList<Integer> checkLang;

        try {
            checkLang = boardTechRepository.findByIdWithSkillForDetail(boardId);
        } catch (Exception e) {
            throw new CustomException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return checkLang;
    }

    @Transactional
    public void updateTech(List<Integer> techList, int boardId) {
        try {
            boardTechRepository.deleteByBoardId(boardId);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BoardInsertSkillReqDto boardInsertSkillReqDto = new BoardInsertSkillReqDto(boardId, techList);
        try {
            boardTechRepository.insertSkill(boardInsertSkillReqDto);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public List<BoardListRespDto> getLangMatchList(int userId) {
        return boardRepository.findAllByUserIdForLangMatching(userId);
    }

    @Transactional(readOnly = true)
    public List<Resume> getResume(int principalId) {
        return resumeRepository.findByUserId(principalId);
    }

    @Transactional
    public void deleteBoard(int boardId, int principalId) {
        Board boardPS = boardRepository.findById(boardId);
        // if (boardPS == null) {
        // throw new CustomApiException("삭제할 게시물이 존재하지 않습니다");
        // }

        // if (boardPS.getUserId() != principalId) {
        // throw new CustomApiException("게시글 삭제 권한이 없습니다");
        // }

        try {
            boardRepository.deleteById(boardId);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
