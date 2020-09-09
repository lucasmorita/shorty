package org.eldron.shorty.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Base http response object. This vo should be always used as the http return json.
 *
 * @param <T> The object to be returned
 */
@Getter
@AllArgsConstructor
public class ErrorResponse<T> {
    private final String message;
}
