package io.weyoui.weyouiappcore.review.infrastructure;

import io.weyoui.weyouiappcore.review.command.application.ReviewStoreService;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewStore;
import io.weyoui.weyouiappcore.review.command.application.dto.Score;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewStoreServiceImpl implements ReviewStoreService {
    private final StoreViewService storeViewService;

    public ReviewStoreServiceImpl(StoreViewService storeViewService) {
        this.storeViewService = storeViewService;
    }
    @Override
    public ReviewStore createReviewStore(StoreId storeId, Score score) {

        Store store = storeViewService.findById(storeId);
        store.calcRating(score.getValue().floatValue());
        return new ReviewStore(store.getId(), store.getName());
    }
}
