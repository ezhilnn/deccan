package io.deccan.controlplane.identity.repository;

import io.deccan.controlplane.identity.entity.User;
import io.deccan.controlplane.identity.enums.UserStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions"
    })
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions"
    })
    Optional<User> findByEmailAndStatus(
            String email,
            UserStatus status
    );

    boolean existsByEmail(String email);

}