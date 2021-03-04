package es.centic.taller1.taller1.todos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.centic.taller1.taller1.todolists.ToDoList;
import es.centic.taller1.taller1.todolists.ToDoListRepository;
import es.centic.taller1.taller1.users.User;
import es.centic.taller1.taller1.users.UserRepository;


@Service
public class ToDoService {

    @Autowired
    private ToDoRepository todoRepository;

    @Autowired
    private ToDoListRepository todoListRepository;

    @Autowired
    private UserRepository userRepository;

    private ToDo createToDo(ToDoList list, String description) {
        return new ToDo(list, description);
    }

    public ToDo addNewToDo(long listId, String description) throws ToDoListNotFoundException, ToDoMaxWIPReachedException {
        Optional<ToDoList> listFound = todoListRepository.findById(listId);
        if(!listFound.isPresent()) {
            throw new ToDoListNotFoundException(listId);
        }
        ToDoList list = listFound.get();
        if(list.getWip() >= todoRepository.countByList(list)) {
            throw new ToDoMaxWIPReachedException(list);
        }
        return todoRepository.save(createToDo(list, description));
        
    }

	public List<ToDo> showAll(long listId) throws ToDoListNotFoundException {
        Optional<ToDoList> list = todoListRepository.findById(listId);
        if(!list.isPresent()) {
            throw new ToDoListNotFoundException(listId);
        }
		return todoRepository.findByList(list.get());
	}

	public ToDo addUsers(long todoId, List<String> users) throws ToDoNotFoundException {
        List<User> usersFound = userRepository.findAllById(users);
        Optional<ToDo> todoFound = todoRepository.findById(todoId);
        if(!todoFound.isPresent()) {
            throw new ToDoNotFoundException(todoId);
        }
        ToDo todo = todoFound.get();
        todo.addUsers(usersFound);
        todoRepository.save(todo);
        return todo;
	}
}
