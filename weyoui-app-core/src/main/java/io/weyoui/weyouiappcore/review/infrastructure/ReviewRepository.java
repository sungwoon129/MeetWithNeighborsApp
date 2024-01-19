package io.weyoui.weyouiappcore.review.infrastructure;

import io.weyoui.weyouiappcore.review.command.application.domain.Review;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewId;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public interface ReviewRepository extends Repository<Review, ReviewId> {

    void save(Review review);

    default ReviewId nextId() {
        int randomNum = ThreadLocalRandom.current().nextInt(90000) + 10000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNum);
        return new ReviewId(number);
    }
}
