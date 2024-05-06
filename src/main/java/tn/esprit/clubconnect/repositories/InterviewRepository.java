package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.clubconnect.entities.Interview;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {
}


