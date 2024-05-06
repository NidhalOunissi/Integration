package tn.esprit.clubconnect.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.clubconnect.entities.Reclamation;
import tn.esprit.clubconnect.entities.TypeR;
import tn.esprit.clubconnect.services.ReclamationServices;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
//permet de consommer les crud dans le front
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/Reclamation")
public class ReclamationRestController {
    private ReclamationServices reclamationServices;
    @PostMapping("/add")
    public Reclamation addReclamation (@RequestBody Reclamation reclamation){
        return reclamationServices.addReclamation(reclamation);
    }
    @PutMapping("/update")
    public Reclamation updateReclamation(@RequestBody Reclamation reclamation){

        return reclamationServices.updateReclamation(reclamation);
    }
    @GetMapping("/get/{idR}")
    public Reclamation getReclamation(@PathVariable Long idR){
        return reclamationServices.getById(idR);
    }

    @DeleteMapping("/delete/{idR}")
    public void removeSkieur(@PathVariable Long idR ){

        reclamationServices.delete(idR);
    }
    @GetMapping("/all")
    public List<Reclamation> getAll(){

        return reclamationServices.getAll();
    }

    @GetMapping("/statistics")
    public Map<TypeR, Long> getReclamationStatisticsByType() {
        return reclamationServices.getReclamationStatisticsByType();
    }
    @GetMapping("/highest-average-rate-type")
    public TypeR getTypeWithHighestAverageRate() {
        return reclamationServices.getTypeWithHighestAverageRate();
    }

    @GetMapping("/priorite")
    public List<Reclamation> getReclamationsByPriority() {
        return reclamationServices.getReclamationsByPriority();
}
}

