package io.weyoui.weyouiappcore;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(resolver = ActiveProfileResolver.class)
@SpringBootTest
public class ActiveProfileTest {


    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @DisplayName("배포환경에 맞는 프로필을 로드한다")
    @Test
    void profile_test() {
    }


}
