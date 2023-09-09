package io.weyoui.weyouiappcore.config.app_config;

import io.weyoui.weyouiappcore.common.exception.PageSizeOutOfBoundsException;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CustomPageableHandlerMethodArgumentResolver extends PageableHandlerMethodArgumentResolver {

    @NonNull
    @Override
    public Pageable resolveArgument(@NonNull MethodParameter methodParameter, @Nullable ModelAndViewContainer mavContainer,
                                    @NotNull NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {

        final Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        if(methodParameter.hasParameterAnnotation(LimitedPageSize.class)) {
            final int maxSize = methodParameter.getParameterAnnotation(LimitedPageSize.class).maxSize();
            validate(pageable, maxSize);
        }

        return pageable;
    }

    private void validate(final Pageable pageable, final int maxSize) {
        if(pageable.getPageSize() > maxSize) {
            throw new PageSizeOutOfBoundsException("page size는 최대 " +  maxSize + "이하의 값을 가져야합니다.");
        }
    }
}
