package org.eldron.shorty.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * http error response object. This vo should be always used as the http return on errors.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
}
