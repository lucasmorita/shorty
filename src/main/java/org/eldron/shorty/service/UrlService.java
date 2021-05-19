package org.eldron.shorty.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.eldron.shorty.exception.InvalidUrlException;
import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.hash.UrlHash;
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
        final var url = urlRepository.findById(shortenedUrlId);

        if (url.isPresent()) {
            return Url.builder()
                    .shortenedUrl(url.get().getId())
                    .originalUrl(url.get().getOriginalUrl())
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
                .shortenedUrl(urlHash.getId())
                .originalUrl(urlHash.getOriginalUrl())
                .build();
    }

    /**
     *
     * @param requestedUrl the original url to save
     * @param customUrl the new url th
     * @return
     */
    public Url shortedCustomUrl(final String requestedUrl, final String customUrl) {
        validateUrl(requestedUrl);

        final var urlHash = UrlHash.builder()
                .id(customUrl)
                .originalUrl(requestedUrl)
                .build();

        urlRepository.save(urlHash);

        return Url.builder()
                .shortenedUrl(urlHash.getId())
                .originalUrl(urlHash.getOriginalUrl())
                .build();
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
