package com.mycompany.app.service;

import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.exception.InvalidUrlException;
import com.mycompany.app.exception.ShortenUrlExistsException;
import com.mycompany.app.repository.DataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class URLShortenerServiceTest {

    @Autowired
    private URLShortenerService shortenerService;

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private DataRepository dataRepository;

    @MockitoBean
    private ShortUrlCodeGeneratorService urlCodeGeneratorService;

    @MockitoBean
    private URLValidatorService urlValidatorService;

    @Test
    void whenInvalidUrl_thenRaiseInvalidUrlException() {
        var invalidUrl = "https ://www.yourcompany.com/very/  long/url";

        when(urlValidatorService.checkUrlFormat(invalidUrl)).thenReturn(false);

        assertThrowsExactly(InvalidUrlException.class, () -> {
            Optional<ShortenUrl> result = shortenerService.createShortenUrl(invalidUrl);
        });

        verify(urlValidatorService, times(1)).checkUrlFormat(invalidUrl);
    }

    @Test
    void whenCreatedShortenUrlAlreadyAssignedToDifferentUrl_thenRaiseShortenUrlExistsException() {
        var originalLongUrl = "https://www.mycompany.com/very/long/url";
        var newLongUrl = "https://www.yourcompany.com/very/long/url";
        var expectedShortenUrl = "7auy7q";
        var savedShortenUrl = ShortenUrl.builder()
                .shortenUrl(expectedShortenUrl)
                .longUrl(originalLongUrl)
                .build();

        when(urlValidatorService.checkUrlFormat(newLongUrl)).thenReturn(true);
        when(dataRepository.findByLongUrl(newLongUrl)).thenReturn(null);
        when(urlCodeGeneratorService.generateUniqueCode(newLongUrl)).thenReturn(expectedShortenUrl);
        when(dataRepository.findByShortenUrl(expectedShortenUrl)).thenReturn(savedShortenUrl);

        assertThrowsExactly(ShortenUrlExistsException.class, () -> {
            Optional<ShortenUrl> result = shortenerService.createShortenUrl(newLongUrl);
        });

        verify(urlValidatorService, times(1)).checkUrlFormat(newLongUrl);
        verify(dataRepository, times(1)).findByLongUrl(newLongUrl);
        verify(urlCodeGeneratorService, times(1)).generateUniqueCode(newLongUrl);
        verify(dataRepository, times(1)).findByShortenUrl(expectedShortenUrl);
    }

    @Test
    void whenCreateShortenUrl_thenPersistToDbAndCacheResult() {
        var originalLongUrl = "https://www.mycompany.com/very/long/url";
        var expectedShortenUrl = "7auy7q";
        var savedShortenUrl = ShortenUrl.builder()
                .shortenUrl(expectedShortenUrl)
                .longUrl(originalLongUrl)
                .build();

        when(urlValidatorService.checkUrlFormat(originalLongUrl)).thenReturn(true);
        when(urlCodeGeneratorService.generateUniqueCode(originalLongUrl)).thenReturn(expectedShortenUrl);
        when(dataRepository.save(any(ShortenUrl.class))).thenReturn(savedShortenUrl);

        Optional<ShortenUrl> result = shortenerService.createShortenUrl(originalLongUrl);

        verify(dataRepository, times(1)).save(any(ShortenUrl.class));
        assertThat(result.get().getShortenUrl()).isEqualTo(expectedShortenUrl);

        ShortenUrl cachedUrl = cacheManager.getCache("shortenUrls").get(expectedShortenUrl, ShortenUrl.class);
        assertThat(cachedUrl.getLongUrl()).isEqualTo(originalLongUrl);
    }

    @Test
    void whenGetShortenUrl_thenCacheIsUsedAndNoDbInteraction() {
        var shortenUrlCode = "abc123";
        var expectedOriginalUrl = "https://www.mycompany.com/another/long/path";
        var savedShortenUrl = ShortenUrl.builder()
                .shortenUrl(shortenUrlCode)
                .longUrl(expectedOriginalUrl)
                .build();

        cacheManager.getCache("shortenUrls").put(shortenUrlCode, savedShortenUrl);

        when(dataRepository.findByShortenUrl(shortenUrlCode)).thenReturn(savedShortenUrl);

        var result = shortenerService.getShortenUrl(shortenUrlCode);

        verify(dataRepository, never()).findByShortenUrl(shortenUrlCode);
        assertThat(result.getLongUrl()).isSameAs(expectedOriginalUrl);
    }
}