package es.centic.taller1.taller1.todos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ToDoController {

    private ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping(value = "/lists/{listId}/todos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListOfToDosResponseBody showToDos(@PathVariable(value="listId") long listId) {
        try {
            List<ToDo> todos = toDoService.showAll(listId);
            return new ListOfToDosResponseBody(todos);
        }
        catch(ToDoListNotFoundException tDNFException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, tDNFException.getMessage(), null);
        }
        catch(Exception exceptions) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable, check later", null);
        }
    }

    @PostMapping(value = "/lists/{listId}/todos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ToDoResponseBody createToDo(@PathVariable long listId, @RequestBody(required = false) ToDoRequestBody todo) {
        try {
            String description = (todo != null && todo.getDescription() != null) ? todo.getDescription() : "Add description";
            ToDo newToDo = toDoService.addNewToDo(listId, description);
            return new ToDoResponseBody(newToDo);
        }
        catch(ToDoListNotFoundException tDNFException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, tDNFException.getMessage(), null);
        }
        catch(Exception exceptions) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable, check later", null);
        }
    }

    @PostMapping(value = "/lists/{listId}/todos/{todoId}/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ToDoResponseBody addUsersInToDo(@PathVariable long listId, @PathVariable long todoId, @RequestBody List<String> users) {
        try {
            ToDo updatedToDo = toDoService.addUsers(todoId, users);
            return new ToDoResponseBody(updatedToDo);
        }
        catch(ToDoNotFoundException tDNFException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, tDNFException.getMessage(), null);
        }
        catch(Exception exceptions) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable, check later", null);
        }
    }
}

