package com.mycompany.app.controller;

import com.mycompany.app.dao.DataRepository;
import com.mycompany.app.domain.ShortenUrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
@Tag(name = "URLShortenerController", description = "Service for URL shortener")
public class URLShortenerController {

    @Autowired
    private DataRepository dataRepository;

    @Operation(description = "Get the shortened URL")
    @GetMapping("/{shortenUrl}")
    public ShortenUrl find(@PathVariable String shortenUrl) {
        return  dataRepository.findByShortenUrl(shortenUrl);
    }

    @Operation(description = "Generate the shorten URL")
    @PostMapping
    public ShortenUrl shorten(@RequestParam String longUrl) {
        ShortenUrl url = ShortenUrl.builder()
                .longUrl(longUrl)
                .shortenUrl(longUrl)
                .build();
        return dataRepository.save(url);
    }

}
