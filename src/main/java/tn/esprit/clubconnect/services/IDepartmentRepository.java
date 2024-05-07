package tn.esprit.clubconnect.services;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Department;

public interface IDepartmentRepository extends JpaRepository<Department,Long> {
}
