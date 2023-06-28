package io.weyoui.weyouiappcore.user.infrastructure;

import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.domain.UserId;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, UserId> {

    Optional<User> findById(UserId id);

    void save(User user);

    void delete(User user);

}
