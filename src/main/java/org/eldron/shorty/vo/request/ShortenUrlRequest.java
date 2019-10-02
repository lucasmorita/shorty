package org.eldron.shorty.vo.request;

import lombok.ToString;

/**
 * VO sent in the request to shorten an url.
 */
@ToString
public class ShortenUrlRequest {
    private String url;

    public ShortenUrlRequest() {
        super();
    }

    public ShortenUrlRequest(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
