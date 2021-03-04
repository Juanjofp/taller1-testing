package es.centic.taller1.taller1.todolists;

public class ToDoListResponseBody {
    private long listId;
    private String title;
    private int wip;

    public ToDoListResponseBody(ToDoList toDoList) {
        this.listId = toDoList.getId();
        this.title = toDoList.getTitle();
        this.wip = toDoList.getWip();
    }

    public long getListId() {
        return listId;
    }

    public String getTitle() {
        return title;
    }

    public int getWip() {
        return wip;
    }
}

