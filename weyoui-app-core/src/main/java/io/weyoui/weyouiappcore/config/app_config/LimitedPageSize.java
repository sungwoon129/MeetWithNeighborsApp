package io.weyoui.weyouiappcore.config.app_config;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LimitedPageSize {
    int maxSize() default 999;
}
