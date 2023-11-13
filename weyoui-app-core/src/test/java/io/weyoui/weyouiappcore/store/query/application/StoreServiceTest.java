package io.weyoui.weyouiappcore.store.query.application;

import io.weyoui.weyouiappcore.common.exception.NoAuthException;
import io.weyoui.weyouiappcore.store.command.application.StoreService;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql("classpath:store-init-test.sql")
public class StoreServiceTest {

    @Autowired
    StoreService storeService;

    /**
     * service 단위 테스트의 경우 비즈니스 로직에서 throw하는 예외를 발생시키지만 통합테스트로 구현해 MockMvc에서 HTTP 메소드로 요청을 보낼경우 예외내용을 클라이언트에 전달
     */
    @DisplayName("가게 주인이 아닌 회원은 가게를 수정 시도를 하면 NoAuthException을 발생시킨다")
    @Test
    void updateStoreTest_fail() {
        //given
        UserId userId = new UserId("wrong user");
        StoreId storeId = new StoreId("store1");
        StoreRequest storeRequest = new StoreRequest();

        //when, then
        assertThrows(NoAuthException.class, () ->{
            storeService.updateStore(userId, storeId, storeRequest);
        });

    }

    @DisplayName("가게 주인이 아닌 회원은 가게를 삭제 시도를 하면 NoAuthException을 발생시킨다")
    @Test
    void DeleteStoreTest_fail() {
        //given
        UserId userId = new UserId("wrong user");
        StoreId storeId = new StoreId("store1");

        //when, then
        assertThrows(NoAuthException.class, () ->{
            storeService.deleteStore(userId, storeId);
        });

    }

}
