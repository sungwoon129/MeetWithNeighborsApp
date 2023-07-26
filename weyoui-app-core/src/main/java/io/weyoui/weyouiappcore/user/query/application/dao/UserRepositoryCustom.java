package io.weyoui.weyouiappcore.user.query.application.dao;

import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.presentation.dto.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> searchAll(UserSearch userSearch, Pageable pageable);
}
