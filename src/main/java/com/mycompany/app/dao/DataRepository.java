package com.mycompany.app.dao;

import com.mycompany.app.domain.ShortenUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<ShortenUrl, Long> {

    ShortenUrl findByShortenUrl(String shortenUrl);
}
