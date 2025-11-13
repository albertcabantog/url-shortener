package com.mycompany.app.controller;

import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.service.URLShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class RedirectShortenUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private URLShortenerService shortenerService;

    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages = {"com.mycompany.app.controller"})
    static class ContextConfiguration {
    }

    @Test
    void givenValidShortenUrl_whenAccessingUrl_shouldRedirectToOriginalUrl() throws Exception {
        var shortUrl = "test-shorten-url";
        var shortenUrl = ShortenUrl.builder()
                .id(1L)
                .shortenUrl(shortUrl)
                .longUrl("this-is-a-long-url")
                .build();

        when(shortenerService.getShortenUrl(shortUrl)).thenReturn(shortenUrl);

        mockMvc.perform(get("/" + shortUrl))
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void givenInvalidShortenUrl_whenAccessingUrl_shouldResultNotFound() throws Exception {
        var shortUrl = "test-shorten-url";

        when(shortenerService.getShortenUrl(shortUrl)).thenReturn(null);

        mockMvc.perform(get("/" + shortUrl))
                .andExpect(status().isNotFound());
    }
}