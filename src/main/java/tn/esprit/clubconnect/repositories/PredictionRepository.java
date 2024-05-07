package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Prediction;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
}
