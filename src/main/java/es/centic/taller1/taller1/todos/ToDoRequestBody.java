package es.centic.taller1.taller1.todos;

public class ToDoRequestBody {
    private String description;

    public ToDoRequestBody() {}

    public ToDoRequestBody(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
