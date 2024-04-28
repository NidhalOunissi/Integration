package tn.esprit.clubconnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.requestAndresponse.RoleChangeRequest;
import tn.esprit.clubconnect.services.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public List<User> getAllUsers (){
        return adminService.getAllUsers();
    }

    @DeleteMapping("/u-delete/{id}")
    void deleteUser(@PathVariable int id){
        adminService.deleteUser(id);
    }

    @PutMapping("/lock/{id}")
    public void lockUser (@PathVariable int id){
        adminService.lockUser(id);
    }

    @PutMapping("/uc-role/{id}")
    public void changeUserRole(@PathVariable int id, @RequestBody RoleChangeRequest request){
        adminService.changeUserRole(id,request);
    }

    @GetMapping("/club-list")
    public List<Club> getAllClubs (){
        return adminService.getAllClubs();
    }

//
//
//    @GetMapping
//    public String get(){
//        return "Ena el GET mta3 el admin";
//    }
//
//
//    @PostMapping
//    public String post(){
//        return "Ena el POST mta3 el admin";
//    }
//
//
//    @PutMapping
//    public String put(){
//        return "Ena el PUT mta3 el admin";
//    }
//
//    @DeleteMapping
//    public String delete(){
//        return "Ena el DELETE mta3 el admin";
//    }

}
