package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Event;

public interface IEventRepository extends JpaRepository<Event,Integer> {
}
