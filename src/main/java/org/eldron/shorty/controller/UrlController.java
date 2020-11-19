package org.eldron.shorty.controller;

import org.eldron.shorty.service.UrlService;
import org.eldron.shorty.vo.Url;
import org.eldron.shorty.vo.request.ShortenUrlRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(final UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortenedUrlId}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable("shortenedUrlId") final String shortenedUrlId) {
        final var url = urlService.getOriginalUrl(shortenedUrlId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url.getOriginalUrl())
                .build();
    }

    @GetMapping("/shorten/{shortenedUrlId}")
    public ResponseEntity<Url> getOriginalUrl(@PathVariable("shortenedUrlId") final String shortenedUrlId) {
        final var url = urlService.getOriginalUrl(shortenedUrlId);
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }

    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestBody final ShortenUrlRequest shortenUrlRequest) {
        final var shortenedUrl = urlService.shortenUrl(shortenUrlRequest.getUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(shortenedUrl);
    }
}
