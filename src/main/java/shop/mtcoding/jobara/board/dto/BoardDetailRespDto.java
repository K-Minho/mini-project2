package shop.mtcoding.jobara.board.dto;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDetailRespDto {
    private Integer id;
    private String title;
    private String content;
    private Integer career;
    private Integer jobType;
    private Integer education;
    private String favor;

    private CompanyDto company;
    private UserDto user;
    private List<ResumeDto> resume;
    private LoveDto love;
    @JsonIgnore
    private String needParse;
    private List<Integer> skill;

    public void skillParse(String needParse) {
        if (needParse == null) {
            return;
        }
        String[] skills = needParse.split(",");
        this.skill = Arrays.stream(skills)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class CompanyDto {
        private Integer userId;
        private String companyName;
        private String companyScale;
        private String companyField;
    }

    @Getter
    @Setter
    public static class UserDto {
        private Integer id;
        private String profile;
    }

    @Getter
    @Setter
    public static class ResumeDto {
        private Integer id;
        private Integer userId;
        private String title;
        private String content;
        private Timestamp createdAt;

        // public String getCreatedAtToString() {
        // return DateParse.format(createdAt);
        // }
    }

    @Getter
    @Setter
    public static class LoveDto {
        private Integer id;
        private Integer boardId;
        private Integer userId;
    }
}