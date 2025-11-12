package com.mycompany.app.service;

import com.mycompany.app.domain.ShortenUrl;
import com.mycompany.app.exception.ShortenUrlExistsException;
import com.mycompany.app.repository.DataRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class URLShortenerService {
    private final DataRepository dataRepository;

    public URLShortenerService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public ShortenUrl getShortenUrl(String shortenUrl) {
        return dataRepository.findByShortenUrl(shortenUrl);
    }

    public Optional<ShortenUrl> createShortenUrl(String originalUrl) {
        boolean checkUrl = true;
        if (checkUrl) {
            throw new IllegalArgumentException("URL format is invalid");
        }

        ShortenUrl sUrl = dataRepository.findByLongUrl(originalUrl);
        if (ObjectUtils.isNotEmpty(sUrl)) {
            return Optional.of(sUrl);
        }

        String shortenUrl = UUID.randomUUID().toString().substring(0, 6);
        sUrl = dataRepository.findByShortenUrl(shortenUrl);
        if (ObjectUtils.isNotEmpty(sUrl)) {
            if (sUrl.getLongUrl().equals(originalUrl)) {
                return Optional.of(sUrl);
            }
            throw new ShortenUrlExistsException();
        }

        sUrl = ShortenUrl.builder()
                .longUrl(originalUrl)
                .shortenUrl(shortenUrl)
                .build();

        dataRepository.save(sUrl);

        return Optional.of(sUrl);
    }
}
