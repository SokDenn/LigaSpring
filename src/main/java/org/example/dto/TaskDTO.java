package org.example.dto;

import org.example.model.Project;
import org.example.model.Status;
import org.example.model.User;

import java.time.LocalDate;
import java.util.UUID;

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

    public String getDateOfCompletionStr() {
        return dateOfCompletionStr;
    }

    public void setDateOfCompletionStr(String dateOfCompletionStr) {
        this.dateOfCompletionStr = dateOfCompletionStr;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public LocalDate getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(LocalDate dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
