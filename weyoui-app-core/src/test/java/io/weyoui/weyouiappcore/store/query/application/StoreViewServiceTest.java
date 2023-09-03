package io.weyoui.weyouiappcore.store.query.application;

import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import io.weyoui.weyouiappcore.store.query.infrastructure.StoreQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class StoreViewServiceTest {

    @Autowired
    StoreQueryRepository storeQueryRepository;

    @Autowired
    StoreViewService storeViewService;


    @DisplayName("가게 목록 조회 서비스 호출 시 성능 측정 로그가 남는다")
    @Test
    void findByConditions_test() {
        //given
        StoreSearchRequest searchRequest = new StoreSearchRequest();
        Pageable pageable = PageRequest.of(0,10);

        //when
        storeViewService.findByConditions(searchRequest,pageable);

    }
}