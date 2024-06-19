package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Entity
public class Task {
    @Id
    private Integer id;
    private String heading;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate dateOfCompletion;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Task() {
    }
    public Task(int id, String heading, String description, LocalDate dateOfCompletion, User user, Status status) {
        this.id = id;
        this.heading = heading;
        this.description = description;
        this.user = user;
        this.dateOfCompletion = dateOfCompletion;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(int id) {
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

    public int getId() {
        return id;
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
