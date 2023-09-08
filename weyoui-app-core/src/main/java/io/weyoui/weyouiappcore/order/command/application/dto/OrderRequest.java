package io.weyoui.weyouiappcore.order.command.application.dto;

import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {

    private List<OrderProduct> orderProducts = new ArrayList<>();
    private GroupId groupId;
    private String message;

}
