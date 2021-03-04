package es.centic.taller1.taller1.todolists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoListService {

    @Autowired
    private ToDoListRepository toDoListRepository;

    public ToDoList createToDoList(String title) {
        return toDoListRepository.save(new ToDoList(title));
    }
}
