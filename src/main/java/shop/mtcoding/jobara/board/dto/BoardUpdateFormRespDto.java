package shop.mtcoding.jobara.board.dto;

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
public class BoardUpdateFormRespDto {
    private Integer id;
    private String title;
    private String content;

    @JsonIgnore
    private Integer careerInteger;
    private String career;
    @JsonIgnore
    private Integer educationInteger;
    private String education;
    @JsonIgnore
    private Integer jobTypeInteger;
    private String jobType;

    private String favor;
    private String deadline;
    private Integer userId;
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

    public void parseIntegerInfo() {
        this.career = CareerParse.careerToString(careerInteger);
        this.jobType = JobTypeParse.jopTypeToString(jobTypeInteger);
        this.education = EducationParse.educationToString(educationInteger);
    }
}
