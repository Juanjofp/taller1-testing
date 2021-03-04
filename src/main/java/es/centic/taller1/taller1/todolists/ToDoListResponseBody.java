package es.centic.taller1.taller1.todolists;

public class ToDoListResponseBody {
    private long listId;
    private String title;

    public ToDoListResponseBody(ToDoList toDoList) {
        this.listId = toDoList.getId();
        this.title = toDoList.getTitle();
    }

    public long getListId() {
        return listId;
    }

    public String getTitle() {
        return title;
    }
}
