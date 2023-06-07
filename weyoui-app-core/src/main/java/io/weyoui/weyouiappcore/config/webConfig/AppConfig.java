package io.weyoui.weyouiappcore.config.webConfig;

import io.weyoui.weyouiappcore.group.domain.Category;
import io.weyoui.weyouiappcore.group.domain.GroupState;
import io.weyoui.weyouiappcore.order.domain.OrderState;
import io.weyoui.weyouiappcore.store.domain.StoreCategory;
import io.weyoui.weyouiappcore.store.domain.StoreState;
import io.weyoui.weyouiappcore.user.domain.UserState;
import io.weyoui.weyouiappcore.util.EnumMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("userState", UserState.class);
        enumMapper.put("groupState", GroupState.class);
        enumMapper.put("category", Category.class);
        enumMapper.put("orderState", OrderState.class);
        enumMapper.put("storeCategory", StoreCategory.class);
        enumMapper.put("storeState", StoreState.class);

        return enumMapper;
    }
}
