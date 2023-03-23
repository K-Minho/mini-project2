package shop.mtcoding.jobara.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.jobara.common.util.Hash;
import shop.mtcoding.jobara.common.util.Verify;
import shop.mtcoding.jobara.user.dto.UserReq.UserLoginReqDto;
import shop.mtcoding.jobara.user.model.User;
import shop.mtcoding.jobara.user.model.UserRepository;
import shop.mtcoding.jobara.user.vo.UserVo;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUser(UserLoginReqDto userLoginReqDto) {
        String salt = userRepository.findByUsername(userLoginReqDto.getUsername()).getSalt();
        String hashPassword = Hash.encode(userLoginReqDto.getPassword() + salt);
        User userPS = userRepository.findByUsernameAndPassword(
                new User(userLoginReqDto.getUsername(), hashPassword));
        Verify.validateObject(userPS, "유저네임이나 암호를 확인해주세요.");
        return userPS;
    }

    @Transactional(readOnly = true)
    public User checkUsername(String username) {
        User userPS = userRepository.findByUsername(username);
        return userPS;
    }
}
