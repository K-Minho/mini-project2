package shop.mtcoding.jobara.board.dto;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.jobara.common.util.CareerParse;
import shop.mtcoding.jobara.common.util.EducationParse;
import shop.mtcoding.jobara.common.util.JobTypeParse;

@Getter
@Setter
public class BoardDetailRespDto {
    private Integer id;
    private String title;
    private String content;
    @JsonIgnore
    private Integer careerInteger;
    @JsonIgnore
    private Integer jobTypeInteger;
    @JsonIgnore
    private Integer educationInteger;
    private String career;
    private String jobType;
    private String education;
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

    public void faSoild() {
        if (love == null) {
            love = new LoveDto();
            love.id = 0;
            love.css = "";
        } else {
            love.css = "fa-solid";
        }
    }

    public void parseIntegerInfo() {
        this.career = CareerParse.careerToString(careerInteger);
        this.jobType = JobTypeParse.jopTypeToString(jobTypeInteger);
        this.education = EducationParse.educationToString(educationInteger);
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
        private String css;

    }
}