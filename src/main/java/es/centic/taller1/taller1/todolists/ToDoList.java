package es.centic.taller1.taller1.todolists;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    protected ToDoList() {}

    public ToDoList(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("ToDoList[%d %s]", id, title);
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj) return true;
        // null check
        if (obj == null) return false;
        // type check and cast
        if (getClass() != obj.getClass()) return false;
        return this.id == ((ToDoList)obj).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}

