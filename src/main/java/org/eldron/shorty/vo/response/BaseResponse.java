package org.eldron.shorty.vo.response;

import lombok.ToString;

/**
 * Base http response object. This vo should be always used as the http return json.
 *
 * @param <T> The object to be returned
 */
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
