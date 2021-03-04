package es.centic.taller1.taller1.todos;

import es.centic.taller1.taller1.todolists.ToDoList;

public class ToDoMaxWIPReachedException extends Exception {
	private static final long serialVersionUID = 1L;

	public ToDoMaxWIPReachedException(ToDoList list) {
        super(String.format("Max WIP(%d) reached in ToDoList %d", list.getWip(), list.getId()));
    }
}
