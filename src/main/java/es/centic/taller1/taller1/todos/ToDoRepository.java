package es.centic.taller1.taller1.todos;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.centic.taller1.taller1.todolists.ToDoList;


public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    List<ToDo> findByList(ToDoList list);
}

