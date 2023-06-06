package io.weyoui.weyouiappcore.config.webConfig;

import io.weyoui.weyouiappcore.group.domain.Category;
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
        enumMapper.put("category", Category.class);

        return enumMapper;
    }
}
