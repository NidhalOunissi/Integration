package tn.esprit.clubconnect.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.clubconnect.entities.Interview;

public interface IInterviewRepository extends CrudRepository<Interview, Integer> {
}
