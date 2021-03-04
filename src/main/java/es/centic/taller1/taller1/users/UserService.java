package es.centic.taller1.taller1.users;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AvatarService avatarService;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User createNewUser(String username, String name, @Nullable String avatar)  throws UserExistsException {
        Optional<User> existsUser = userRepository.findById(username);
        if(existsUser.isPresent()) {
            throw new UserExistsException(username);
        }
        String cachedAvatar = avatarService.cacheAvatar(avatar);
        return userRepository.save(new User(username, name, cachedAvatar));
    }
}

