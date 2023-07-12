package io.weyoui.domain;

public class CommonResponse<T>  {
    private T data;
    private int count;

    public CommonResponse(T data, int count) {
        this.data = data;
        this.count = count;
    }

    public CommonResponse(T data) {
        this.data = data;
        this.count = 1;
    }
}
