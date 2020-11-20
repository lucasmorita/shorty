package org.eldron.shorty.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Url {
    @JsonProperty("shortened_url")
    private String shortenedUrl;
    @JsonProperty("original_url")
    private String originalUrl;
}
