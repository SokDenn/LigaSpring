package org.example.repos;

import org.example.model.Status;
import org.example.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepo extends CrudRepository<Task, UUID> {
    List<Task> findByUserIdAndStatus(UUID userId, Status status);

    List<Task> findByUserId(UUID userId);

    List<Task> findByStatus(Status status);

    List<Task> findAllByOrderByDateOfCompletionDesc();
}
