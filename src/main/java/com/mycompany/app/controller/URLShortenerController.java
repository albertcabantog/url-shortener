package com.mycompany.app.controller;

import com.mycompany.app.repository.DataRepository;
import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.service.URLShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/urls")
@Tag(name = "URLShortenerController", description = "Service for URL shortener")
public class URLShortenerController {

    @Autowired
    private URLShortenerService shortenerService;

    @Operation(description = "Get the shortened URL")
    @GetMapping("/{shortenUrl}")
    public ResponseEntity<ShortenUrl> getShortenUrl(@PathVariable String shortenUrl) {
        ShortenUrl url = shortenerService.getShortenUrl(shortenUrl);
        return Optional.ofNullable(url).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(description = "Generate the shorten URL")
    @PostMapping
    public ResponseEntity<ShortenUrl> createShortenUrl(@RequestParam String longUrl) throws Exception {
        Optional<ShortenUrl> shortenUrl = shortenerService.createShortenUrl(longUrl);
        return ResponseEntity.ok(shortenUrl.get());
    }

}
