package org.eldron.shorty.repository;

import org.eldron.shorty.entity.UrlEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<UrlEntity, String> {
    Optional<UrlEntity> findByShortenedUrl(final String shortenedUrl);
}
