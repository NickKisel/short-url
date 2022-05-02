package tech.avito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.avito.model.Url;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByHash(String shorterUrl);
}
