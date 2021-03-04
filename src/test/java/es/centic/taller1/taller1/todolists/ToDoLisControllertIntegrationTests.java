package es.centic.taller1.taller1.todolists;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class ToDoLisControllertIntegrationTests {
    @Autowired
    private ToDoListController toDoListController;

    @Autowired
    private MockMvc mockMvc;

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
}