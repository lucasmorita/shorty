package org.eldron.shorty.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.eldron.shorty.exception.InvalidUrlException;
import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.hash.UrlHash;
import org.eldron.shorty.repository.UrlRepository;
import org.eldron.shorty.vo.Url;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final RestTemplate restTemplate;

    public UrlService(final UrlRepository urlRepository, final RestTemplate restTemplate) {
        this.urlRepository = urlRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Gets original url from the shortened
     *
     * @param shortenedUrlId the shortened url id.
     * @return the vo of the url
     * @throws UrlNotFoundException if the shortened url doesn't exist
     */
    public Url getOriginalUrl(final String shortenedUrlId) {
        final var url = urlRepository.findById(shortenedUrlId);

        if (url.isPresent()) {
            return Url.builder()
                    .shortUrl(url.get().getId())
                    .url(url.get().getOriginalUrl())
                    .build();
        }
        throw new UrlNotFoundException();
    }

    /**
     * Saves a new url.
     *
     * @param url the original url to save.
     * @return the vo of the url
     */
    public Url shortenUrl(final String url) {
        validateUrl(url);

        final var urlHash = UrlHash.builder()
                .id(urlIdGenerator())
                .originalUrl(url)
                .build();

        urlRepository.save(urlHash);

        return Url.builder()
                .shortUrl(urlHash.getId())
                .url(urlHash.getOriginalUrl())
                .build();
    }

    private void validateUrl(final String url) {
        final var schemes = new String[]{"http", "https"};
        final var validator = new UrlValidator(schemes);
        if (!validator.isValid(url)) {
            throw new InvalidUrlException(url);
        }

        try {
            restTemplate.getForObject(url, String.class);
        } catch (final RestClientException e) {
            throw new InvalidUrlException(e);
        }
    }

    private String urlIdGenerator() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}
