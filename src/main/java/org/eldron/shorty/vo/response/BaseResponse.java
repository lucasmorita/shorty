package org.eldron.shorty.vo.response;

import lombok.ToString;

@ToString
public class BaseResponse {
    private String message;
    private Object data;

    public BaseResponse(String message) {
        this.message = message;
    }

    public BaseResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
