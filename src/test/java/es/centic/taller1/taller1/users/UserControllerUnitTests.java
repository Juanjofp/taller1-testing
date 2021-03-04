package es.centic.taller1.taller1.users;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

public class UserControllerUnitTests {

    private UserService mockUserService = Mockito.mock(UserService.class);
    private UserController controller = new UserController(mockUserService);

    // HU: Quiero ver los usuarios de la aplicación
    
    @Test
    void showAnEmptyListWhenNoUsers() throws Exception {
        // Arrange
        ArrayList<User> emptyList = new ArrayList<>();
        given(mockUserService.findAllUsers()).willReturn(emptyList);

        // Act
        ListOfUsersBodyResponse response = controller.showAllUser();

        // Assert
        assertThat(response.getUsers()).isEqualTo(emptyList);
    }

    @Test
    void showAListOfUsers() throws Exception {
        // Arrange
        ArrayList<User> usersMocked = new ArrayList<>();
        usersMocked.add(new User("juanjo@centic", "Juanjo"));
        usersMocked.add(new User("joaquin@centic", "Joaquin"));
        usersMocked.add(new User("pedro@centic", "Pedro"));
        given(mockUserService.findAllUsers()).willReturn(usersMocked);


        // Act
        ListOfUsersBodyResponse response = controller.showAllUser();

        // Assert
        assertThat(response.getUsers()).isEqualTo(usersMocked);
    }

    @Test()
    void showAListOfUsersFailsWhenDBUnavailable() throws Exception {
        // Arrange
        given(mockUserService.findAllUsers()).willThrow(IllegalArgumentException.class);

        // Act
        Exception expected = assertThrows(
            ResponseStatusException.class,
            () -> {
                controller.showAllUser();
            }
        );

        assertThat(expected.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    // HU: Quiero añadir usuarios con username y name, el username será su email.

    @Test
    void createNewUserWithEmail() throws Exception {
        // Arrange
        String username = "juanjo@centic";
        String name = "Juanjo";
        UserBodyRequest body = new UserBodyRequest(username, name);
        given(mockUserService.createNewUser(username, name)).willReturn(new User(username, name));

        // Act
        UserBodyResponse response = controller.createNewUser(body);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.getName()).isEqualTo(name);
    }

    @Test()
    void createNewUserWithInvalidEmail() throws Exception {
        // Arrange
        String username = "juanjocentic";
        String name = "Juanjo";
        UserBodyRequest body = new UserBodyRequest(username, name);
        given(mockUserService.createNewUser(username, name)).willThrow(new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid email " + username));

        // Act
        Exception expected = assertThrows(
            ResponseStatusException.class,
            () -> {
                controller.createNewUser(body);
            }
        );

        assertThat(expected.getMessage()).isEqualTo("400 BAD_REQUEST \"Invalid username juanjocentic\"");
    }

    @Test()
    void createNewUserWithoutEmail() throws Exception {
        // Arrange
        String username = null;
        String name = "Juanjo";
        UserBodyRequest body = new UserBodyRequest(username, name);
        given(mockUserService.createNewUser(username, name)).willThrow(new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid email " + username));

        // Act
        Exception expected = assertThrows(
            ResponseStatusException.class,
            () -> {
                controller.createNewUser(body);
            }
        );

        assertThat(expected.getMessage()).isEqualTo("400 BAD_REQUEST \"Invalid username null\"");
    }

    @Test()
    void createNewUserFailsWhenDBUnavailable() throws Exception {
        // Arrange
        String username = "juanjo@centic";
        String name = "Juanjo";
        UserBodyRequest body = new UserBodyRequest(username, name);
        given(mockUserService.createNewUser(username, name)).willThrow(IllegalArgumentException.class);

        // Act
        Exception expected = assertThrows(
            ResponseStatusException.class,
            () -> {
                controller.createNewUser(body);
            }
        );
        
        // Assert
        assertThat(expected.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }
}

