package es.centic.taller1.taller1.todos;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import es.centic.taller1.taller1.todolists.ToDoList;
import es.centic.taller1.taller1.users.User;
import es.centic.taller1.taller1.users.UserRepository;

import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ToDoServiceUnitTests {
    @Autowired
    private ToDoService todoService;

    @MockBean
    private ToDoRepository todoRepository;

    @MockBean
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<ToDo> todoCaptor;

    @Test
    void addUserSkipUsersAlreadyAssociated() throws Exception {
        // Arrange
        User juanjo = new User("juanjo@centic", "juanjo Franco");
        User joaquin = new User("joaquin@centic", "Joaquin Lasheras");
        User alicia = new User("alicia@centic", "Alicia Garcia");
        List<User> usersInToDo = Arrays.asList(juanjo, joaquin);
        List<String> usersToAsociate = Arrays.asList("juanjo@centic", "alicia@centic");
        ToDoList list = Mockito.mock(ToDoList.class);
        String todoDescription = "My first ToDo";
        ToDo todo = new ToDo(list, todoDescription);
        todo.addUsers(usersInToDo);
        Optional<ToDo> todoFound = Optional.of(todo);

        given(userRepository.findAllById(usersToAsociate)).willReturn(Arrays.asList(juanjo, alicia));
        given(todoRepository.findById(todo.getId())).willReturn(todoFound);
        
        // Act
        todoService.addUsers(todo.getId(), usersToAsociate);

        // Assert
        Mockito.verify(todoRepository).save(todoCaptor.capture());
        assertThat(todoCaptor.getAllValues().size()).isEqualTo(1);
        ToDo todoSaved = todoCaptor.getValue();
        assertThat(todoSaved.getUsers()).hasSize(3);
        assertThat(todoSaved.getUsers()).contains(juanjo, joaquin, alicia);
    }

    @Test
    void addUserSkipInvalidUsersAndAssociateValidOnes() throws Exception {
        // Arrange
        User juanjo = new User("juanjo@centic", "juanjo Franco");
        User joaquin = new User("joaquin@centic", "Joaquin Lasheras");
        User alicia = new User("alicia@centic", "Alicia Garcia");
        List<User> usersInToDo = Arrays.asList(juanjo, joaquin);
        List<String> usersToAsociate = Arrays.asList("juanjo@centic", "alicia@centic", "pedro@arques");
        ToDoList list = Mockito.mock(ToDoList.class);
        String todoDescription = "My first ToDo";
        ToDo todo = new ToDo(list, todoDescription);
        todo.addUsers(usersInToDo);
        Optional<ToDo> todoFound = Optional.of(todo);

        given(userRepository.findAllById(usersToAsociate)).willReturn(Arrays.asList(juanjo, alicia));
        given(todoRepository.findById(todo.getId())).willReturn(todoFound);
        
        // Act
        todoService.addUsers(todo.getId(), usersToAsociate);

        // Assert
        Mockito.verify(todoRepository).save(todoCaptor.capture());
        assertThat(todoCaptor.getAllValues().size()).isEqualTo(1);
        ToDo todoSaved = todoCaptor.getValue();
        assertThat(todoSaved.getUsers()).hasSize(3);
        assertThat(todoSaved.getUsers()).contains(juanjo, joaquin, alicia);
    }
}
