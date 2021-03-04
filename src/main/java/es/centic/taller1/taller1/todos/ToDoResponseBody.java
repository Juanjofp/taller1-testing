package es.centic.taller1.taller1.todos;

import java.util.List;
import java.util.stream.Collectors;

import es.centic.taller1.taller1.users.UserBodyResponse;

public class ToDoResponseBody {
    private long todoId;
    private String description;
    private List<UserBodyResponse> users;

    public ToDoResponseBody(ToDo toDo) {
        this.todoId = toDo.getId();
        this.description = toDo.getDescription();
        this.users = toDo.getUsers().stream().map(user -> new UserBodyResponse(user)).collect(Collectors.toList());
    }

    public long getTodoId() {
        return todoId;
    }
    public String getDescription() {
        return description;
    }

    public List<UserBodyResponse> getUsers() {
        return users;
    }

}


