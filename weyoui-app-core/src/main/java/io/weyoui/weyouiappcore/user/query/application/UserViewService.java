package io.weyoui.weyouiappcore.user.query.application;

import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.command.application.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.command.application.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import io.weyoui.weyouiappcore.user.query.infrastructure.UserQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@Service
public class UserViewService {

    private final UserQueryRepository userQueryRepository;

    public UserViewService(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }


    public Page<User> findAll(UserSearchRequest userSearchRequest, Pageable pageable) {
        return userQueryRepository.searchAll(userSearchRequest, pageable);
    }

    public User findById(UserId id) {

        return userQueryRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("해당 ID를 가진 회원이 존재하지 않습니다."));
    }

    public User findByEmail(String email) {

        return userQueryRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserException("해당 email을 가진 회원을 찾을 수 없습니다."));
    }

    public void validationDuplicateUser(String email) {
        if(userQueryRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
    }

}
