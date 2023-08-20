package io.weyoui.weyouiappcore.store.query.infrastructure;

import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface StoreQueryRepository extends Repository<Store, StoreId>, StoreQueryRepositoryCustom {

    Optional<Store> findById(StoreId storeId);
}
