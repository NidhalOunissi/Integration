package tn.esprit.clubconnect.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.Department;
import tn.esprit.clubconnect.entities.Interview;

import java.util.Optional;

public interface IDepartmentRepository extends CrudRepository<Department, Interview> {
}
