package com.mycompany.app.controller;

import com.mycompany.app.exception.OriginalUrlNotFoundException;
import com.mycompany.app.service.URLShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class RedirectShortenUrlController {

    private final URLShortenerService shortenerService;

    public RedirectShortenUrlController(URLShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @GetMapping("/{shortenUrl}")
    public RedirectView redirectWithUsingRedirectView(@PathVariable String shortenUrl) {
        var url = shortenerService.getShortenUrl(shortenUrl);
        return Optional.ofNullable(url).map(shortenUrl1 -> new RedirectView(url.getLongUrl()))
                .orElseThrow(OriginalUrlNotFoundException::new);
    }
}
