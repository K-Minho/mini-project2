package shop.mtcoding.jobara.board;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import shop.mtcoding.jobara.board.dto.BoardDetailRespDto;
import shop.mtcoding.jobara.board.dto.BoardMyListRespDto;
import shop.mtcoding.jobara.board.dto.BoardMyScrapListRespDto;
import shop.mtcoding.jobara.board.dto.BoardPagingListDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertReqDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.jobara.board.dto.BoardUpdateFormRespDto;
import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.common.ex.CustomException;
import shop.mtcoding.jobara.common.util.DateParse;
import shop.mtcoding.jobara.love.LoveService;
import shop.mtcoding.jobara.user.vo.UserVo;

@RestController
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    LoveService loveService;

    @Autowired
    HttpSession session;

    public UserVo setPrincipal() {
        return new UserVo(1, "ssar", "", "employee");
    }

    public UserVo setCompanyPrincipal() {
        return new UserVo(6, "cos", "", "company");
    }

    @GetMapping({ "/", "/home" })
    public ResponseEntity<?> home(HttpServletRequest request) {
        // 1. 기능 : 메인페이지 요청 메서드
        // ※ Cookie - 메인페이지 하단의 로그인 component에서 아이디 기억하기에 활용
        // 작성자 : 이상현
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        String username = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("remember")) {
                    username = cookie.getValue();
                }
            }
        }
        return new ResponseEntity<>(new ResponseDto<>(1, "메인 페이지", null), HttpStatus.OK);
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<?> detail(@PathVariable int id) {
        // 1. 기능 : 구인공고 목록에서 특정 구인공고 클릭시 해당 페이지를 요청하는 메서드
        //          (전체 공고리스트, 등록한 공고, 스크랩한 공고 각 페이지에서 요청 가능)
        // 2. Arguments :
        // - PathVariable : id, 해당 구인공고의 id이다. PK이며 null이 될 수 없음.
        // 3. Return :
        // - BoardDetailRespDto
        //   (id, title, content, career, jobType, education, favor, skill,
        //    Company(userId, companyName, comapnyScale, companyField),
        //    User(id, profile),
        //    Resume(id, userId, title, content, createdAt)
        // 작성자 : 이상현
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        UserVo principal = setPrincipal();
        System.out.println(principal.getId());
        BoardDetailRespDto boardDetailRespDto = boardService.getDetail(principal.getId(), id);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 상세페이지", boardDetailRespDto), HttpStatus.OK);
    }

    @GetMapping("/boards")
    public ResponseEntity<?> list(Integer page, String keyword) {
        // 1. 기능 : 구인공고 목록페이지를 요청하는 페이지
        // 2. Arguments :
        // - Page : keyword 또는 기본 정렬에 따른 Page 요청 값이다.
        //         타 페이지에서의 진입시 null 값이 들어올 수 있으며, 해당 경우 Service에서 1페이지 처리를 한다.
        // - keyword : 구인공고 목록페이지 우상단에 있는 selectBox 내의 요청 값이다.
        //             null, lang(매칭공고), deadline(마감일순) 값이 들어올 수 있다.
        // 3. Return :
        // - BoardPagingListDto
        //   (keyword, blockCount, currentBlock, currentPage, startPageNum, lastPageNum,
        //    totalCount, totalPage, isLast, isFirst,
        //    List<Board>(id, title, companyName, dday, User(id, profile), Love(id, css))
        // 작성자 : 이상현
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        UserVo principal = setPrincipal();
        BoardPagingListDto boardPagingDto = boardService.getListWithJoin(page, keyword, principal);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 목록페이지", boardPagingDto), HttpStatus.OK);
    }

    @GetMapping("/company/boards/saveForm")
    // @CompanyCheck
    public ResponseEntity<?> saveForm() {
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 등록페이지", null), HttpStatus.OK);
    }

    @GetMapping("/company/boards/updateForm/{id}")
    // @CompanyCheck
    public ResponseEntity<?> updateForm(@PathVariable int id) {
        UserVo principal = setCompanyPrincipal();
        BoardUpdateFormRespDto boardUpdateFormRespDto = boardService.getUpdateFormInfo(id);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정페이지", boardUpdateFormRespDto), HttpStatus.OK);
    }

    @PutMapping("/company/boards/{id}")
    // @CompanyCheckApi
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid BoardUpdateReqDto boardUpdateReqDto,
            BindingResult bindingResult) {
        UserVo principal = setCompanyPrincipal();
        // 유효성
        ArrayList<Object> resDateParse = DateParse.Dday(boardUpdateReqDto.getDeadline());
        if (!(0 < (Integer) resDateParse.get(0) && (Integer) resDateParse.get(0) < 100)) {
            throw new CustomApiException("1일~100일 내의 마감날짜를 선택 해주세요. (~" + (String) resDateParse.get(1) + ")");
        }

        boardService.updateBoard(boardUpdateReqDto, principal.getId());
        boardService.updateTech(boardUpdateReqDto.getCheckedValues(), id);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정 성공", null), HttpStatus.CREATED);
    }

    @PostMapping("/company/boards")
    // @CompanyCheck
    public ResponseEntity<?> save(@RequestBody @Valid BoardInsertReqDto boardInsertReqDto,
            BindingResult bindingResult) {
        UserVo principal = setCompanyPrincipal();
        // 유효성
        ArrayList<Object> resDateParse = DateParse.Dday(boardInsertReqDto.getDeadline());
        if (!(0 < (Integer) resDateParse.get(0) && (Integer) resDateParse.get(0) < 100)) {
            throw new CustomException("1일~100일 내의 마감날짜를 선택 해주세요. (~" + (String) resDateParse.get(1) + ")");
        }

        int boardId = boardService.insertBoard(boardInsertReqDto, principal.getId());
        boardService.insertSkill(boardInsertReqDto.getCheckLang(), boardId);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 등록 성공", null), HttpStatus.CREATED);
    }

    @GetMapping("/company/boards/myList/{id}")
    // @CompanyCheck
    public ResponseEntity<?> myBoardList(@PathVariable int id) {
        UserVo principal = setCompanyPrincipal();

        List<BoardMyListRespDto> myBoardListPS = boardService.getMyBoardList(principal.getId(), id);
        return new ResponseEntity<>(new ResponseDto<>(1, "등록 게시글 목록", myBoardListPS), HttpStatus.OK);
    }

    @GetMapping("/employee/boards/myScrapList/{id}")
    public ResponseEntity<?> myScrapBoardList(@PathVariable int id) {

        UserVo principal = setPrincipal();

        // 인증체크
        // Verify.validateObject(
        // principal, "로그인이 필요한 페이지입니다", HttpStatus.BAD_REQUEST,
        // "/loginForm");

        // Verify.checkRole(principal, "employee");

        List<BoardMyScrapListRespDto> myScrapBoardListPS = boardService.getMyScrapBoardList(principal.getId(), id);
        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 게시글 목록", myScrapBoardListPS), HttpStatus.OK);
    }

    @DeleteMapping("/company/boards/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        UserVo principal = setCompanyPrincipal();
        // Verify.validateApiObject(
        // principal, "로그인이 필요한 페이지입니다", HttpStatus.BAD_REQUEST);
        // Verify.checkRoleApi(principal, "company");

        boardService.deleteMyBoard(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글을 삭제하였습니다", null), HttpStatus.OK);
    }

}
