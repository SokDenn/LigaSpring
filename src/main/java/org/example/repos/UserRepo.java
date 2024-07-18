package org.example.repos;

import org.example.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends CrudRepository<User, UUID> {
    Optional<User> findById(UUID userId);
    Optional<User> findByUsername(String username);
}
