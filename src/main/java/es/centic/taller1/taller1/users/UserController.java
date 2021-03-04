package es.centic.taller1.taller1.users;

import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {
    
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users", produces = "application/json")
    public ListOfUsersBodyResponse showAllUser() {
        try {
            List<User> users = userService.findAllUsers();
            return new ListOfUsersBodyResponse(users);
        }
        catch(Exception exceptions) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptions.getMessage(), null);
        }
    }

    @PostMapping(value="/users", produces = "application/json")
    public UserBodyResponse createNewUser(@RequestBody UserBodyRequest userBody) {
        String username = userBody.getUsername();
        try {
            InternetAddress email = new InternetAddress(username);
            email.validate();
            String name = userBody.getName();
            User newUser = userService.createNewUser(username, name);
            return new UserBodyResponse(newUser);
        }
        catch(AddressException | NullPointerException badEmailException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username " + username, null);
        }
        catch(Exception exceptions) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptions.getMessage(), null);
        }
    }
}
