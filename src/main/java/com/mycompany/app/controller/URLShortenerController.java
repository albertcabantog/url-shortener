package com.mycompany.app.controller;

import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.service.URLShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/urls")
@Tag(name = "URLShortenerController", description = "Service for URL shortener")
public class URLShortenerController {

    @Autowired
    private URLShortenerService shortenerService;

    @Operation(description = "Get the shortened URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Short URL code not found")
    })
    @GetMapping("/{shortenUrl}")
    public ResponseEntity<ShortenUrl> getShortenUrl(@PathVariable String shortenUrl) {
        var url = shortenerService.getShortenUrl(shortenUrl);
        return Optional.ofNullable(url).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(description = "Generate the shorten URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid URL format"),
            @ApiResponse(responseCode = "500",
                    description = "Server encountered an error when retrieving intended URL"
            )
    })
    @PostMapping
    public ResponseEntity<ShortenUrl> createShortenUrl(@RequestParam String longUrl) throws Exception {
        Optional<ShortenUrl> shortenUrl = shortenerService.createShortenUrl(longUrl);
        return ResponseEntity.ok(shortenUrl.get());
    }

}
