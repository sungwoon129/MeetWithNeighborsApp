package io.weyoui.weyouiappcore.config.app_config;

import io.weyoui.weyouiappcore.config.exception.ErrorCode;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.store.domain.StoreCategory;
import io.weyoui.weyouiappcore.store.domain.StoreState;
import io.weyoui.weyouiappcore.user.command.domain.UserState;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.util.EnumMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final JwtTokenProvider tokenProvider;

    public AppConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("userState", UserState.class);
        enumMapper.put("groupState", GroupState.class);
        enumMapper.put("groupCategory", GroupCategory.class);
        enumMapper.put("orderState", OrderState.class);
        enumMapper.put("storeCategory", StoreCategory.class);
        enumMapper.put("storeState", StoreState.class);
        enumMapper.put("errorCode", ErrorCode.class);

        return enumMapper;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberIdArgumentResolver(tokenProvider));
    }
}
