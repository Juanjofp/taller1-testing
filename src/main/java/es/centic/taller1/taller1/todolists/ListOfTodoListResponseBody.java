package es.centic.taller1.taller1.todolists;

import java.util.List;

public class ListOfTodoListResponseBody {
    private List<ToDoList> lists;

    public ListOfTodoListResponseBody(List<ToDoList> toDoListCollection) {
        this.lists = toDoListCollection;
    }

    public List<ToDoList> getLists() {
        return lists;
    }
}
