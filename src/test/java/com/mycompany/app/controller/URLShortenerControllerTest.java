package com.mycompany.app.controller;

import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.exception.InvalidUrlException;
import com.mycompany.app.exception.ShortenUrlExistsException;
import com.mycompany.app.service.URLShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class URLShortenerControllerTest {

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
    void givenShortenUrl_whenGettingAnExistingUrl_shouldReturnOk() throws Exception {
        var shortenUrlCode = "test-shorten-url";
        var longUrl = "this-is-a-long-url";
        ShortenUrl shortenUrl = ShortenUrl.builder()
                .id(1L)
                .shortenUrl(shortenUrlCode)
                .longUrl(longUrl)
                .build();

        when(shortenerService.getShortenUrl(shortenUrlCode)).thenReturn(shortenUrl);

        mockMvc.perform(get("/api/urls/test-shorten-url"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortenUrl").value(shortenUrlCode))
                .andExpect(jsonPath("$.longUrl").value(longUrl));
    }

    @Test
    void givenShortenUrl_whenGettingNonExistingUrl_shouldReturnNotFound() throws Exception {
        when(shortenerService.getShortenUrl("test-shorten-url")).thenReturn(null);

        mockMvc.perform(get("/api/urls/test-shorten-url"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenInvalidLongUrl_whenCreatingShortenUrl_shouldThrowIllegalArgumentException() throws Exception {
        String invalidUrl = "httpx: //wwww.invalid.url";
        when(shortenerService.createShortenUrl(invalidUrl))
                .thenThrow(InvalidUrlException.class);

        mockMvc.perform(post("/api/urls").param("longUrl", invalidUrl))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(InvalidUrlException.class, result.getResolvedException()));
    }

    @Test
    void givenUrl_whenShortenUrlHasDifferentOriginalUrl_shouldThrowShortenUrlExistsException() throws Exception {
        String newUrl = "https://wwww.mycompany.url";
        when(shortenerService.createShortenUrl(newUrl))
                .thenThrow(ShortenUrlExistsException.class);

        mockMvc.perform(post("/api/urls").param("longUrl", newUrl))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertInstanceOf(ShortenUrlExistsException.class, result.getResolvedException()));
    }
}