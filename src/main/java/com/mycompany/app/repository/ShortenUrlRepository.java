package com.mycompany.app.repository;

import com.mycompany.app.domain.ShortenUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortenUrlRepository extends JpaRepository<ShortenUrl, Long> {

    ShortenUrl findByShortenUrl(String shortenUrl);
    ShortenUrl findByLongUrl(String longUrl);
}
