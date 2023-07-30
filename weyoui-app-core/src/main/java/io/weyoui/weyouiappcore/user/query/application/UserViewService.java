package io.weyoui.weyouiappcore.user.query.application;

import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@RequiredArgsConstructor
@Service
public class UserViewService {

    private final UserRepository userRepository;


    public Page<User> findAll(UserSearchRequest usersearch, Pageable pageable) {
        return userRepository.searchAll(usersearch, pageable);
    }

    public User findById(UserId id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("해당 ID를 가진 회원이 존재하지 않습니다."));
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserException("해당 email을 가진 회원을 찾을 수 없습니다."));
    }

}
