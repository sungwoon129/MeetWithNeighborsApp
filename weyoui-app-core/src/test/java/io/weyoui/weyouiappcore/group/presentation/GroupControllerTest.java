package io.weyoui.weyouiappcore.group.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.weyoui.weyouiappcore.MessageConverterTestConfig;
import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupAddResponse;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import io.weyoui.weyouiappcore.group.infrastructure.GroupRepository;
import io.weyoui.weyouiappcore.group.query.application.dto.GroupViewResponse;
import io.weyoui.weyouiappcore.group.query.infrastructure.GroupQueryRepository;
import io.weyoui.weyouiappcore.user.command.application.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import io.weyoui.weyouiappcore.user.query.infrastructure.UserQueryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.junit.jupiter.MockitoExtension;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(MessageConverterTestConfig.class)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    MockMvc mvc;

    @Autowired
    GroupQueryRepository groupQueryRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserQueryRepository userQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JtsModule jtsModule;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(jtsModule);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }


    @WithMockUser
    @DisplayName("조건에 맞는 모임을 검색한다")
    @Test
    void searchGroupTest() throws Exception {
        //given
        String expectedName = "테스트 그룹";

        groupRepository.save(Group.builder()
                        .id(groupRepository.nextId())
                        .name(expectedName)
                        .state(GroupState.BEFORE_ACTIVITY)
                        .place(new Address("경기도","서울특별시","123-456",new GeometryFactory().createPoint(new Coordinate(37.37369668919236,127.11314527061833))))
                        .category(GroupCategory.WORKOUT)
                        .build());

        String uri = "http://localhost:" + port + "/api/v1/users/groups";

        //when
        MvcResult result = mvc.perform(get(uri)
                        .queryParam("size","10")
                        .queryParam("name","테스트")
                        .queryParam("state","B")
                        .queryParam("location.longitude","37.37369668919236")
                        .queryParam("location.latitude","127.11314527061833")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //then
        CommonResponse<List<GroupViewResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(expectedName, response.getData().get(0).getName());

    }

    @DisplayName("클라이언트의 요청대로 그룹이 생성된다")
    @Test
    void createGroupTest() throws Exception {
        //given
        insertAnyUser();
        UserId userId = getFirstUserIdFromDb();

        UserResponse.Token token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(userId,"",
                Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()))),userId);

        GroupRequest groupRequest = GroupRequest.builder()
                .name("운동 모임")
                .category(GroupCategory.WORKOUT.name())
                .capacity(5)
                .description("공원에서 운동해요!")
                .startTime(LocalDateTime.now().minusMinutes(10))
                .endTime(LocalDateTime.now().plusHours(1))
                .place(new Address(
                        "서울",
                        "한강",
                        "123-456",
                        new GeometryFactory().createPoint(new Coordinate(123d,456d))))
                .build();

        String apiUrl = "http://localhost:" + port + "/api/v1/users/group";

        //when
        MvcResult result = mvc.perform(post(apiUrl)
                                    .header(HttpHeaders.AUTHORIZATION,token.getGrantType() + " " + token.getAccessToken())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(groupRequest))
                                    .with(csrf()))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn();

        //then
        CommonResponse<GroupAddResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        Group group = groupQueryRepository.findById(new GroupId(response.getData().getGroupId())).orElseThrow(() -> new IllegalArgumentException("아이디와 일치하는 그룹이 없습니다."));

        assertEquals(group.getName(),groupRequest.getName());
        assertEquals(group.getCapacity(),groupRequest.getCapacity());
        assertEquals(group.getDescription(),groupRequest.getDescription());
        assertEquals(group.getPlace().getFullAddress(),groupRequest.getPlace().getFullAddress());
        assertEquals(group.getPlace().getZipCode(),groupRequest.getPlace().getZipCode());
        assertEquals(group.getPlace().getPoint().getX(),groupRequest.getPlace().getPoint().getX());
        assertEquals(group.getPlace().getPoint().getY(),groupRequest.getPlace().getPoint().getY());
        assertEquals(group.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm")),groupRequest.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm")));
        assertEquals(group.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm")),groupRequest.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm")));

        assertEquals(group.getState(), GroupState.IN_ACTIVITY);

    }

    @WithMockUser
    @DisplayName("모임 활동시간을 변경한다")
    @Test
    void  changeActivityTime() throws Exception {
        //given
        GroupId groupId = insertAnyGroup();
        LocalDateTime expectedStartTime = LocalDateTime.now().plusMinutes(10);
        LocalDateTime expectedEndTime = expectedStartTime.plusMinutes(30);
        GroupRequest groupRequest = GroupRequest.builder()
                .startTime(expectedStartTime)
                .endTime(expectedEndTime)
                .build();

        String apiUrl = "/api/v1/users/groups/" + groupId.getId() + "/activity-time";

        //when
        mvc.perform(put(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
            .andDo(print())
            .andExpect(status().isOk());

        //then
        Group group = groupQueryRepository.findById(groupId).orElseThrow(() -> new NullPointerException("해당 id를 가진 모임이 존재하지 않습니다."));
        assertEquals(group.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm:ss:SSS")),expectedStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm:ss:SSS")));
        assertEquals(group.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm:ss:SSS")),expectedEndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm:ss:SSS")));

    }

    @WithMockUser
    @DisplayName("모임 활동장소를 변경한다")
    @Test
    void changeActivityPlace() throws Exception {
        //given
        GroupId groupId = insertAnyGroup();
        String expectedFullAddress = "서울특별시 강남구 서초대로";
        Point expectedLocation = new GeometryFactory().createPoint(new Coordinate(38.37369668919236, 128.11314527061833));
        GroupRequest groupRequest = GroupRequest.builder()
                .place(new Address("서울특별시","강남구 서초대로","123-456",expectedLocation))
                .build();

        String apiUrl = "/api/v1/users/groups/" + groupId.getId() + "/activity-place";

        //when
        mvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        Group group = groupQueryRepository.findById(groupId).orElseThrow(() -> new NullPointerException("해당 id를 가진 모임이 존재하지 않습니다."));
        assertEquals(expectedFullAddress, group.getPlace().getFullAddress());
        assertEquals((double) Math.round(expectedLocation.getX() * 10000) / 10000, (double) Math.round(group.getPlace().getPoint().getX() * 10000) / 10000);
        assertEquals((double) Math.round(expectedLocation.getY() * 10000) / 10000, (double) Math.round(group.getPlace().getPoint().getY() * 10000) / 10000);
    }

    private GroupId insertAnyGroup() {
        GroupId nextId = groupRepository.nextId();
        groupRepository.save(
                Group.builder()
                        .id(nextId)
                        .name("임의의 모임")
                        .state(GroupState.IN_ACTIVITY)
                        .place(new Address("경기도","성남시","123-456",new GeometryFactory().createPoint(new Coordinate(37.37369668919236,127.11314527061833))))
                        .category(GroupCategory.MEAL)
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusHours(1))
                        .build()
        );
        return nextId;
    }

    private void insertAnyUser() {
        userRepository.save(
                User.builder()
                        .id(userRepository.nextUserId())
                        .nickname("any user")
                        .build());
    }

    private UserId getFirstUserIdFromDb() {
        Page<User> result = userQueryRepository.findByConditions(new UserSearchRequest(), PageRequest.of(0,1));
        
        return result.getContent().stream().findFirst().orElseThrow(() -> new NotFoundUserException("no user")).getId();
    }

}
