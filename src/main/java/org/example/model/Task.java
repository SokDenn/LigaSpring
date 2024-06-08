package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String heading;
    private String description;
    private Integer userId;
    private LocalDate dateOfCompletion;
    private Status status;

    public Task(int id, String heading, String description, LocalDate dateOfCompletion, Integer userId, Status status) {
        this.id = id;
        this.heading = heading;
        this.description = description;
        this.userId = userId;
        this.dateOfCompletion = dateOfCompletion;
        this.status = status;
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

    public Integer getUserId() {
        return userId;
    }

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return this.id + "," + this.heading + "," + this.description + "," + this.userId + ","
                + this.dateOfCompletion.format(formatter) + "," + status + "\n";
    }
}
