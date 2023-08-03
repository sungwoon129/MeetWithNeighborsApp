package io.weyoui.weyouiappcore.user.query.infrastructure;

import io.weyoui.weyouiappcore.TestConfig;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.query.application.dto.CustomPageRequest;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserQueryRepositoryTest {

    @Autowired
    UserQueryRepository userQueryRepository;

    @Autowired
    UserRepository userRepository;

    @DisplayName("회원 검색 쿼리가 동적으로 생성된다")
    @Test
    void dynamicUserSearchQueryTest() {
        //given
        User user = User.builder()
                .id(userRepository.nextUserId())
                .nickname("test")
                .email("test@weyoui.io")
                // TODO : Point 객체 생성해서 검색하는 테스트코드 작성필요
                //.address(new Address("경기도","정자동","123-456", new Point(123,456)))
                .build();
        userRepository.save(user);

        UserSearchRequest search = UserSearchRequest.builder()
                .email("test")
                .nickname("test")
                .build();
        CustomPageRequest pageRequest = new CustomPageRequest();


        //when
        Page<User> result = userQueryRepository.findByConditions(search, PageRequest.of(pageRequest.getPage(), pageRequest.getSize()));

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.get().findFirst().isPresent()).isEqualTo(true);
        assertTrue(result.get().findFirst().get().getEmail().contains(user.getEmail()));
        assertTrue(result.get().findFirst().get().getAddress().getFullAddress().contains(user.getAddress().getFullAddress()));

    }

}