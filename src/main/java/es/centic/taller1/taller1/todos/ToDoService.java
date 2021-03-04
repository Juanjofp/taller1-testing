package es.centic.taller1.taller1.todos;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.centic.taller1.taller1.todolists.ToDoList;
import es.centic.taller1.taller1.todolists.ToDoListRepository;


@Service
public class ToDoService {

    @Autowired
    private ToDoRepository todoRepository;

    @Autowired
    private ToDoListRepository todoListRepository;

    private ToDo createToDo(ToDoList list, String description) {
        return new ToDo(list, description);
    }

    public ToDo addNewToDo(long listId, String description) throws ToDoListNotFoundException {
        Optional<ToDoList> listFound = todoListRepository.findById(listId);
        if(!listFound.isPresent()) {
            throw new ToDoListNotFoundException(listId);
        }
        ToDoList list = listFound.get();
        return todoRepository.save(createToDo(list, description));
        
    }
}
