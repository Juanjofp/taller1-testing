package es.centic.taller1.taller1.todolists;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class ToDoLisControllertIntegrationTests {
    @Autowired
    private ToDoListController toDoListController;

    @Test
    void checkToDoListController() {
        assertThat(toDoListController).isNotNull();
    }
}
