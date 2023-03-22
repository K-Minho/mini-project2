package shop.mtcoding.jobara.board;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import shop.mtcoding.jobara.board.dto.BoardDetailRespDto;
import shop.mtcoding.jobara.board.dto.BoardPagingListDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertReqDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.jobara.board.dto.BoardResp.MyBoardListRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.MyScrapBoardListRespDto;
import shop.mtcoding.jobara.board.dto.BoardUpdateFormRespDto;
import shop.mtcoding.jobara.common.aop.CompanyCheck;
import shop.mtcoding.jobara.common.aop.CompanyCheckApi;
import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.common.ex.CustomException;
import shop.mtcoding.jobara.common.util.DateParse;
import shop.mtcoding.jobara.common.util.RedisService;
import shop.mtcoding.jobara.common.util.RedisServiceSet;
import shop.mtcoding.jobara.common.util.Verify;
import shop.mtcoding.jobara.love.LoveService;
import shop.mtcoding.jobara.user.vo.UserVo;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    LoveService loveService;

    @Autowired
    HttpSession session;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisServiceSet redisServiceSet;

    public UserVo setPrincipal() {
        return new UserVo(1, "ssar", "", "employee");
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
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<?> detail(@PathVariable int id) {
        UserVo principal = setPrincipal();
        System.out.println(principal.getId());
        BoardDetailRespDto boardDetailRespDto = boardService.getDetail(principal.getId(), id);

        return ResponseEntity.status(200).body(boardDetailRespDto);
    }

    @GetMapping("/boards/list")
    public ResponseEntity<?> list(Integer page, String keyword) {
        UserVo principal = setPrincipal();
        BoardPagingListDto boardPagingDto = boardService.getListWithJoin(page, keyword, principal);

        return ResponseEntity.status(200).body(boardPagingDto);
    }

    @GetMapping("/boards/saveForm")
    // @CompanyCheck
    public ResponseEntity<?> saveForm() {
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/boards/updateForm/{id}")
    // @CompanyCheck
    public ResponseEntity<?> updateForm(@PathVariable int id) {
        UserVo principal = setPrincipal();
        BoardUpdateFormRespDto boardUpdateFormRespDto = boardService.getUpdateFormInfo(id);

        return ResponseEntity.status(200).body(boardUpdateFormRespDto);
    }

    @PutMapping("/board/update/{id}")
    @CompanyCheckApi
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardUpdateReqDto boardUpdateReqDto) {
        UserVo principal = redisService.getValue("principal");

        // 유효성
        Verify.validateApiString(boardUpdateReqDto.getDeadline(), "마감 날짜를 선택하세요");

        ArrayList<Object> resDateParse = DateParse.Dday(boardUpdateReqDto.getDeadline());
        if (!(0 < (Integer) resDateParse.get(0) && (Integer) resDateParse.get(0) < 100)) {
            throw new CustomApiException("1일~100일 내의 마감날짜를 선택 해주세요. (~" + (String) resDateParse.get(1) + ")");
        }

        if (boardUpdateReqDto.getFavor().length() > 16) {
            throw new CustomApiException("우대사항은 16자 이내 입력 가능합니다");
        }

        Verify.isEqualApi(boardUpdateReqDto.getCheckedValues().size(), 0, "선호기술을 한 가지 이상 선택해주세요.",
                HttpStatus.BAD_REQUEST);

        Verify.validateApiString(boardUpdateReqDto.getTitle(), "제목을 입력하세요");
        Verify.validateApiString(boardUpdateReqDto.getContent(), "내용을 입력하세요");
        Verify.validateApiString(boardUpdateReqDto.getCareerString(), "경력을 입력하세요");

        boardService.updateBoard(boardUpdateReqDto, principal.getId());
        boardService.updateTech(boardUpdateReqDto.getCheckedValues(), id);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정완료", null), HttpStatus.OK);
    }

    @PostMapping("/board/save")
    @CompanyCheck
    public String save(BoardInsertReqDto boardInsertReqDto,
            @RequestParam(required = false, defaultValue = "") ArrayList<Integer> checkLang) {

        UserVo principal = redisService.getValue("principal");

        // 유효성
        Verify.validateString(boardInsertReqDto.getTitle(), "제목을 입력하세요");
        Verify.validateString(boardInsertReqDto.getContent(), "내용을 입력하세요");
        if (boardInsertReqDto.getFavor().length() > 16) {
            throw new CustomException("우대사항은 16자 이내 입력 가능합니다");
        }

        Verify.isStringEquals(boardInsertReqDto.getCareerString(), "경력선택", "경력을 선택하세요", HttpStatus.BAD_REQUEST);
        Verify.isStringEquals(boardInsertReqDto.getCareerString(), "학력선택", "학력을 선택하세요", HttpStatus.BAD_REQUEST);
        Verify.isStringEquals(boardInsertReqDto.getCareerString(), "근무형태", "근무형태를 선택하세요", HttpStatus.BAD_REQUEST);

        Verify.validateString(boardInsertReqDto.getDeadline(), "마감 날짜를 선택하세요");

        ArrayList<Object> resDateParse = DateParse.Dday(boardInsertReqDto.getDeadline());
        if (!(0 < (Integer) resDateParse.get(0) && (Integer) resDateParse.get(0) < 100)) {
            throw new CustomException("1일~100일 내의 마감날짜를 선택 해주세요. (~" + (String) resDateParse.get(1) + ")");
        }

        Verify.isEqual(checkLang.size(), 0, "선호기술을 한 가지 이상 선택해주세요.", HttpStatus.BAD_REQUEST);

        int boardId = boardService.insertBoard(boardInsertReqDto, principal.getId());
        boardService.insertSkill(checkLang, boardId);

        return "redirect:/board/boardList/" + principal.getId();
    }

    @GetMapping("/board/boardList/{id}")
    @CompanyCheck
    public String myBoardList(@PathVariable int id, Model model) {

        UserVo principal = redisService.getValue("principal");

        List<MyBoardListRespDto> myBoardListPS = boardService.getMyBoard(principal.getId(), id);
        model.addAttribute("myBoardList", myBoardListPS);
        redisServiceSet.addModel(model);
        return "board/myBoardList";
    }

    @GetMapping("/board/scrapList/{id}")
    public String myScrapBoardList(@PathVariable int id, Model model) {

        UserVo principal = redisService.getValue("principal");

        // 인증체크
        Verify.validateObject(
                principal, "로그인이 필요한 페이지입니다", HttpStatus.BAD_REQUEST,
                "/loginForm");

        Verify.checkRole(principal, "employee");

        List<MyScrapBoardListRespDto> myScrapBoardListPS = boardService.getMyScrapBoard(principal.getId(), id);
        model.addAttribute("myScrapBoardList", myScrapBoardListPS);
        redisServiceSet.addModel(model);
        return "board/myScrapBoardList";
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {

        UserVo principal = redisService.getValue("principal");
        Verify.validateApiObject(
                principal, "로그인이 필요한 페이지입니다", HttpStatus.BAD_REQUEST);
        Verify.checkRoleApi(principal, "company");

        boardService.deleteBoard(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글을 삭제하였습니다", null), HttpStatus.OK);
    }

}
