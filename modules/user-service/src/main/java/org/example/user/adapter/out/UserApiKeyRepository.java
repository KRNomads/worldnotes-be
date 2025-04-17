package org.example.user.adapter.out;

import java.util.Optional;
import java.util.UUID;

import org.example.user.domain.entity.UserApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApiKeyRepository extends JpaRepository<UserApiKey, Long> {

    Optional<UserApiKey> findByUserId(UUID userId);

}
