package org.eldron.shorty.service;

import org.eldron.shorty.exception.InvalidUrlException;
import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.hash.UrlHash;
import org.eldron.shorty.repository.UrlRepository;
import org.eldron.shorty.vo.Url;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UrlService urlService;

    @Test
    public void whenUrlExists_thenGetOriginalUrl() throws UrlNotFoundException {
        final var urlId = "urlid";
        final var originalUrl = "www.google.com";
        when(urlRepository.findById(urlId))
                .thenReturn(Optional.of(UrlHash.builder()
                        .id(urlId)
                        .originalUrl(originalUrl)
                        .build()));

        final Url url = urlService.getOriginalUrl(urlId);

        assertThat(url).isNotNull();
        assertThat(url.getOriginalUrl()).isNotNull();
        assertThat(url.getOriginalUrl()).isEqualTo(originalUrl);
    }

    @Test(expected = UrlNotFoundException.class)
    public void whenUrlDoesntExist_thenThrowException() throws UrlNotFoundException {
        final var urlId = "urlid";
        when(urlRepository.findById(urlId))
                .thenReturn(Optional.empty());

        urlService.getOriginalUrl(urlId);
    }

    @Test
    public void shortenUrl_validUrlThenSaveUrl() throws Exception {
        final var url = "http://www.google.com";
        when(restTemplate.getForObject(url, String.class))
                .thenReturn("");
        when(urlRepository.save(any(UrlHash.class)))
                .thenReturn(eq(any(UrlHash.class)));

        final Url shortenedUrl = urlService.shortenUrl(url);

        assertThat(shortenedUrl).isNotNull();
        assertThat(shortenedUrl.getOriginalUrl()).isEqualTo(url);
        assertThat(shortenedUrl.getId().length()).isEqualTo(10);
    }

    @Test(expected = InvalidUrlException.class)
    public void shortenUrl_invalidUrlThenSaveUrl() throws Exception {
        final var url = "www.google.com";

        urlService.shortenUrl(url);

        verify(restTemplate, times(0)).getForObject(url, String.class);
        verify(urlRepository, times(0)).save(any(UrlHash.class));
    }

    @Test(expected = InvalidUrlException.class)
    public void shortenUrl_invalidRestTemplateUrlThenSaveUrl() throws Exception {
        final var url = "http://www.google.com";
        when(restTemplate.getForObject(url, String.class))
                .thenThrow(RestClientException.class);

        urlService.shortenUrl(url);

        verify(restTemplate, times(1)).getForObject(url, String.class);
        verify(urlRepository, times(0)).save(any(UrlHash.class));
    }

}
