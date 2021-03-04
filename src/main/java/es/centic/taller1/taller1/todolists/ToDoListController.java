package es.centic.taller1.taller1.todolists;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
public class ToDoListController {
    
    @GetMapping(value="/lists", produces = MediaType.APPLICATION_JSON_VALUE)
    public String showAllLists() {
        return "[]";
    }
}
