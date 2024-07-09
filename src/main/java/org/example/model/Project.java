package org.example.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Project {
    @Id
    private Long id;
    private String heading;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL) // Каскадное сохранение задач
    private Set<Task> tasks;

    public Project() {}
    public Project(Long id, String heading, String description) {
        this.id = id;
        this.heading = heading;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
