package org.example.repos;

import org.example.model.Project;
import org.example.model.Task;
import org.example.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepo extends CrudRepository<Project, Long> {
    Optional<Project> findById(Integer projectId);
    List<Project> findByUsers_Id(Long userId);
}
