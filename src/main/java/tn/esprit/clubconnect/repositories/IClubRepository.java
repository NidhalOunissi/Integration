package tn.esprit.clubconnect.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.clubconnect.entities.Club;

import java.util.Optional;

public interface IClubRepository extends CrudRepository<Club, Integer> {
}
