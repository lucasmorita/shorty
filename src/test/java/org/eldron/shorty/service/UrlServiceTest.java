package org.eldron.shorty.service;

import org.eldron.shorty.entity.UrlEntity;
import org.eldron.shorty.exception.InvalidUrlException;
import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UrlService urlService;

    @Test
    void whenUrlExists_thenGetOriginalUrl() throws UrlNotFoundException {
        final var customUrl = "customUrl";
        final var originalUrl = "www.google.com";
        when(urlRepository.findByShortenedUrl(customUrl))
                .thenReturn(Optional.of(UrlEntity.builder()
                        .shortenedUrl(customUrl)
                        .originalUrl(originalUrl)
                        .build()));

        final var url = urlService.getOriginalUrl(customUrl);

        assertThat(url).isNotNull();
        assertThat(url.getOriginalUrl()).isNotNull();
        assertThat(url.getOriginalUrl()).isEqualTo(originalUrl);
    }

    @Test
    void whenUrlDoesntExist_thenThrowException() throws UrlNotFoundException {
        final var urlId = "urlid";
        when(urlRepository.findByShortenedUrl(urlId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(UrlNotFoundException.class).isThrownBy(() ->
                urlService.getOriginalUrl(urlId)
        );

        verify(urlRepository).findByShortenedUrl(urlId);
    }

    @Test
    void shortenUrl_validUrlThenSaveUrl() {
        final var url = "http://www.google.com";
        when(restTemplate.getForObject(url, String.class))
                .thenReturn("");
        when(urlRepository.save(any(UrlEntity.class)))
                .thenReturn(eq(any(UrlEntity.class)));

        final var shortenedUrl = urlService.shortenUrl(url);

        assertThat(shortenedUrl).isNotNull();
        assertThat(shortenedUrl.getOriginalUrl()).isEqualTo(url);
        assertThat(shortenedUrl.getShortenedUrl()).hasSize(10);
    }

    @Test
    void shortenUrl_invalidUrlThenSaveUrl() {
        final var url = "www.google.com";

        assertThatExceptionOfType(InvalidUrlException.class).isThrownBy(() ->
                urlService.shortenUrl(url)
        );

        verify(restTemplate, never()).getForObject(url, String.class);
        verify(urlRepository, never()).save(any(UrlEntity.class));
    }

    @Test
    void shortenUrl_invalidRestTemplateUrlThenSaveUrl() {
        final var url = "http://www.google.com";
        when(restTemplate.getForObject(url, String.class))
                .thenThrow(RestClientException.class);

        assertThatExceptionOfType(InvalidUrlException.class).isThrownBy(() ->
                urlService.shortenUrl(url)
        );

        verify(restTemplate).getForObject(url, String.class);
        verify(urlRepository, never()).save(any(UrlEntity.class));
    }

}
