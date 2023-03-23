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
import shop.mtcoding.jobara.common.util.DateParse;
import shop.mtcoding.jobara.common.util.Verify;
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
        UserVo principal = setPrincipal();
        System.out.println(principal.getId());
        BoardDetailRespDto boardDetailRespDto = boardService.getDetail(principal.getId(), id);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 상세페이지", boardDetailRespDto), HttpStatus.OK);
    }

    @GetMapping("/boards")
    public ResponseEntity<?> list(Integer page, String keyword) {
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
        // Verify.validateApiString(boardUpdateReqDto.getDeadline(), "마감 날짜를 선택하세요");

        ArrayList<Object> resDateParse = DateParse.Dday(boardUpdateReqDto.getDeadline());
        if (!(0 < (Integer) resDateParse.get(0) && (Integer) resDateParse.get(0) < 100)) {
            throw new CustomApiException("1일~100일 내의 마감날짜를 선택 해주세요. (~" + (String) resDateParse.get(1) + ")");
        }

        // if (boardUpdateReqDto.getFavor().length() > 16) {
        // throw new CustomApiException("우대사항은 16자 이내 입력 가능합니다");
        // }

        // Verify.isEqualApi(boardUpdateReqDto.getCheckedValues().size(), 0, "선호기술을 한 가지
        // 이상 선택해주세요.",
        // HttpStatus.BAD_REQUEST);

        // Verify.validateApiString(boardUpdateReqDto.getTitle(), "제목을 입력하세요");
        // Verify.validateApiString(boardUpdateReqDto.getContent(), "내용을 입력하세요");
        // Verify.validateApiString(boardUpdateReqDto.getCareerString(), "경력을 입력하세요");

        boardService.updateBoard(boardUpdateReqDto, principal.getId());
        boardService.updateTech(boardUpdateReqDto.getCheckedValues(), id);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정 성공", null), HttpStatus.CREATED);
    }

    @PostMapping("/company/boards")
    // @CompanyCheck
    public ResponseEntity<?> save(@RequestBody BoardInsertReqDto boardInsertReqDto) {

        UserVo principal = setCompanyPrincipal();
        // 유효성
        // Verify.validateString(boardInsertReqDto.getTitle(), "제목을 입력하세요");
        // Verify.validateString(boardInsertReqDto.getContent(), "내용을 입력하세요");
        // if (boardInsertReqDto.getFavor().length() > 16) {
        // throw new CustomException("우대사항은 16자 이내 입력 가능합니다");
        // }

        // Verify.isStringEquals(boardInsertReqDto.getCareerString(), "경력선택", "경력을
        // 선택하세요", HttpStatus.BAD_REQUEST);
        // Verify.isStringEquals(boardInsertReqDto.getCareerString(), "학력선택", "학력을
        // 선택하세요", HttpStatus.BAD_REQUEST);
        // Verify.isStringEquals(boardInsertReqDto.getCareerString(), "근무형태", "근무형태를
        // 선택하세요", HttpStatus.BAD_REQUEST);

        // Verify.validateString(boardInsertReqDto.getDeadline(), "마감 날짜를 선택하세요");

        // ArrayList<Object> resDateParse =
        // DateParse.Dday(boardInsertReqDto.getDeadline());
        // if (!(0 < (Integer) resDateParse.get(0) && (Integer) resDateParse.get(0) <
        // 100)) {
        // throw new CustomException("1일~100일 내의 마감날짜를 선택 해주세요. (~" + (String)
        // resDateParse.get(1) + ")");
        // }

        // Verify.isEqual(checkLang.size(), 0, "선호기술을 한 가지 이상 선택해주세요.",
        // HttpStatus.BAD_REQUEST);

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
