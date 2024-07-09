package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Entity
public class Task {
    @Id
    private Long id;
    private String heading;
    private String description;
    private LocalDate dateOfCompletion;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Task() {
    }
    public Task(TaskDTO taskDTO) {
        this.id = taskDTO.getTaskId();
        this.heading = taskDTO.getHeading();
        this.description = taskDTO.getDescription();
        this.dateOfCompletion = taskDTO.getDateOfCompletion();
        this.status = taskDTO.getStatus();
        this.user = taskDTO.getUser();
        this.project = taskDTO.getProject();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateOfCompletion(LocalDate dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateOfCompletion(){return dateOfCompletion;}

    public String getDateStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = dateOfCompletion.format(formatter);

        return formattedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {this.status = status;}

    public String getFullInfoStr() {
        return this.id + "," + this.heading + "," + this.description + "," + this.user.getId() + ","
                + getDateStr() + "," + status + "\n";
    }
}
