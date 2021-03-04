package es.centic.taller1.taller1.todos;

public class ToDoResponseBody {
    private long todoId;
    private String description;

    public ToDoResponseBody(ToDo toDo) {
        this.todoId = toDo.getId();
        this.description = toDo.getDescription();
    }

    public long getTodoId() {
        return todoId;
    }
    public String getDescription() {
        return description;
    }
}

