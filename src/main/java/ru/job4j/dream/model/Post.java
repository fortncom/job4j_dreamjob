package ru.job4j.dream.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private int id;
    private String name;
    private String description = "";
    private Timestamp created;

    public Post(int id, String name) {
        this.id = id;
        this.name = name;
        this.created = Timestamp.valueOf(LocalDateTime.now());
    }

    public Post(int id, String name, Timestamp created) {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id
                && name.equals(post.name)
                && Objects.equals(description, post.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", created=" + created
                + '}';
    }
}
