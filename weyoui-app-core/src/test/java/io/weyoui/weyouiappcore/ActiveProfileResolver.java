package io.weyoui.weyouiappcore;

import org.springframework.test.context.ActiveProfilesResolver;

public class ActiveProfileResolver implements ActiveProfilesResolver {

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    @Override
    public String[] resolve(Class<?> testClass) {
        String property = System.getProperty(SPRING_PROFILES_ACTIVE);
        return new String[] {property};
    }
}
