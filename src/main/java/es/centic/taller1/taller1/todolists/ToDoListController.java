package es.centic.taller1.taller1.todolists;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@RestController
public class ToDoListController {

    @Autowired
    private ToDoListService toDoListService;
    
    @GetMapping(value="/lists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListOfTodoListResponseBody showAllLists() {
        try {
            List<ToDoList> toDoListCollection = toDoListService.findAllLists();
            return new ListOfTodoListResponseBody(toDoListCollection);
        }
        catch(Exception exceptions) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable, check later", null);
        }
    }

    @PostMapping(value="/lists", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ToDoListResponseBody createNewList(@RequestBody(required = false) ToDoListRequestBody body) {
        if (body == null || body.getTitle() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required", null);
        }
        try { 
            ToDoList toDoList = toDoListService.createToDoList(body.getTitle(), body.getWip());
            return new ToDoListResponseBody(toDoList);
        }
        catch(Exception exceptions) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable, check later", null);
        }
    }
}
