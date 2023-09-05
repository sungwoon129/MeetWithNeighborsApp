package io.weyoui.weyouiappcore.product.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ImageViewResponse {

    private Long id;
    private String path;
    private int listIdx;

    @Builder
    @QueryProjection
    public ImageViewResponse(long id, String path, int listIdx) {
        this.id = id;
        this.path = path;
        this.listIdx = listIdx;
    }
}
