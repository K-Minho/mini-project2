package shop.mtcoding.jobara.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Board {
    private Integer id;
    private Integer companyId;
    private String title;
    private String content;
    private Integer career;
    private Timestamp createdAt;
}
