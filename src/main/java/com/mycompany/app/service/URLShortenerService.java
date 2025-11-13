package com.mycompany.app.service;

import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.exception.InvalidUrlException;
import com.mycompany.app.exception.ShortenUrlExistsException;
import com.mycompany.app.repository.ShortenUrlRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class URLShortenerService {
    private final ShortenUrlRepository shortenUrlRepository;
    private final ShortUrlCodeGeneratorService shortUrlCodeGeneratorService;

    public URLShortenerService(ShortenUrlRepository shortenUrlRepository, ShortUrlCodeGeneratorService shortUrlCodeGeneratorService) {
        this.shortenUrlRepository = shortenUrlRepository;
        this.shortUrlCodeGeneratorService = shortUrlCodeGeneratorService;
    }

    @Cacheable(value = "shortenUrls", key = "#shortenUrl")
    public ShortenUrl getShortenUrl(String shortenUrl) {
        return shortenUrlRepository.findByShortenUrl(shortenUrl);
    }

    @CachePut(value = "shortenUrls", key = "#result.shortenUrl")
    public Optional<ShortenUrl> createShortenUrl(String originalUrl) {
        boolean checkUrl = shortUrlCodeGeneratorService.checkUrlFormat(originalUrl);
        if (! checkUrl) {
            throw new InvalidUrlException();
        }

        var shortenUrl = shortenUrlRepository.findByLongUrl(originalUrl);
        if (ObjectUtils.isNotEmpty(shortenUrl)) {
            return Optional.of(shortenUrl);
        }

        var shortenUrlCode = shortUrlCodeGeneratorService.generateUniqueCode();
        shortenUrl = shortenUrlRepository.findByShortenUrl(shortenUrlCode);
        if (ObjectUtils.isNotEmpty(shortenUrl)) {
            if (shortenUrl.getLongUrl().equals(originalUrl)) {
                return Optional.of(shortenUrl);
            }
            throw new ShortenUrlExistsException();
        }

        shortenUrl = ShortenUrl.builder()
                .longUrl(originalUrl)
                .shortenUrl(shortenUrlCode)
                .build();

        shortenUrlRepository.save(shortenUrl);

        return Optional.of(shortenUrl);
    }
}
