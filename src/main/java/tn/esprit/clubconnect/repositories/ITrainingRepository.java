package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tn.esprit.clubconnect.entities.Training;
import tn.esprit.clubconnect.entities.User;

public interface ITrainingRepository extends CrudRepository<Training, Integer> {
    @Query("SELECT COUNT(t) FROM Training t JOIN t.users u WHERE u = :user")
    int countTrainingsByUser(@Param("user") User user);
}
