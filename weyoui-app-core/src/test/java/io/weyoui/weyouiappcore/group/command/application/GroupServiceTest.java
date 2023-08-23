package io.weyoui.weyouiappcore.group.command.application;

import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
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
                .place(new Address(
                        "서울",
                        "한강",
                        "123-456",
                        new GeometryFactory().createPoint(new Coordinate(123d,456d))))
                .build();

        given(groupService.createGroup(groupRequest)).willReturn(new GroupId("123"));

        //when
        groupService.createGroup(groupRequest);
    }

}