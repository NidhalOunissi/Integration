package tn.esprit.clubconnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.clubconnect.entities.Event;
import tn.esprit.clubconnect.services.ICloudinaryService;
import tn.esprit.clubconnect.services.IEventServices;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clubadmin/event")
@CrossOrigin("http://localhost:4200")

public class EventConntroller {

    private final IEventServices eventServices;
    private final ICloudinaryService cloudinaryService;
    //private final IClubService clubService;



    @PostMapping("/addEvent")
    public ResponseEntity<String> addEventWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            //@RequestParam("duration") int duration,
            @RequestParam("location") String location,

            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        try {
            Event event = new Event();
            event.setTitle(title);
            event.setDescription(description);
            event.setStartDate(startDate);
            event.setLocation(location);

            /*event.setDuration(duration);

            Club club = clubService.getbyid(idC); // Assuming you have a method to retrieve the club by ID
            event.setClub(club);*/

            if (!imageFile.isEmpty()) {
                // Image is present, process it
                eventServices.addEventWithImage(event, imageFile);
            } else {
                // No image uploaded, potentially allow saving without image
               // eventServices.addEvent(event); // Assuming a method without image param
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("event ajoutée avec succès !");
        } catch (Exception e) {
            e.printStackTrace(); // Consider more specific exception handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout de l'event.");
        }
    }

    @PutMapping("/update")
    public Event updateEvent(@RequestBody Event event){return eventServices.updateEvent(event);}
    @PutMapping("/updatebyid/{idE}")
    public Event upadteEventbyId(@PathVariable("idE") int idE , @RequestBody  Event event){return  eventServices.updateEventById(idE,event); }

    @GetMapping("/get/{idE}")
    public Event getEvent(@PathVariable int idE){return  eventServices.getbyid(idE);}
    @DeleteMapping("/delete/{idE}")
    public void removeEvent(@PathVariable int idE){eventServices.deleteEvent(idE);}
    @GetMapping("/all")
    public List<Event> getall(){
        return eventServices.getall();
    }
    @PutMapping("/paiement/{idE}")
    public Event paiement(@PathVariable int idE){return eventServices.paiement(idE);}


}
