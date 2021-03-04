package es.centic.taller1.taller1.todolists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;


@SpringBootTest
@AutoConfigureMockMvc
class ToDoLisControllertIntegrationTests {
    @Autowired
    private ToDoListController toDoListController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoListRepository toDoListRepository;

    @BeforeEach
    void setUpTests() {
        toDoListRepository.deleteAll();
    }

    // HU: Quiero poder crear listas con un título
    @Test
    void checkToDoListController() {
        assertThat(toDoListController).isNotNull();
    }

    @Test
    void checkToDoListControllerIsItsPath() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists");

        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createANewListWithATitle() throws Exception {
        // Arrange
        JSONObject body = new JSONObject();
        body.put("title", "My First List");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists")
                                                    .content(body.toString())
                                                    .header("content-type", "application/json");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(MockMvcResultMatchers.jsonPath("$.listId").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("My First List"));
    }

    @Test
    void createANewListWithoutTitle() throws Exception {
        // Arrange
        JSONObject body = new JSONObject();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists")
                                                    .content(body.toString())
                                                    .header("content-type", "application/json");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("400 BAD_REQUEST \"Title is required\"")
            );
    }

    @Test
    void createANewListWithoutBody() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists").header("content-type", "application/json");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("400 BAD_REQUEST \"Title is required\"")
            );
    }

    // HU: Quiero ver las listas que hay en la aplicación
    @Test
    void returnAnEmptyListIfNoToDoListStored() throws Exception{
        // Arrange
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists");

        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.lists").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.lists").isEmpty());

    }

    @Test
    void returnAListWithAllToDoListStored() throws Exception{
        // Arrange
        ArrayList<ToDoList> allToDosList = new ArrayList<>();
        allToDosList.add(new ToDoList("First List"));
        allToDosList.add(new ToDoList("Second List"));
        toDoListRepository.saveAll(allToDosList);
        ListOfTodoListResponseBody expectedResponse = new ListOfTodoListResponseBody(allToDosList);
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(expectedResponse);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lists");

        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.lists").isArray())
            .andExpect(MockMvcResultMatchers.content().json(expectedBody));

    }

    @Test
    void createANewListWithATitleAndWIP() throws Exception {
        // Arrange
        JSONObject body = new JSONObject();
        body.put("title", "My First List");
        body.put("wip", 3);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists").content(body.toString()).header("content-type", "application/json");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(MockMvcResultMatchers.jsonPath("$.listId").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("My First List"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.wip").value("3"));
        
        assertThat(toDoListRepository.count()).isEqualTo(1);
    }
}
