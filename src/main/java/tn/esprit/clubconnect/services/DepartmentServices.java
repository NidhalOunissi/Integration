package tn.esprit.clubconnect.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.Department;
import tn.esprit.clubconnect.repositories.IClubRepository;
import tn.esprit.clubconnect.repositories.IDepartmentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service

public class DepartmentServices implements IDepartmentServices{

    private final IDepartmentRepository iDepartmentRepository;
    private final IClubRepository iClubRepository;
    private final NotificationService notificationService;

    @Override
    public Department addDepartment(Department department){

        return iDepartmentRepository.save(department);
    }

   /* @Override
    public Department addDepartment(Department department) {
        Department newDepartment = iDepartmentRepository.save(department);

        if (newDepartment != null) {
            // Trigger notification for the new department
            notificationService.sendDepartmentAddedNotification(newDepartment);
        }

        return newDepartment;
    }*/

    @Override
    public Department update(Department department) {

        return iDepartmentRepository.save(department);
    }

    @Override
    public void delete(Long numDepartment) {

        iDepartmentRepository.deleteById(numDepartment);
    }

    @Override
    public Department getById(Long numDepartment) {

        return iDepartmentRepository.findById(numDepartment).get();
    }

    @Override
    public List<Department> getAll() {

        return (List<Department>) iDepartmentRepository.findAll();
    }

    public Department assignDepToClub(Long depId, Long clubId){
        Department dep = iDepartmentRepository.findById(depId).orElse(null);
        Club club=iClubRepository.findById(clubId).orElse(null);
        dep.setClub(club);
        return iDepartmentRepository.save(dep);
    }




}
