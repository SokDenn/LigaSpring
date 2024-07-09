package org.example.model;

import java.time.LocalDate;

public class TaskDTO {
    private Long taskId;
    private String heading;
    private String description;
    private String dateOfCompletionStr;
    private Status status;
    private Long userId;
    private Long projectId;
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


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
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
