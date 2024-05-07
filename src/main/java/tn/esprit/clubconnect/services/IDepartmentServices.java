package tn.esprit.clubconnect.services;

import tn.esprit.clubconnect.entities.Department;

import java.util.List;

public interface IDepartmentServices {

    Department addDepartment(Department department);
    Department update(Department department);
    void delete(Long numDepartment);
    Department getById(Long numDepartment);
    List<Department> getAll();
    public Department assignDepToClub(Long depId, Long clubId);
}
