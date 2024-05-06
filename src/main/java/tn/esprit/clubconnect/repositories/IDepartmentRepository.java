package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Department;

public interface IDepartmentRepository extends JpaRepository<Department,Long> {
}
