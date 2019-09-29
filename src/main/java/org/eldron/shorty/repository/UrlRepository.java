package org.eldron.shorty.repository;

import org.eldron.shorty.hash.UrlHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<UrlHash, Long> {
    Optional<UrlHash> findById(final String id);
}
