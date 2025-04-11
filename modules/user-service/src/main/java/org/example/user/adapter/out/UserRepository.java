package org.example.user.adapter.out;

import java.util.UUID;

import org.example.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

}
