package shop.mtcoding.jobara.board;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.board.dto.BoardDetailRespDto;
import shop.mtcoding.jobara.board.dto.BoardMyListRespDto;
import shop.mtcoding.jobara.board.dto.BoardMyScrapListRespDto;
import shop.mtcoding.jobara.board.dto.BoardPagingListDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardInsertReqDto;
import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.jobara.board.dto.BoardUpdateFormRespDto;
import shop.mtcoding.jobara.common.aop.CompanyCheckApi;
import shop.mtcoding.jobara.common.aop.EmployeeCheckApi;
import shop.mtcoding.jobara.common.config.auth.LoginUser;
import shop.mtcoding.jobara.common.dto.ResponseDto;
import shop.mtcoding.jobara.common.ex.CustomApiException;
import shop.mtcoding.jobara.common.util.DateParse;
import shop.mtcoding.jobara.common.util.Token;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    private final HttpSession session;

    @GetMapping({ "/", "/home" })
    public ResponseEntity<?> home(HttpServletRequest request) {
        // 1. 기능 : 메인페이지 요청 메서드
        // ※ Cookie - 메인페이지 하단의 로그인 component에서 아이디 기억하기에 활용

        // 작성자 : 이상x
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
        return new ResponseEntity<>(new ResponseDto<>(1, "메인 페이지 출력 성공", null), HttpStatus.OK);
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<?> detail(@PathVariable int id, ServletRequest request, ServletResponse response) {
        // 1. 기능 : 구인공고 목록에서 특정 구인공고 클릭시 해당 페이지를 요청하는 메서드
        //          (전체 공고리스트, 등록한 공고, 스크랩한 공고 각 페이지에서 요청 가능)
        // 2. Arguments :
        // - PathVariable : id, 해당 구인공고의 id이며, PK이다.

        // 3. Return :
        // - BoardDetailRespDto
        // (id, title, content, career, jobType, education, favor, List<Integer> skill,
        //  Company(userId, companyName, comapnyScale, companyField),
        //  user(id, profile),
        //  resume(id, userId, title, content, createdAt),
        //  love(id, boardId, userId, css)

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        // 특이사항 : 해당 핸들러는 인증이 필요 없지만, employee 로그인 유저에겐 love, resume을 보여 줘야 함
        //           Filter에서는 걸리지 않기 때문에 session에 값이 저장되어 있지 않은 상태임
        //           로그인 유저의 id가 필요한 상황이라 인터셉터나 AOP처리를 하려고 하였으나 기타 문제로 인해 추후에 처리할 예정
        //           (진행 코드는 Controller에서 token을 확인하여 id값을 가져옴)
        LoginUser principal = null;
        principal = Token.loginCheck(principal, request, response);

        BoardDetailRespDto boardDetailRespDto = boardService.getDetail(id, principal);

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 상세정보 불러오기 성공", boardDetailRespDto), HttpStatus.OK);
    }

    @GetMapping("/boards")
    public ResponseEntity<?> list(Integer page, String keyword, ServletRequest request, ServletResponse response) {
        // 1. 기능 : 구인공고 목록페이지를 요청하는 페이지
        // 2. Arguments :
        // - Page : keyword 또는 기본 정렬에 따른 Page 요청 값이다.
        //          타 페이지에서의 진입시 null 값이 들어올 수 있으며, 해당 경우 Service에서 1페이지 처리를 한다.
        // - keyword : 구인공고 목록페이지 우상단에 있는 selectBox 내의 요청 값이다.
        //             null, lang(매칭공고), deadline(마감일순) 값이 들어올 수 있다.
        //             Service와 Query에서의 if문으로 위 3값 Check

        // 3. Return :
        // - BoardPagingListDto
        // (keyword, blockCount, currentBlock, currentPage, startPageNum, lastPageNum,
        //  totalCount, totalPage, isLast, isFirst,
        //  List<Board>(id, title, companyName, dday, user(id, profile), love(id, css))

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        
        // 특이사항 : 해당 핸들러는 인증이 필요 없지만, employee 로그인 유저에겐 love, resume을 보여 줘야 함
        //           Filter에서는 걸리지 않기 때문에 session에 값이 저장되어 있지 않은 상태임
        //           로그인 유저의 id가 필요한 상황이라 인터셉터나 AOP처리를 하려고 하였으나 기타 문제로 인해 추후에 처리할 예정
        //           (진행 코드는 Controller에서 token을 확인하여 id값을 가져옴)
        LoginUser principal = null;
        principal = Token.loginCheck(principal, request, response);

        BoardPagingListDto boardPagingDto = boardService.getListWithJoin(page, keyword, principal);

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 리스트 불러오기 성공", boardPagingDto), HttpStatus.OK);
    }

    @GetMapping("/company/boards/saveForm")
    @CompanyCheckApi
    public ResponseEntity<?> saveForm() {
        // 1. 기능 : 구인공고 등록페이지를 요청하는 페이지
        // 2. Arguments :
        // 3. Return :

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -
        return new ResponseEntity<>(new ResponseDto<>(1, "공고 등록페이지 출력 성공", null), HttpStatus.OK);
    }

    @GetMapping("/company/boards/updateForm/{id}")
    @CompanyCheckApi
    public ResponseEntity<?> updateForm(@PathVariable int id) {
        // 1. 기능 : 구인공고 수정페이지를 요청하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 해당 구인공고의 id이며, PK이다.

        // 3. Return :
        // - BoardUpdateFormRespDto
        // (id, title, content, career, education, jobType,
        // favor, deadline, userId, List<Integer> skill)

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        LoginUser principal = (LoginUser) session.getAttribute("loginUser");
        BoardUpdateFormRespDto boardUpdateFormRespDto = boardService.getUpdateFormInfo(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 수정페이지 출력 성공", boardUpdateFormRespDto), HttpStatus.OK);
    }

    @PutMapping("/company/boards/{id}")
    @CompanyCheckApi
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid BoardUpdateReqDto boardUpdateReqDto,
            BindingResult bindingResult) {
        // 1. 기능 : 구인공고 수정을 요청하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 해당 구인공고의 id이며, PK이다.
        // - BoardUpdateReqDto
        //  (id, title, content, careerString, educationString, jobTypeString, deadline,
        //  favor, userId, List<Integer> checkedValues)
        //   title : 최소 1 최대 16, null&empty
        //   content : 최소 1 최대 65536, null&empty
        //   careerString : selectBox에서 선택, null&empty
        //   educationString : selectBox에서 선택, null&empty
        //   jobTypeString : selectBox에서 선택, null&empty
        //   deadline : 한 가지 이상 선택해야함, null&empty
        //              아래 DateParse.Dday 메서드를 통해 마감날짜에 대한 최대 일 수를 100일로 제한
        //   favor : 최소 1 최대 16, null&empty
        //   checkedValues : 한 가지 이상 선택해야함, null&empty

        // 3. Return :

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -
        LoginUser principal = (LoginUser) session.getAttribute("loginUser");

        ArrayList<Object> resDateParse = DateParse.Dday(boardUpdateReqDto.getDeadline());
        if (!(0 < (Integer) resDateParse.get(0) && (Integer) resDateParse.get(0) < 100)) {
            throw new CustomApiException("1일~100일 내의 마감날짜를 선택 해주세요. (~" + (String) resDateParse.get(1) + ")");
        }

        boardService.updateBoard(boardUpdateReqDto, principal.getId());
        boardService.updateTech(boardUpdateReqDto.getCheckedValues(), id);

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 수정성공", null), HttpStatus.CREATED);
    }

    @PostMapping("/company/boards")
    @CompanyCheckApi
    public ResponseEntity<?> save(@RequestBody @Valid BoardInsertReqDto boardInsertReqDto,
            BindingResult bindingResult) {
        // 1. 기능 : 구인공고 등록을 요청하는 메서드
        // 2. Arguments :
        // - BoardInsertReqDto
        //  (title, content, careerString, educationString, jobTypeString, deadline,
        //  favor, userId, List<Integer> checkLang)
        //   title : 최소 1 최대 16, null&empty
        //   content : 최소 1 최대 65536, null&empty
        //   careerString : selectBox에서 선택, null&empty
        //   educationString : selectBox에서 선택, null&empty
        //   jobTypeString : selectBox에서 선택, null&empty
        //   deadline : 한 가지 이상 선택해야함, null&empty
        //              아래 DateParse.Dday 메서드를 통해 마감날짜에 대한 최대 일 수를 100일로 제한
        //   favor : 최소 1 최대 16, null&empty
        //   checkLang : 한 가지 이상 선택해야함, null&empty

        // 3. Return :

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -
        LoginUser principal = (LoginUser) session.getAttribute("loginUser");

        ArrayList<Object> resDateParse = DateParse.Dday(boardInsertReqDto.getDeadline());
        if (!(0 < (Integer) resDateParse.get(0) && (Integer) resDateParse.get(0) < 100)) {
            throw new CustomApiException("1일~100일 내의 마감날짜를 선택 해주세요. (~" + (String) resDateParse.get(1) + ")");
        }

        int boardId = boardService.insertBoard(boardInsertReqDto, principal.getId());
        boardService.insertSkill(boardInsertReqDto.getCheckLang(), boardId);

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 등록성공", null), HttpStatus.CREATED);
    }

    @GetMapping("/company/boards/myList/{id}")
    @CompanyCheckApi
    public ResponseEntity<?> myBoardList(@PathVariable int id) {
        // 1. 기능 : 내가 등록한 공고목록을 요청하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 특정 유저의 id값이며, PK이다.
        //                  (로그인 유저의 id값과 열람하려는 id값을 비교하여 권한체크에 활용)

        // 3. Return :
        // - List<BoardMyListRespDto>
        //  (id, title, dday, company, user,
        //   company(companyNmae),
        //   user(id, profile))

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -
        LoginUser principal = (LoginUser) session.getAttribute("loginUser");

        List<BoardMyListRespDto> myBoardListPS = boardService.getMyBoardList(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "등록 공고 리스트 불러오기 성공", myBoardListPS), HttpStatus.OK);
    }

    @GetMapping("/employee/boards/myScrapList/{id}")
    @EmployeeCheckApi
    public ResponseEntity<?> myScrapBoardList(@PathVariable int id) {
        // 1. 기능 : 내가 스크랩한 공고목록을 요청하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 특정 유저의 id값이며, PK이다.
        //                  (로그인 유저의 id값과 열람하려는 id값을 비교하여 권한체크에 활용)

        // 3. Return :
        // - List<BoardMyScrapListRespDto>
        //  (id, title, dday, company, user,
        //   company(companyNmae),
        //   user(id, profile))

        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -

        LoginUser principal = (LoginUser) session.getAttribute("loginUser");
        List<BoardMyScrapListRespDto> myScrapBoardListPS = boardService.getMyScrapBoardList(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "스크랩 공고 목록 불러오기 성공", myScrapBoardListPS), HttpStatus.OK);
    }

    @DeleteMapping("/company/boards/{id}")
    @CompanyCheckApi
    public ResponseEntity<?> delete(@PathVariable int id) {
        LoginUser principal = (LoginUser) session.getAttribute("loginUser");
        // 1. 기능 : 등록된 공고를 삭제 요청하는 메서드
        // 2. Arguments :
        // - PathVariable : id, 삭제요청하는 공고의 id이며, PK이다.
        //                 (삭제할 게시물의 존재유무 체크, deleteById Query에 활용)
        // 3. Return :
        
        // 작성자 : 이상x
        // 작성일 : 2023-03-24
        // 수정자 : -
        // 수정일 : -
        boardService.deleteMyBoard(id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "공고 삭제 성공", null), HttpStatus.OK);
    }

}
