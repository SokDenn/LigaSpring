package org.example.repos;

import org.example.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepo extends CrudRepository<Project, UUID> {
    Optional<Project> findById(UUID projectId);

    List<Project> findByUsersId(UUID userId);

}
