package es.centic.taller1.taller1.todolists;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@RestController
public class ToDoListController {

    @Autowired
    private ToDoListService toDoListService;
    
    @GetMapping(value="/lists", produces = MediaType.APPLICATION_JSON_VALUE)
    public String showAllLists() {
        return "[]";
    }

    @PostMapping(value="/lists", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ToDoListResponseBody createNewList(@RequestBody ToDoListRequestBody body) {
        ToDoList toDoList = toDoListService.createToDoList(body.getTitle());
        return new ToDoListResponseBody(toDoList);
    }
}
