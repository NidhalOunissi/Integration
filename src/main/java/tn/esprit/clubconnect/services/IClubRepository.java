package tn.esprit.clubconnect.services;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Club;

public interface IClubRepository extends JpaRepository<Club,Long> {
}
