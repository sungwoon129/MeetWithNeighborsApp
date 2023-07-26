package io.weyoui.weyouiappcore.user.presentation.dto;

import jakarta.validation.constraints.Max;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class CustomPageRequest {
    private int page = 0;
    @Max(999)
    private int size = 20;
    private Sort sort;

    public CustomPageRequest(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }
}
