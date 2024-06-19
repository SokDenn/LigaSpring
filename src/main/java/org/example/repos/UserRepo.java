package org.example.repos;

import org.example.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findById(Long userId);
}
