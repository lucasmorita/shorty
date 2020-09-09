package org.eldron.shorty.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * VO sent in the request to shorten an url.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortenUrlRequest {
    private String url;
}
