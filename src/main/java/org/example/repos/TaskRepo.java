package org.example.repos;

import org.example.model.Status;
import org.example.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepo extends CrudRepository<Task, Long> {
    List<Task> findByUserIdAndStatus(Long userId, Status status);
    List<Task> findByUserId(Long userId);
    List<Task> findByStatus(Status status);
}
