package io.weyoui.weyouiappcore.order.infrastructure;

import io.weyoui.weyouiappcore.order.command.domain.CancelOrderPolicy;
import io.weyoui.weyouiappcore.order.command.domain.Canceller;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityCancelPolicy implements CancelOrderPolicy {
    @Override
    public boolean hasCancellationPermission(Order order, Canceller canceller) {
        return isCancellerOrderAndOrderer(order, canceller) || isCurrentUserAdminRole();
    }

    private boolean isCancellerOrderAndOrderer(Order order, Canceller canceller) {
        return order.getOrderer().getUserId().getId().equals(canceller.getUserId());
    }

    private boolean isCurrentUserAdminRole() {
        SecurityContext context = SecurityContextHolder.getContext();
        if(context == null) return false; // 회원 정보가 존재하지 않는 경우(Spring Security context 내에)
        Authentication authentication = context.getAuthentication();
        if(authentication == null) return false; // 권한정보가 없는 경우
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(authorities == null) return false;
        return authorities.stream().anyMatch(auth -> auth.getAuthority().equals(RoleType.ROLE_ADMIN.name()));
    }
}
