package org.eldron.shorty.controller;

import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.service.UrlService;
import org.eldron.shorty.vo.Url;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten")
public class UrlController {
    private final UrlService urlService;

    public UrlController(final UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortenedUrlId}")
    public ResponseEntity redirect(@PathVariable("shortenedUrlId") final String shortenedUrlId) {
        try {
            final Url url = urlService.getOriginalUrl(shortenedUrlId);
            return ResponseEntity.ok().body(url);
        } catch (final UrlNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity shortenUrl(@RequestBody final Url url) {
        return urlService.shortenUrl(url.getOriginalUrl());
    }
}
