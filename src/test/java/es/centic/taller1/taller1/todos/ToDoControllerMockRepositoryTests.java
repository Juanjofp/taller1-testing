package es.centic.taller1.taller1.todos;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import es.centic.taller1.taller1.todolists.ToDoList;
import es.centic.taller1.taller1.todolists.ToDoListRepository;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ToDoControllerMockRepositoryTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoListRepository todoListRepository;

    @MockBean
    private ToDoRepository todoRepository;

    // HU: Quiero poder crear Tareas en una lista existente
    
    @Test
    void controllerReturn503WhenRepositoryFailsSavingANewToDo() throws Exception {
        // Arrange
        ToDoList list = new ToDoList("First List");
        list.setId(1L);
        Optional<ToDoList> optList = Optional.of(list);
        JSONObject body = new JSONObject();
        body.put("description", "First Todo");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists/{idList}/todos", list.getId().toString())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .content(body.toString());
        given(todoListRepository.findById(list.getId())).willReturn(optList);
        given(todoRepository.save(any(ToDo.class))).willThrow(new RuntimeException("Service unavailable, check later"));

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isServiceUnavailable())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("Service unavailable, check later")
            );
    }
    
    // HU: Quiero ver las tareas que tiene una lista 

    @Test
    void controllerReturn503WhenRepositoryFailsRetriveToDos() throws Exception {
        // Arrange
        ToDoList list = new ToDoList("First List");
        list.setId(1L);
        Optional<ToDoList> optList = Optional.of(list);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists/{idList}/todos", list.getId().toString());
        given(todoListRepository.findById(list.getId())).willReturn(optList);
        given(todoRepository.findByList(list)).willThrow(new RuntimeException("Service unavailable, check later"));

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isServiceUnavailable())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("Service unavailable, check later")
            );
    }

    @Test
    void controllerReturn503WhenRepositoryFailsRetriveList() throws Exception {
        // Arrange
        ToDoList list = new ToDoList("First List");
        list.setId(1L);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists/{idList}/todos", list.getId().toString());
        given(todoListRepository.findById(list.getId())).willThrow(new RuntimeException("Service unavailable, check later"));

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isServiceUnavailable())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("Service unavailable, check later")
            );
    }

    // HU: Quiero poder asociar usuarios a tareas

    @Test
    void controllerReturn503WhenRepositoryFailsAddingUser() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists/1/todos/1/users")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content("[\"juanjo@centic\"]");
        given(todoRepository.findById(1L)).willThrow(new RuntimeException("Service unavailable, check later"));

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isServiceUnavailable())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("Service unavailable, check later")
            );
    }
}
