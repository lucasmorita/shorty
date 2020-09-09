package org.eldron.shorty.hash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "urls", timeToLive = 600L)
@Getter
@Builder
@AllArgsConstructor
public class UrlHash implements Serializable {
    @Id
    private final String id;
    private final String originalUrl;
}
