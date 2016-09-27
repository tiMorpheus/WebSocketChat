package com.webchat.models;



import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.getAll", query = "SELECT u from User u"),
        @NamedQuery(name = "User.getByLogin", query = "SELECT u from User u where u.login=:login")
})
public class User extends Model{

/*    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;*/

    @Column(name = "login", unique = true, length = 20)
    private String login;

    @Column(name = "password", length = 20)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="user_role", joinColumns = @JoinColumn(name = "user_id", nullable = false, updatable = false),
            inverseJoinColumns=@JoinColumn(name ="role_id", nullable = false, updatable = false))
    private Set<Role> roles = new HashSet<>();

    public User(){

    }

    public User(String login, String password) {

        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", roles=" + roles +
                '}';
    }
}