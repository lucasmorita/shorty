package org.eldron.shorty.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.eldron.shorty.hash.UrlHash;
import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.repository.UrlRepository;
import org.eldron.shorty.vo.Url;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlService(final UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url getOriginalUrl(final String shortenedUrlId) throws UrlNotFoundException {
        final Optional<UrlHash> url = urlRepository.findById(shortenedUrlId);

        if (url.isPresent()) {
            return Url.builder()
                    .id(url.get().getId())
                    .originalUrl(url.get().getOriginalUrl())
                    .build();
        }
        throw new UrlNotFoundException("Url was not found");
    }

    public ResponseEntity shortenUrl(final String url) {
        final var urlEntity = UrlHash.builder()
                .id(urlIdGenerator())
                .originalUrl(url)
                .build();

        urlRepository.save(urlEntity);
        return ResponseEntity.ok().body(urlEntity);
    }

    private String urlIdGenerator() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}
