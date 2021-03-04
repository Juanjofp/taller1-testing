package es.centic.taller1.taller1.users;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void initApiServer() {
        userRepository.deleteAll();
    }

    // HU: Quiero ver los usuarios de la aplicación

    @Test
    void showAnEmptyListWhenNoUsers() throws Exception {
        // Arrange
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
        expectedUsers.put(new JSONObject("{\"username\": \"juanjo@centic\", \"name\": \"Juanjo\", \"avatar\": \"http://miavatarsupermolor.com\"}"));
        expectedUsers.put(new JSONObject("{\"username\": \"joaquin@centic\", \"name\": \"Joaquin\", \"avatar\": \"http://miavatarsupermolor.com\"}"));
        expectedUsers.put(new JSONObject("{\"username\": \"pedro@centic\", \"name\": \"Pedro\", \"avatar\": \"http://miavatarsupermolor.com\"}"));
        expectedJson.put("users", expectedUsers);
        // No añadir avatar hasta el final....
        userRepository.save(new User("juanjo@centic", "Juanjo", "http://miavatarsupermolor.com"));
        userRepository.save(new User("joaquin@centic", "Joaquin", "http://miavatarsupermolor.com"));
        userRepository.save(new User("pedro@centic", "Pedro", "http://miavatarsupermolor.com"));


        // Act
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.users").isArray())
            .andExpect(
                MockMvcResultMatchers.content().json(expectedJson.toString())
            );
    }

    // HU: Quiero añadir usuarios con username y name, el username será su email.

    @Test
    void createNewUserWithEmail() throws Exception {
        // Arrange
        JSONObject body = new JSONObject("{\"username\": \"juanjo@centic\", \"name\": \"Juanjo\"}");
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
            .content(body.toString())
            .header("content-type", "application/json");
        User expectedUser = new User("juanjo@centic", "Juanjo", "http://miavatarsupermolor.com");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(
                MockMvcResultMatchers.content().json(body.toString())
            );
        Optional<User> savedUser = userRepository.findById("juanjo@centic");
        assertThat(savedUser.get()).isEqualTo(expectedUser);
    }

    @Test
    void createNewUserWithInvalidEmail() throws Exception {
        // Arrange
        JSONObject body = new JSONObject("{\"username\": \"juanjocentic\", \"name\": \"Juanjo\"}");
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
            .content(body.toString())
            .header("content-type", "application/json");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Invalid username juanjocentic"));
        Optional<User> savedUser = userRepository.findById("juanjocentic");
        assertThat(savedUser.isPresent()).isEqualTo(false);
    }

    @Test
    void createNewUserWithoutEmail() throws Exception {
        // Arrange
        JSONObject body = new JSONObject("{\"name\": \"Juanjo\"}");
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
            .content(body.toString())
            .header("content-type", "application/json");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Invalid username null"));
        Optional<User> savedUser = userRepository.findById("null");
        assertThat(savedUser.isPresent()).isEqualTo(false);
    }

    // Al crear un usuario con el mismo username modifica el name, queremos que nos avise de que el usuario existe

    @Test
    void createDuplicateUser() throws Exception {
        // Arrange
        User userInApp = new User("juanjo@centic", "Juanjo", "http://miavatarsupermolor.com");
        userRepository.save(userInApp);
        JSONObject body = new JSONObject("{\"username\": \"juanjo@centic\", \"name\": \"Juanjo Franco\"}");
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
            .content(body.toString())
            .header("content-type", "application/json");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("409 CONFLICT \"User juanjo@centic already exists\""));
        Optional<User> savedUser = userRepository.findById("juanjo@centic");
        assertThat(savedUser.get()).isEqualTo(userInApp);
    }

    // HU: Quiero que los usuarios al crearse puedan añadirse un avatar o se le proporcione uno por defecto

    @Test
    void createNewUserWithEmailAndAvatar() throws Exception {
        // Arrange
        JSONObject body = new JSONObject("{\"username\": \"juanjo@centic\", \"name\": \"Juanjo\", \"avatar\": \"http://miavatarsupermolor.com\"}");
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
            .content(body.toString())
            .header("content-type", "application/json");
        User expectedUser = new User("juanjo@centic", "Juanjo", "http://miavatarsupermolor.com");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(
                MockMvcResultMatchers.content().json(body.toString())
            );
        Optional<User> savedUser = userRepository.findById("juanjo@centic");
        assertThat(savedUser.get()).isEqualTo(expectedUser);
    }

    @Test
    void createNewUserWithEmailAndWithoutAvatar() throws Exception {
        // Arrange
        JSONObject body = new JSONObject("{\"username\": \"juanjo@centic\", \"name\": \"Juanjo\"}");
        JSONObject expectedBody = new JSONObject(body.toString());
        expectedBody.put("avatar", "https://avatar.io/default");
        RequestBuilder request = MockMvcRequestBuilders.post("/users")
            .content(body.toString())
            .header("content-type", "application/json");
        User expectedUser = new User("juanjo@centic", "Juanjo", "https://avatar.io/default");

        // Act
        mockMvc.perform(request)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
            .andExpect(
                MockMvcResultMatchers.content().json(expectedBody.toString())
            );
        Optional<User> savedUser = userRepository.findById("juanjo@centic");
        assertThat(savedUser.get()).isEqualTo(expectedUser);

    }
}

