package shop.mtcoding.jobara.apply.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndResume;
import shop.mtcoding.jobara.apply.dto.ApplyResp.ApplyJoinBoardAndUser;
import shop.mtcoding.jobara.apply.dto.ApplyResp.MailDto;

@Mapper
public interface ApplyRepository {

    public List<Apply> findAll();

    public Apply findById(int id);

    public int insert(Apply apply);

    public int updateById(Apply apply);

    public int deleteById(int id);

    public Apply findByUserIdAndBoardId(Apply apply);

    public List<ApplyJoinBoardAndUser> findByUserIdWithBoardAndUser(int userId);

    public List<ApplyJoinBoardAndResume> findByUserIdWithBoardAndResume(int userId);

    public MailDto findByIdWithBoardForMail(int id);
}
