package com.mycompany.app.controller;

import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.service.URLShortenerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {URLShortenerController.class})
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
        ShortenUrl shortenUrl = ShortenUrl.builder()
                .id(1L)
                .shortenUrl("test-shorten-url")
                .longUrl("this-is-a-long-url")
                .build();

        Mockito.when(shortenerService.getShortenUrl("test-shorten-url")).thenReturn(shortenUrl);

        mockMvc.perform(get("/api/urls/test-shorten-url"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortenUrl").value("test-shorten-url"))
                .andExpect(jsonPath("$.longUrl").value("this-is-a-long-url"));
    }

    @Test
    void givenShortenUrl_whenGettingNonExistingUrl_shouldReturnNotFound() throws Exception {
        Mockito.when(shortenerService.getShortenUrl("test-shorten-url")).thenReturn(null);

        mockMvc.perform(get("/api/urls/test-shorten-url"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenInvalidLongUrl_whenCreatingShortenUrl_shouldThrowIllegalArgumentException() throws Exception {
        Mockito.when(shortenerService.createShortenUrl("httpx: //wwww.invalid.url")).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post("/api/urls").param("longUrl", "httpx: //wwww.invalid.url")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertInstanceOf(IllegalArgumentException.class, result.getResolvedException()));
    }
}