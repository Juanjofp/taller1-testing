package es.centic.taller1.taller1.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User createNewUser(String username, String name) {
        return userRepository.save(new User(username, name));
    }
}

