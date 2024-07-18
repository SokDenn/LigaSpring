package org.example.repos;

import org.example.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepo extends CrudRepository<Role, UUID> {
    Optional<Role> findById(UUID userId);
    Optional<Role> findByName(String roleName);
}
