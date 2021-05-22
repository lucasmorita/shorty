package org.eldron.shorty.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.eldron.shorty.entity.UrlEntity;
import org.eldron.shorty.exception.CustomUrlAlreadyExistsException;
import org.eldron.shorty.exception.InvalidUrlException;
import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.repository.UrlRepository;
import org.eldron.shorty.vo.Url;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Pattern;

@Service
public class UrlService {
    private static final String REGEX_PATTERN = "(http|https)://.+";
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
        final var url = urlRepository.findByShortenedUrl(shortenedUrlId);

        if (url.isPresent()) {
            return Url.builder()
                    .shortenedUrl(url.get().getShortenedUrl())
                    .originalUrl(url.get().getOriginalUrl())
                    .build();
        }
        throw new UrlNotFoundException();
    }

    /**
     * Saves a new url.
     *
     * @param url the original url to save.
     * @param customUrl The customized url that substitutes a random one.
     * @return the vo of the url
     */
    public Url shortenUrl(final String url, final String customUrl) {
        validateUrl(url);

        final var urlEntity = UrlEntity.builder()
                .shortenedUrl(customUrl == null ? urlIdGenerator() : customUrl)
                .originalUrl(url)
                .build();

        urlRepository.save(urlEntity);

        return Url.builder()
                .shortenedUrl(urlEntity.getShortenedUrl())
                .originalUrl(urlEntity.getOriginalUrl())
                .build();
    }


    public Url shortenUrl(final String url) {
        return shortenUrl(url, null);
    }

    public void validateCustomUrl(final String custom) {
        final var urlHash = urlRepository.findByShortenedUrl(custom);
        urlHash.ifPresent(urlEntity -> {
            throw new CustomUrlAlreadyExistsException(urlEntity.getShortenedUrl());
        });
    }

    private void validateUrl(final String url) {
        final var pattern = Pattern.compile(REGEX_PATTERN);
        final var matcher = pattern.matcher(url);
        if (!matcher.find()) {
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
