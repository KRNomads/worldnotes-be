package org.example.user.adapter.out;

import java.util.Optional;
import java.util.UUID;

import org.example.user.domain.entity.UserSocialToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersSocialTokenRepository extends JpaRepository<UserSocialToken, Long> {

    Optional<UserSocialToken> findByUserId(UUID userId);

}
