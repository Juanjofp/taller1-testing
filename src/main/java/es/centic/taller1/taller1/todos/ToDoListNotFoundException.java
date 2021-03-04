package es.centic.taller1.taller1.todos;

public class ToDoListNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public ToDoListNotFoundException(long listId) {
        super(String.format("ToDoList with Id %d not found", listId));
    }
}
