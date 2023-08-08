package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AdminControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository repository;

    @DisplayName("회원을 여러가지 조건을 정해서 검색이 가능하다")
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void findByConditionsTest() throws Exception {
        //given
        repository.save(User.builder()
                        .id(repository.nextUserId())
                        .nickname("test")
                        .email("test@weyoui.io")
                        .address(new Address("경기도","정자동","123-456",new GeometryFactory().createPoint(new Coordinate(123d,456d))))
                        .build());


        //when,then
        ResultActions resultActions = mvc.perform(get("/api/v1/admin/users?email=test&size=30")
                    )
                .andDo(print())
                .andExpect(status().isOk());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("test@weyoui.io"));

    }

}