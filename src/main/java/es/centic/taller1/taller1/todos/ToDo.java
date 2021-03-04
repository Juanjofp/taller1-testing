package es.centic.taller1.taller1.todos;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import es.centic.taller1.taller1.todolists.ToDoList;
import es.centic.taller1.taller1.users.User;

@Entity
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private ToDoList list;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    protected ToDo() {}

    public ToDo(ToDoList list, String description) {
        this.list = list;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void addUsers(List<User> users) {
        HashSet<User> filterDuplicated = new HashSet<>(this.users);
        filterDuplicated.addAll(users);
        this.users = new ArrayList<>(filterDuplicated);
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj) return true;
        // null check
        if (obj == null) return false;
        // type check and cast
        if (getClass() != obj.getClass()) return false;
        return this.id == ((ToDo)obj).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}

