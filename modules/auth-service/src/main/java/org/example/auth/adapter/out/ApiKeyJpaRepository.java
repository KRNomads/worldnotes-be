package org.example.auth.adapter.out;

import java.util.Optional;

import org.example.auth.domain.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyJpaRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findByKey(String key);

}
