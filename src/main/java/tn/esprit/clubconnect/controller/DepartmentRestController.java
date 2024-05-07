package tn.esprit.clubconnect.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.clubconnect.entities.Department;
import tn.esprit.clubconnect.services.DepartmentServices;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")


@AllArgsConstructor
@RestController
@RequestMapping("/Department")
public class DepartmentRestController {

    private DepartmentServices departmentsServices;



    @PostMapping("/add")
    public Department adddepartment(@RequestBody Department department){
        return departmentsServices.addDepartment(department);
    }
    @PutMapping("/update")
    public Department updatedepartment(@RequestBody Department department){

        return departmentsServices.update(department);
    }
    @GetMapping("/get/{numdepartment}")
    public Department getdepartment(@PathVariable Long numdepartment){
        return departmentsServices.getById(numdepartment);
    }
    @DeleteMapping("/delete/{numdepartment}")
    public void removedepartment(@PathVariable Long numdepartment){
        departmentsServices.delete(numdepartment);
    }
    @GetMapping("/all")
    public List<Department> getAll(){

        return departmentsServices.getAll();
    }

    @PutMapping("/assignDepToClub/{depId}/{ClubId}")
    public Department assignDepToClub(@PathVariable Long depId,@PathVariable Long ClubId) {
        return departmentsServices.assignDepToClub(depId, ClubId);
    }

}
