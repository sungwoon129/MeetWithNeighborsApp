package io.weyoui.weyouiappcore.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonResponse<T>  {

    private ResultYnType resultYn = ResultYnType.N;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long total;

    public CommonResponse(ResultYnType resultYn, T data, long count) {
        this.resultYn = resultYn;
        this.data = data;
        this.total = count;
    }

    public CommonResponse(T data, long count) {
        this.resultYn = ResultYnType.Y;
        this.data = data;
        this.total = count;
    }

    public CommonResponse(T data) {
        this.resultYn = ResultYnType.Y;
        this.data = data;

    }

    public CommonResponse(ResultYnType resultYn) {
        this.resultYn = resultYn;
    }
}
