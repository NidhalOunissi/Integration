package tn.esprit.clubconnect.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.clubconnect.entities.User;

import java.util.Optional;

public interface IUserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail (String email);
    User findByIdU (int id);
    Optional<User> findByTokens_Token(String token);

}
