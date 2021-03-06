package com.webchat.models;



import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role extends Model {

    @Enumerated(EnumType.STRING)
    @Column(name = "title", length = 25, insertable = false, updatable = false)
    private RoleList title;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Role() {
    }

    public Role(long id) {
        super(id);
    }

    public RoleList getTitle() {
        return title;
    }

    public void setTitle(RoleList title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
