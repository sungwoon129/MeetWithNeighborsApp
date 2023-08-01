package io.weyoui.weyouiappcore.user.query.infrastructure;

import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserQueryRepository extends Repository<User, UserId>,UserQueryRepositoryCustom {
    Optional<User> findById(UserId id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
