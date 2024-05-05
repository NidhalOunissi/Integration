package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Rating;

public interface IratingRepository extends JpaRepository<Rating,Long> {



}
