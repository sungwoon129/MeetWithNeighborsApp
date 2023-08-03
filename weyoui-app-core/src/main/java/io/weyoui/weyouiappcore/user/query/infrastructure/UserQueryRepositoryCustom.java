package io.weyoui.weyouiappcore.user.query.infrastructure;

import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryRepositoryCustom {

    Page<User> findByConditions(UserSearchRequest userSearchRequest, Pageable pageable);
}
