package io.weyoui.weyouiappcore.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonResponse<T>  {
    private T data;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int count;

    public CommonResponse(T data, int count) {
        this.data = data;
        this.count = count;
    }

    public CommonResponse(T data) {
        this.data = data;

    }
}
