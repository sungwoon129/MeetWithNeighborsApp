package io.weyoui.weyouiappcore.config.app_config;

import com.querydsl.core.util.StringUtils;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static io.weyoui.weyouiappcore.user.infrastructure.filter.JwtAuthenticationFilter.AUTHORIZATION_HEADER;

@RequiredArgsConstructor
@Component
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider tokenProvider;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUserId.class) && parameter.getParameterType().equals(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String bearerToken = webRequest.getHeader(AUTHORIZATION_HEADER);
        //if(StringUtils.isNullOrEmpty(bearerToken)) throw new SecurityException("토큰값이 null 이거나 비어있습니다.");

        String token = bearerToken != null ? bearerToken.substring(7) : "token";
        return tokenProvider.getUserIdByToken(token);
    }
}
