package shop.mtcoding.jobara.board.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPagingListDto {
    public static final int ROW = 8;
    // private boolean isNotResult;
    private String keyword;
    private Integer blockCount; // 상수 한페이지에 페이지 넘수 개수(5) 1-5, 6-10
    private Integer currentBlock; // 변수
    private Integer currentPage;
    private Integer startPageNum; // 변수 1 -> 6 -> 11
    private Integer lastPageNum; // 변수 5 -> 10 -> 15
    private Integer totalCount;
    private Integer totalPage;
    private boolean isLast; // getter가 만들어지면 isLast() 이름으로 만들어짐. -> el에서는 last로 찾음
    private boolean isFirst; // getter가 만들어지면 isFirst() 이름으로 만들어짐. -> el에서는 first로 찾음

    private List<BoardListDto> board;

    @Getter
    @Setter
    public static class BoardListDto {
        private Integer boardId;
        private String title;
        private String companyName;
        private String dday;
        private List<UserDto> user;
        private List<LoveDto> love;

        @Getter
        @Setter
        public static class UserDto {
            private Integer userId;
            private String profile;
        }

        @Getter
        @Setter
        public static class LoveDto {
            private Integer loveId = 0;
            private String css = "";
        }

    }

    public void makeBlockInfo(String keyword) {
        this.keyword = keyword;
        this.blockCount = 5;

        this.currentBlock = currentPage / blockCount;
        this.startPageNum = 1 + blockCount * currentBlock;
        this.lastPageNum = 5 + blockCount * currentBlock;
        this.totalPage = (int) Math.ceil((double) totalCount / ROW);
        if (this.currentPage == totalPage - 1) {
            this.isLast = true;
        } else {
            this.isLast = false;
        }

        if (totalPage < lastPageNum) {
            this.lastPageNum = totalPage;
        }
    }
}
