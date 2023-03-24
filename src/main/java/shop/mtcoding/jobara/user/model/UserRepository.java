package shop.mtcoding.jobara.user.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import shop.mtcoding.jobara.user.vo.UserVo;

@Mapper
public interface UserRepository {

    public List<User> findAll();

    public User findById(int id);

    public int insert(User user);

    public int deleteById(int id);

    public int updateById(User user);

    public int insertForEmployee(User user);

    public int insertForCompany(User user);

    public User findByUsername(String username);

    public User findByUsernameAndPassword(User user);
}
