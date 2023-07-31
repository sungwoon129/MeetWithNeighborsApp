package io.weyoui.weyouiappcore.user.command.domain;

import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.dao.UserRepositoryCustom;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public interface UserRepository extends Repository<User, UserId>, UserRepositoryCustom {

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(String email);

    void save(User user);

    boolean existsByEmail(String email);


    default UserId nextUserId() {
        int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNo);
        return new UserId(number);
    }
}