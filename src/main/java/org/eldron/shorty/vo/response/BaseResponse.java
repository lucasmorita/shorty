package org.eldron.shorty.vo.response;

import lombok.ToString;

@ToString
public class BaseResponse<T> {
    private String message;
    private T data;

    protected BaseResponse() {
    }

    public BaseResponse(final String message) {
        this.message = message;
    }

    public BaseResponse(final String message, final T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }
}
