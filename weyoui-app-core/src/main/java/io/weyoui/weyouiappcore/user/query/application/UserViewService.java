package io.weyoui.weyouiappcore.user.query.application;

import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.presentation.dto.UserSearch;
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


    public Page<User> findAll(UserSearch usersearch, Pageable pageable) {
        return userRepository.searchAll(usersearch, pageable);
    }
}
