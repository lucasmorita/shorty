package org.eldron.shorty.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortenCustomUrlRequest {

    private String url;
    private String custom;

}
