package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Training;

import java.time.LocalDateTime;
import java.util.List;

public interface ITrainingRepository extends JpaRepository<Training, Integer> {
    List<Training> findByTitleTContainingIgnoreCaseOrStartDateOrLocationContainingIgnoreCase(String titleT, LocalDateTime startDate, String location);

    List<Training> findByStartDateBetweenAndNumberPlaceGreaterThan(LocalDateTime startDate, LocalDateTime endDate, int numberPlace);
}
