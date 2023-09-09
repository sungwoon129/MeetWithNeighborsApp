package io.weyoui.weyouiappcore.order.command.domain;

public interface CancelOrderPolicy {
    boolean hasCancellationPermission(Order order, Canceller canceller);
}
