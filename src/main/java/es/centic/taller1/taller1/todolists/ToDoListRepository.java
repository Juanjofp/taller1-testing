package es.centic.taller1.taller1.todolists;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ToDoListRepository extends CrudRepository<ToDoList, Long> {
    List<ToDoList> findAll();
}
