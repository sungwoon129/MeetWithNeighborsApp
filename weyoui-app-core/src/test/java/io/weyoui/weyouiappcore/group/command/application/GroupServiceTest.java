package io.weyoui.weyouiappcore.group.command.application;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import io.weyoui.weyouiappcore.group.infrastructure.GroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

class GroupServiceTest {

    @Mock
    GroupRepository groupRepository;

    @InjectMocks
    GroupService groupService;

    @DisplayName("클라이언트의 요청대로 그룹이 생성된다")
    @Test
    void createGroupTest() {
        //given
        GroupRequest groupRequest = GroupRequest.builder()
                .name("운동 모임")
                .category(GroupCategory.WORKOUT.name())
                .capacity(5)
                .description("공원에서 운동해요!")
                .startTime(LocalDateTime.now().minusMinutes(10))
                .endTime(LocalDateTime.now().plusHours(1))
                .venue(new Address("서울","한강","123-456",new Point(37.5650407,126.8858048)))
                .build();

        //when
        groupService.createGroup(groupRequest);
    }

}