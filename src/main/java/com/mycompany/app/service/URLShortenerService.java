package com.mycompany.app.service;

import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.exception.InvalidUrlException;
import com.mycompany.app.exception.ShortenUrlExistsException;
import com.mycompany.app.repository.DataRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class URLShortenerService {
    private final DataRepository dataRepository;
    private final URLValidatorService urlValidatorService;
    private final ShortUrlCodeGeneratorService shortUrlCodeGeneratorService;

    public URLShortenerService(DataRepository dataRepository, URLValidatorService urlValidatorService, ShortUrlCodeGeneratorService shortUrlCodeGeneratorService) {
        this.dataRepository = dataRepository;
        this.urlValidatorService = urlValidatorService;
        this.shortUrlCodeGeneratorService = shortUrlCodeGeneratorService;
    }

    @Cacheable(value = "shortenUrls", key = "#shortenUrl")
    public ShortenUrl getShortenUrl(String shortenUrl) {
        return dataRepository.findByShortenUrl(shortenUrl);
    }

    @CachePut(value = "shortenUrls", key = "#result.shortenUrl")
    public Optional<ShortenUrl> createShortenUrl(String originalUrl) {
        boolean checkUrl = urlValidatorService.checkUrlFormat(originalUrl);
        if (! checkUrl) {
            throw new InvalidUrlException();
        }

        var shortenUrl = dataRepository.findByLongUrl(originalUrl);
        if (ObjectUtils.isNotEmpty(shortenUrl)) {
            return Optional.of(shortenUrl);
        }

        var shortenUrlCode = shortUrlCodeGeneratorService.generateUniqueCode(originalUrl);
        shortenUrl = dataRepository.findByShortenUrl(shortenUrlCode);
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

        dataRepository.save(shortenUrl);

        return Optional.of(shortenUrl);
    }
}
