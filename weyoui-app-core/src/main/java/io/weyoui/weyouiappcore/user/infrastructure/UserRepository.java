package io.weyoui.weyouiappcore.user.infrastructure;

import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public interface UserRepository extends Repository<User, UserId> {

    void save(User user);

    default UserId nextUserId() {
        int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNo);
        return new UserId(number);
    }
}
