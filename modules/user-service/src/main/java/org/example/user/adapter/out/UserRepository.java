package org.example.user.adapter.out;

import java.util.Optional;
import java.util.UUID;

import org.example.common.enums.SocialProvider;
import org.example.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // 특정 제공자와 제공자 ID로 Users 객체 반환
    Optional<User> findByProviderAndProviderId(SocialProvider provider, String providerId);

}
