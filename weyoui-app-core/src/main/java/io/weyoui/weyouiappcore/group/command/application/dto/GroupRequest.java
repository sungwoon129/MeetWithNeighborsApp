package io.weyoui.weyouiappcore.group.command.application.dto;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GroupRequest {

    private String name;
    private GroupCategory category;
    private String description;
    private Address venue;
    private int capacity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder
    public GroupRequest(String name, GroupCategory category, String description, Address venue,
                                     int capacity, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.venue = venue;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
