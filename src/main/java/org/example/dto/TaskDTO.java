package org.example.dto;

import lombok.Data;
import org.example.model.Project;
import org.example.model.Status;
import org.example.model.User;

import java.time.LocalDate;
import java.util.UUID;
@Data
public class TaskDTO {
    private UUID taskId;
    private String heading;
    private String description;
    private String dateOfCompletionStr;
    private Status status;
    private UUID userId;
    private UUID projectId;
    private LocalDate dateOfCompletion;
    private User user;
    private Project project;

    public TaskDTO() {}

}
