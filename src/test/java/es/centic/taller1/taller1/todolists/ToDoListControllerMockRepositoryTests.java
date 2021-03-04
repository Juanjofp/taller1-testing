package es.centic.taller1.taller1.todolists;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ToDoListControllerMockRepositoryTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoListRepository toDoListRepository;

    // HU: Quiero poder crear listas con un título

    @Test
    void controllerReturn503WhenRepositoryFailsSavingANewToDoList() throws Exception{
        // Arrange
        JSONObject body = new JSONObject();
        body.put("title", "New List");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lists").content(body.toString()).header("content-type", "application/json");
        given(toDoListRepository.save(any(ToDoList.class))).willThrow(new RuntimeException());

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isServiceUnavailable())
            .andExpect(
                result -> assertThat(result.getResolvedException().getMessage()).contains("Service unavailable, check later")
            );
    }
}
