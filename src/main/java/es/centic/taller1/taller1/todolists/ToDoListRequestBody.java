package es.centic.taller1.taller1.todolists;

public class ToDoListRequestBody {
    private String title;
    private int wip;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWip() {
        return wip;
    }

    public void setWip(int wip) {
        this.wip = wip;
    }
}

