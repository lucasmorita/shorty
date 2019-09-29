package org.eldron.shorty.controller;

import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.repository.UrlRepository;
import org.eldron.shorty.service.UrlService;
import org.eldron.shorty.vo.Url;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlControllerTest {
    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    @Test
    public void whenRequestUrlThatDoesntExist_thenReturnNotFound() throws UrlNotFoundException {
        final var shortenedUrl = "abcd";
        when(urlService.getOriginalUrl(shortenedUrl)).thenThrow(UrlNotFoundException.class);

        final ResponseEntity response = urlController.getOriginalUrl(shortenedUrl);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenRequestUrlExists_thenReturnOriginalUrl() throws UrlNotFoundException {
        final var shortenedUrl = "abcd";
        final var originalUrl = "www.google.com";
        final var url = Url.builder()
                .id(shortenedUrl)
                .originalUrl(originalUrl)
                .build();
        when(urlService.getOriginalUrl(shortenedUrl)).thenReturn(url);

        final ResponseEntity response = urlController.getOriginalUrl(shortenedUrl);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
