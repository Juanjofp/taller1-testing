package es.centic.taller1.taller1.todos;

import java.util.List;

public class ListOfToDosResponseBody {
    private List<ToDo> todos;

    public ListOfToDosResponseBody(List<ToDo> todos) {
        this.todos = todos;
    }

    public List<ToDo> getTodos() {
        return todos;
    }

}
