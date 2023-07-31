package io.weyoui.weyouiappcore.user.infrastructure;

import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.TestConfig;
import io.weyoui.weyouiappcore.user.command.domain.UserRepository;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import io.weyoui.weyouiappcore.user.command.application.dto.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @DisplayName("회원 가입할 때 입력한 비밀번호가 암호화되어서 DB에 저장된다")
    @Test
    void encodePassword_success() {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("example@weyoui.com")
                .password("123456")
                .passwordConfirm("123456")
                .build();

        //when
        User user = User.builder()
                .id(userRepository.nextUserId())
                .password(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()))
                .role(RoleType.ROLE_USER)
                .build();

        userRepository.save(user);

        Page<User> users = userRepository.searchAll(new UserSearchRequest(), PageRequest.of(0,10));
        User findUser = users.stream().findFirst().orElseThrow(() -> new NullPointerException("불러올 회원이 존재하지 않습니다."));

        //then
        assertThat(signUpRequest.getPassword()).isNotEqualTo(findUser.getPassword());

    }

}