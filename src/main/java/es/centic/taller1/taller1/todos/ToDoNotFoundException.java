package es.centic.taller1.taller1.todos;

public class ToDoNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

    public ToDoNotFoundException(long toDoId) {
        super(String.format("ToDo with Id %d not found", toDoId));
    }
}
