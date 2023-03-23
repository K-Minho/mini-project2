package shop.mtcoding.jobara.apply.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ApplyReq {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplyDecideReqDto {
        @NotNull(message = "유저 아이디를 입력하세요")
        @Min(1)
        private Integer userId;
        @NotNull(message = "상태를 변경하세요")
        @Min(-1)
        @Max(1)
        private Integer state;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplyReqDto {
        @NotNull(message = "공고 아이디를 입력하세요")
        @Min(1)
        private Integer boardId;
        @NotNull(message = "이력서 아이디를 입력하세요")
        @Min(1)
        private Integer resumeId;
    }

}
