package tn.esprit.clubconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.clubconnect.entities.Registration;
import tn.esprit.clubconnect.services.IRegistrationServices;

import java.util.List;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    private final IRegistrationServices registrationServices;

    @Autowired
    public RegistrationController(IRegistrationServices registrationServices) {
        this.registrationServices = registrationServices;
    }

    @GetMapping("/all")
    public List<Registration> getAllRegistrations() {
        return registrationServices.findAll();
    }

    @GetMapping("/getbyid/{idR}")
    public Registration getRegistrationById(@PathVariable Integer idR) {
        return registrationServices.findById(idR);
    }

    @PostMapping("/add")
    public Registration createRegistration(@RequestBody Registration registration) {
        return registrationServices.save(registration);
    }

    @PutMapping("/update/{idR}")
    public Registration updateRegistration(@PathVariable Integer idR, @RequestBody Registration registration) {
        registration.setIdR(idR);
        return registrationServices.save(registration);
    }

    @DeleteMapping("/delete/{idR}")
    public void deleteRegistration(@PathVariable Integer idR) {
        registrationServices.deleteById(idR);
    }

}
