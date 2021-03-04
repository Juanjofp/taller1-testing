package es.centic.taller1.taller1.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class UserRepositoryIntegrationTests {
    
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setupTests() {
        userRepository.deleteAll();
    }

    // HU: Quiero añadir usuarios con username y name, el username será su email.

    @Test
    void createANewUser() throws Exception {
        // Arrange
        User user = new User("juanjo@centic", "Juanjo");

        // ACT
        User expected = userRepository.save(user);

        // ASSERT
        assertThat(expected.getUsername()).isNotNull();
    }

    @Test
    void createANewUserWithoutName() throws Exception {
        // Arrange
        User user = new User("Juanjo", null);

        // ACT
        User expected = userRepository.save(user);

        // ASSERT
        assertThat(expected.getUsername()).isNotNull();
        assertThat(expected.getName()).isNull();
    }

    // HU: Quiero ver los usuarios de la aplicación
    @Test
    void retriveAllUsers() throws Exception {
        // Arrange
        User one = userRepository.save(new User("juanjo@centic", "Juanjo"));
        User two = userRepository.save(new User("joaquin@centic", "Joaquin"));
        User three = userRepository.save(new User("alicia@centic", "Alicia"));
        User four = userRepository.save(new User("pedro@centic", "Pedro"));

        // ACT
        Iterable<User> users = userRepository.findAll();

        // Expect
        assertThat(users)
            .hasSize(4)
            .extracting("name").contains(
                one.getName(),
                two.getName(),
                three.getName(),
                four.getName()
            );

    }
}

