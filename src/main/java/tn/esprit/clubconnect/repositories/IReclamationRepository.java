package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.clubconnect.entities.Reclamation;
import tn.esprit.clubconnect.entities.TypeR;

import java.util.List;

public interface IReclamationRepository extends JpaRepository<Reclamation,Long> {
    @Query("SELECT r.type, COUNT(r) FROM Reclamation r GROUP BY r.type")
    List<Object[]> countByType();

    List<Reclamation> findByType(TypeR type);
}
