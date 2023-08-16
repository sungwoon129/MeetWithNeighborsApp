package io.weyoui.weyouiappcore.group.command.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String category;
    private String description;
    private Address place;
    private int capacity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "Asia/Seoul")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    @Builder
    public GroupRequest(String name, String category, String description, Address place,
                                     int capacity, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.place = place;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
