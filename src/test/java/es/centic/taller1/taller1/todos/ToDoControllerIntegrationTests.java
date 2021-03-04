package es.centic.taller1.taller1.todos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import es.centic.taller1.taller1.todolists.ToDoList;
import es.centic.taller1.taller1.todolists.ToDoListRepository;
import es.centic.taller1.taller1.users.User;
import es.centic.taller1.taller1.users.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class ToDoControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoRepository todoRepository;

    @Autowired
    private ToDoListRepository todoListRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUpTests() {
        todoRepository.deleteAll();
        todoListRepository.deleteAll();
        userRepository.deleteAll();
    }

    // HU: Quiero poder crear Tareas en una lista existente
    
    @Test
    void checkToDoControllerPath() throws Exception{
        // Arrange
        ToDoList list = todoListRepository.save(new ToDoList("First List"));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists/{idList}/todos", list.getId().toString());

        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createNewToDoWithDdescriptionInAList() throws Exception {
        // Arrange
        ToDoList list = todoListRepository.save(new ToDoList("First List"));
        JSONObject body = new JSONObject();
        body.put("description", "First Todo");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists/{idList}/todos", list.getId().toString())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .content(body.toString());
        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(MockMvcResultMatchers.jsonPath("$.todoId").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("First Todo"));
    }

    @Test
    void fails404WhenListIsNotPresentCreatingToDo() throws Exception {
        // Arrange
        JSONObject body = new JSONObject();
        body.put("description", "First Todo");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists/{idList}/todos", "1")
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .content(body.toString());
        // Act
        mockMvc.perform(request)
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            result -> assertThat(result.getResolvedException().getMessage()).contains("List with Id 1 not found")
        );
    }



    // HU: Quiero ver las tareas que tiene una lista 

    @Test
    void returnAnEmptyListIfNoToDoInTheList() throws Exception {
        // Arrange
        ToDoList list = todoListRepository.save(new ToDoList("First List"));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists/{idList}/todos", list.getId().toString())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE);
        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.todos").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.todos").isEmpty());
    }

    @Test
    void returnAllToDosInTheList() throws Exception {
        // Arrange
        ToDoList list = todoListRepository.save(new ToDoList("First List"));
        ArrayList<ToDo> todos = new ArrayList<>();
        todos.add(new ToDo(list, "First ToDo"));
        todos.add(new ToDo(list, "Second ToDo"));
        todoRepository.saveAll(todos);
        ListOfToDosResponseBody expectedResponse = new ListOfToDosResponseBody(todos);
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(expectedResponse);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists/{idList}/todos", list.getId().toString())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE);
        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.todos").isArray())
            .andExpect(MockMvcResultMatchers.content().json(expectedBody));
    }

    @Test
    void fails404WhenListIsNotPresentListing() throws Exception {
        // Arrange
        JSONObject body = new JSONObject();
        body.put("description", "First Todo");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists/{idList}/todos", "1")
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .content(body.toString());
        // Act
        mockMvc.perform(request)
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            result -> assertThat(result.getResolvedException().getMessage()).contains("List with Id 1 not found")
        );
    }

    // HU: Quiero crear Tareas con descripción por defecto
    @Test
    void createNewToDoWithoutDescriptionInAList() throws Exception {
        // Arrange
        ToDoList list = todoListRepository.save(new ToDoList("First List"));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post("/lists/{idList}/todos", list.getId().toString())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE);
        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(MockMvcResultMatchers.jsonPath("$.todoId").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Add description"));
    }


    // HU: Quiero poder asociar usuarios a tareas

    @Test
    void AddTwoUserToAToDo() throws Exception {
        // Arrange
        ToDoList list = todoListRepository.save(new ToDoList("First List"));
        ToDo todo = todoRepository.save(new ToDo(list, "First ToDo"));
        User userOne = userRepository.save(new User("juanjo@centic", "Juanjo Franco"));
        User userTwo = userRepository.save(new User("joaquin@centic", "Joaquin Lasheras"));
        ArrayList<String> usersExpected = new ArrayList<>();
        usersExpected.add(userOne.getUsername());
        usersExpected.add(userTwo.getUsername());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post(
                                                        "/lists/{idList}/todos/{idTodo}/users",
                                                        list.getId().toString(),
                                                        Long.toString(todo.getId())
                                                    )
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .content(
                                                        String.format(
                                                            "[\"%s\", \"%s\"]",
                                                            userOne.getUsername(),
                                                            userTwo.getUsername()
                                                        )
                                                    );
        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(MockMvcResultMatchers.jsonPath("$.todoId").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("First ToDo"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.users").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.users", hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.users[*].username", hasItems("joaquin@centic", "juanjo@centic")));
    }

    @Test
    void fails404WhenNoToDoPresent() throws Exception {
        // Arrange
        User userOne = userRepository.save(new User("juanjo@centic", "Juanjo Franco"));
        User userTwo = userRepository.save(new User("joaquin@centic", "Joaquin Lasheras"));
        ArrayList<String> usersExpected = new ArrayList<>();
        usersExpected.add(userOne.getUsername());
        usersExpected.add(userTwo.getUsername());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                                    .post("/lists/1/todos/1/users")
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .content(String.format("[\"%s\", \"%s\"]", userOne.getUsername(), userTwo.getUsername()));
        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("ToDo with Id 1 not found")
            );
    }

    @Test
    void fails409WhenReachWIP() throws Exception {
        // Arrange
        ToDoList list = todoListRepository.save(new ToDoList("First List", 2));
        JSONObject body = new JSONObject();
        body.put("description", "Third Todo");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists/{idList}/todos", list.getId().toString())
                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                    .content(body.toString());
        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("Max WIP(2) reached in ToDoList")
            );
    }    
}

