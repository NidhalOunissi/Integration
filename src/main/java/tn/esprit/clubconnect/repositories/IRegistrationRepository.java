package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Registration;

public interface IRegistrationRepository extends JpaRepository<Registration, Integer> {

}
