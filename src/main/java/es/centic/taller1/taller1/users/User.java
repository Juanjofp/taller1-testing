package es.centic.taller1.taller1.users;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private String username;

    private String name;

    private String avatar;

    protected User() {}

    public User(String username, String name, String avatar) {
        this.name = name;
        this.username = username;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return String.format("User[name='%s', username='%s']", name, username);
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj) return true;
        // null check
        if (obj == null) return false;
        // type check and cast
        if (getClass() != obj.getClass()) return false;
        return this.username.compareTo(((User)obj).username) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }
}

