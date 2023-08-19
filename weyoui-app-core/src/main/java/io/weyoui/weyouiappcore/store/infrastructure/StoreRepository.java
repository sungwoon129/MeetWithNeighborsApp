package io.weyoui.weyouiappcore.store.infrastructure;

import io.weyoui.weyouiappcore.store.domain.Store;
import io.weyoui.weyouiappcore.store.domain.StoreId;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public interface StoreRepository extends Repository<StoreId, Store> {

    void save(Store store);

    default StoreId nextId() {
        int randomNum = ThreadLocalRandom.current().nextInt(90000) + 10000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNum);
        return new StoreId(number);
    }
}
