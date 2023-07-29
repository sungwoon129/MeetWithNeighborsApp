package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.user.command.application.UserService;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @LocalServerPort
    int port;

    @Autowired
    UserService userService;

    @WithMockUser(roles = {"USER"})
    @Test
    void findByIdTest() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("exUser@weyoui.com")
                .password("123456")
                .passwordConfirm("123456")
                .build();

        UserId id = userService.signUp(signUpRequest);
        String uri = "http://localhost:" + port + "/api/v1/users/" + id.getId();


        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());

    }



}