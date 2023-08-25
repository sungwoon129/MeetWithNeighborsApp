package io.weyoui.weyouiappcore.config.app_config;

import io.weyoui.weyouiappcore.config.exception.PageSizeOutOfBoundsException;
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

        String pageSize = webRequest.getParameter(getParameterNameToUse(getSizeParameterName(), methodParameter));
        validate(pageSize);
        return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }

    private void validate(final String pageSize) {
        if (pageSize != null && Integer.parseInt(pageSize) > 999) {
            throw new PageSizeOutOfBoundsException("size는 최대 999이하의 값을 가져야합니다.");
        }
    }
}
