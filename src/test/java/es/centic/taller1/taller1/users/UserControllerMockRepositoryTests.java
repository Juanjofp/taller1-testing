package es.centic.taller1.taller1.users;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerMockRepositoryTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    // HU: Quiero ver los usuarios de la aplicación

    @Test
    void showAnEmptyListWhenNoUsers() throws Exception {
        // Arrange
        given(userRepository.findAll()).willReturn(new ArrayList<>());
        RequestBuilder request = MockMvcRequestBuilders.get("/users");

        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.users").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.users").isEmpty());
    }

    @Test
    void showAListOfUsers() throws Exception {
        // Arrange
        RequestBuilder request = MockMvcRequestBuilders.get("/users");
        JSONObject expectedJson = new JSONObject();
        JSONArray expectedUsers = new JSONArray();
        expectedUsers.put(new JSONObject("{\"username\": \"juanjo@centic\", \"name\": \"Juanjo\"}"));
        expectedUsers.put(new JSONObject("{\"username\": \"joaquin@centic\", \"name\": \"Joaquin\"}"));
        expectedUsers.put(new JSONObject("{\"username\": \"pedro@centic\", \"name\": \"Pedro\"}"));
        expectedJson.put("users", expectedUsers);
        ArrayList<User> usersMocked = new ArrayList<>();
        usersMocked.add(new User("juanjo@centic", "Juanjo"));
        usersMocked.add(new User("joaquin@centic", "Joaquin"));
        usersMocked.add(new User("pedro@centic", "Pedro"));
        given(userRepository.findAll()).willReturn(usersMocked);


        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.users").isArray())
            .andExpect(
                MockMvcResultMatchers.content().json(expectedJson.toString())
            );
    }

    @Test
    void showAListOfUsersFailsWhenDBUnavailable() throws Exception {
        // Arrange
        RequestBuilder request = MockMvcRequestBuilders.get("/users");
        given(userRepository.findAll()).willThrow(IllegalArgumentException.class);

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("500 INTERNAL_SERVER_ERROR"));
    }

    // HU: Quiero añadir usuarios con username y name, el username será su email.
    
    @Test
    void createNewUserWithEmail() throws Exception {
        // Arrange
        JSONObject body = new JSONObject("{\"username\": \"juanjo@centic\", \"name\": \"Juanjo\"}");
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
            .content(body.toString())
            .header("content-type", "application/json");
        User expectedUser = new User("juanjo@centic", "Juanjo");
        given(userRepository.save(expectedUser)).willReturn(expectedUser);

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(
                MockMvcResultMatchers.content().json(body.toString())
            );
    }

    @Test
    void createNewUserFailsWhenDBUnavailable() throws Exception {
        // Arrange
        JSONObject body = new JSONObject("{\"username\": \"juanjo@centic\", \"name\": \"Juanjo\"}");
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
            .content(body.toString())
            .header("content-type", "application/json");
        User expectedUser = new User("juanjo@centic", "Juanjo");
        given(userRepository.save(expectedUser)).willThrow(IllegalArgumentException.class);

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("500 INTERNAL_SERVER_ERROR"));
    }
    
}

