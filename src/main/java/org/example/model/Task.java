package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.dto.TaskDTO;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    private UUID id;

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
        this.heading = taskDTO.getHeading();
        this.description = taskDTO.getDescription();
        this.dateOfCompletion = taskDTO.getDateOfCompletion();
        this.status = taskDTO.getStatus();
        this.user = taskDTO.getUser();
        this.project = taskDTO.getProject();
    }

    public String getDateStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = dateOfCompletion.format(formatter);

        return formattedDate;
    }
}
