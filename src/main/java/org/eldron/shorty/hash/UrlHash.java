package org.eldron.shorty.hash;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("urls")
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class UrlHash implements Serializable {
    @Id
    private String id;
    private String originalUrl;

    protected UrlHash() {}

    public UrlHash(final String id, final String originalUrl) {
        this.id = id;
        this.originalUrl = originalUrl;
    }
}
