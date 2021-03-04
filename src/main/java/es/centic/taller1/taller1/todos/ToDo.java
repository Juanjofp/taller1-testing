package es.centic.taller1.taller1.todos;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.centic.taller1.taller1.todolists.ToDoList;

@Entity
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private ToDoList list;

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

