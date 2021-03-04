package es.centic.taller1.taller1.todolists;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoListService {

    @Autowired
    private ToDoListRepository toDoListRepository;

    public ToDoList createToDoList(String title, int wip) {
        if(wip <= 0) wip = 0;
        return toDoListRepository.save(new ToDoList(title, wip));
    }

    public List<ToDoList> findAllLists() {
        return toDoListRepository.findAll();
    }
}
