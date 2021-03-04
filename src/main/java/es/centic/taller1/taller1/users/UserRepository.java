package es.centic.taller1.taller1.users;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
}
