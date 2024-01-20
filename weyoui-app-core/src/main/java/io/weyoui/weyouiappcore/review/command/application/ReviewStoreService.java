package io.weyoui.weyouiappcore.review.command.application;

import io.weyoui.weyouiappcore.review.command.application.domain.ReviewStore;
import io.weyoui.weyouiappcore.review.command.application.dto.Score;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;

public interface ReviewStoreService {

    ReviewStore createReviewStore(StoreId storeId, Score score);
}
