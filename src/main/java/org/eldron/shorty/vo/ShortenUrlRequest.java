package org.eldron.shorty.vo;

import lombok.Data;

@Data
public class ShortenUrlRequest {
    String url;

    public ShortenUrlRequest(String url) {
        this.url = url;
    }
}
