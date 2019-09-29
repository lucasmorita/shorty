package org.eldron.shorty.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class Url {
    @JsonProperty("shortUrl")
    private String id;

    @JsonProperty("url")
    private String originalUrl;

    protected Url() {}

    public Url(final String id, final String originalUrl) {
        this.id = id;
        this.originalUrl = originalUrl;
    }
}
