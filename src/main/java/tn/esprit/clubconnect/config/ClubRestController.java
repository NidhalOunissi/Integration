package tn.esprit.clubconnect.config;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.Categ;
import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.Image;
import tn.esprit.clubconnect.entities.PredictionObject;
import tn.esprit.clubconnect.repositories.IClubRepository;
import tn.esprit.clubconnect.repositories.IImageRepository;
import tn.esprit.clubconnect.services.CloudinaryServiceImpl;
import tn.esprit.clubconnect.services.ClubServices;
import tn.esprit.clubconnect.services.NotificationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")

@AllArgsConstructor
@RestController
@RequestMapping("/Club")
public class ClubRestController {
    @Autowired
    private IImageRepository imageRepository;
    @Autowired
    private IClubRepository iClubRepository;
    private NotificationService notificationService;
    @Autowired
    private final ClubServices clubServices;
    @Autowired
    private final CloudinaryServiceImpl cloudinaryService;


   /* @PostMapping("/add")
    public Club addclub(@RequestBody Club club){
       return clubsServices.addClub(club);
    }*/
   /* @PostMapping("/add")
    public ResponseEntity<?> addClub(@RequestBody Club club) {
        try {
            //Club savedClub = clubsServices.addClub(club);
            //return ResponseEntity.ok(savedClub);
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding club: " + e.getMessage());
        }
    }*/

    @Autowired
    public ClubRestController(ClubServices clubServices, CloudinaryServiceImpl cloudinaryService) {
        this.clubServices = clubServices;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/addClub")
    public ResponseEntity<String> addClubImage(@RequestParam("nameC") String nameC,
                                               @RequestParam("description") String description,
                                               @RequestParam("creationDate") String creationDate,
                                               @RequestParam("categorie") Categ categorie,
                                               @RequestParam("websiteURL") String websiteURL,
                                               @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedCreationDate = LocalDate.parse(creationDate, formatter);

            // Create a new club
            Club club = new Club();
            club.setNameC(nameC);
            club.setDescription(description);
            club.setCreationDate(parsedCreationDate);
            club.setCategorie(categorie);
            club.setWebsiteURL(websiteURL);
            //club.setLogo(imageFile);

            // Add the club with the image
            if (cloudinaryService != null) {
                clubServices.addClub(club, imageFile);
                return ResponseEntity.status(HttpStatus.CREATED).body("Club ajouté avec succès !");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Le service Cloudinary n'est pas disponible pour le moment. Veuillez réessayer plus tard.");
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Format de date invalide. Utilisez le format yyyy-MM-dd pour la date de création.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout du club.");
        }
    }



  /* @PostMapping("/add")
   public ResponseEntity<?> addClub(@RequestBody Club club) {
       try {
           Club savedClub = clubsServices.addClub(club);

           // Trigger notification creation after adding a club
           notificationService.createNotification("New club added: " + club.getNameC());

           return ResponseEntity.ok(savedClub);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding club: " + e.getMessage());
       }
   }*/



    // Updated updateClub method
    @PutMapping("/updateClub")
    public ResponseEntity<String> updateClub(@RequestParam("idC") Long idC,
                                             @RequestParam("nameC") String nameC,
                                             @RequestParam("description") String description,
                                             @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            Club existingClub = iClubRepository.findById(idC)
                    .orElseThrow(() -> new EntityNotFoundException("Club not found"));

            // Update the club details
            existingClub.setNameC(nameC);
            existingClub.setDescription(description);

            // Update the club with the new image
            if (imageFile != null) {
                String imageUrl = cloudinaryService.uploadFile(imageFile, "club_images");

                if (imageUrl == null) {
                    throw new RuntimeException("Failed to upload image");
                }

                Image image = Image.builder()
                        .name(imageFile.getOriginalFilename())
                        .url(imageUrl)
                        .build();
                imageRepository.save(image);

                existingClub.setLogo(image);
            }

            iClubRepository.save(existingClub); // Save the updated club

            return ResponseEntity.status(HttpStatus.OK).body("Club updated successfully");
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Club not found.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the club: " + e.getMessage());
        }
    }




    /* @PutMapping("/update")
    public Club updateclub(@RequestBody Club club){

        return clubServices.update(club);
    }*/
    @GetMapping("/get/{numclub}")
    public Club getclub(@PathVariable Long numclub){
    return clubServices.getById(numclub);
    }
    @DeleteMapping("/delete/{numclub}")
    public void removeclub(@PathVariable Long numclub){
        clubServices.delete(numclub);
    }
    @GetMapping("/all")
    public List<Club> getAll(){
        return clubServices.getAll();
    }
 




    @GetMapping("/generateQRCode/{clubId}")
    public String generateQRCodeForClubWebsite(@PathVariable Long clubId) {
        try {
            Club club = clubServices.getById(clubId); // Assuming getById method retrieves the club by ID
            if (club != null) {
                return clubServices.generateQRCodeForClubWebsite(club);
            } else {
                // Handle case where club with specified ID is not found
                return "Club not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating QR code: " + e.getMessage();
        }
    }


    @GetMapping("/departments/count/{clubId}")
    public ResponseEntity<?> getNumberOfDepartmentsPerClub(@PathVariable Long clubId) {
        try {
            int departmentCount = clubServices.getNumberOfDepartmentsPerClub(clubId); // Assuming the implementation of this method
            return ResponseEntity.ok(departmentCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting department count for club: " + e.getMessage());
        }
    }


    @GetMapping("/users/count/{clubId}")
    public ResponseEntity<?> getNumberOfUsersPerClub(@PathVariable Long clubId) {
        try {
            int userCount = clubServices.getNumberOfUsersPerClub(clubId); // Assuming the implementation of this method
            return ResponseEntity.ok(userCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting user count for club: " + e.getMessage());
        }
    }

    @GetMapping("/events/count/{clubId}")
    public ResponseEntity<?> getNumberOfEventsPerClub(@PathVariable Long clubId) {
        try {
            int eventCount = clubServices.getNumberOfEventsPerClub(clubId); // Assuming the implementation of this method
            return ResponseEntity.ok(eventCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting event count for club: " + e.getMessage());
        }
    }


    /*@GetMapping("/{clubId}/average-user-engagement")
    public ResponseEntity<Double> getAverageUserEngagement(@PathVariable Long clubId) {
        double averageUserEngagement = clubServices.calculateAverageUserEngagement(clubId);
        if (averageUserEngagement >= 0) {
        return ResponseEntity.ok(averageUserEngagement);
    } else {
        return ResponseEntity.notFound().build();
    }
}*/

    /*@GetMapping("/{clubId}/total-funds-raised")
    public ResponseEntity<Double> getTotalFundsRaised(@PathVariable Long clubId) {
        double totalFundsRaised = clubServices.calculateTotalFundsRaised(clubId);
        if (totalFundsRaised >= 0) {
            return ResponseEntity.ok(totalFundsRaised);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

   /* @GetMapping("/{clubId}/number-of-projects")
    public ResponseEntity<Integer> getNumberOfProjects(@PathVariable Long clubId) {
        int numberOfProjects = clubServices.calculateNumberOfProjects(clubId);
        if (numberOfProjects >= 0) {
            return ResponseEntity.ok(numberOfProjects);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/



    @PostMapping("/{clubId}/assign-user/{userId}")
    public ResponseEntity<String> assignUserToClub(@PathVariable Long clubId, @PathVariable Long userId) {
        try {
            clubServices.assignUserToClub(userId, clubId);
            return ResponseEntity.ok("User assigned to club successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while assigning user to club.");
        }
    }

    @PostMapping("/{userId}/assign-event/{eventId}")
    public ResponseEntity<String> assignUserToEvent(@PathVariable Long userId, @PathVariable int eventId) {
        try {
            clubServices.assignUserToEvent(userId, eventId);
            return ResponseEntity.ok("User assigned to event successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while assigning user to event.");
        }
    }

    @PostMapping("/{userId}/assign-training/{trainingId}")
    public ResponseEntity<String> assignUserToTraining(@PathVariable Long userId, @PathVariable int trainingId) {
        try {
            clubServices.assignUserToTraining(userId, trainingId);
            return ResponseEntity.ok("User assigned to training successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while assigning user to training.");
        }
    }


    @GetMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictProjectsAndFunds(@RequestParam Long clubId, @RequestParam double averageProjectCost) {
        Map<String, Object> prediction = clubServices.predictProjectsAndFunds(clubId, averageProjectCost);
        return ResponseEntity.ok(prediction);
    }

   /* @GetMapping("/{clubId}/predict")
    public ResponseEntity<Map<String, Object>> predictProjectsAndFundsInPeriod(
            @PathVariable Long clubId,
            @RequestParam int periodInMonths) {
        Map<String, Object> prediction = clubServices.predictProjectsAndFundsInPeriod(clubId, periodInMonths);
        return ResponseEntity.ok(prediction);
    }*/

    /*@GetMapping("/{clubId}/predict")
    public ResponseEntity<List<PredictionObject>> predictProjectsAndFundsInPeriod(
            @PathVariable Long clubId,
            @RequestParam int periodInMonths) {
        Map<String, Object> predictionMap = clubServices.predictProjectsAndFundsInPeriod(clubId, periodInMonths);
        List<PredictionObject> predictionList = clubServices.convertPredictionMapToList(predictionMap);
        return ResponseEntity.ok(predictionList);
    }*/
   /* @GetMapping("/{clubId}/predict")
    public ResponseEntity<List<PredictionObject>> predictProjectsAndFundsInPeriod(
            @PathVariable Long clubId,
            @RequestParam int periodInMonths) {
        Map<String, Object> predictionMap = clubServices.predictProjectsAndFundsInPeriod(clubId, periodInMonths);
        List<PredictionObject> predictionList = clubServices.convertPredictionMapToList(predictionMap, clubId, periodInMonths);

        return ResponseEntity.ok(predictionList);
    }*/

    @GetMapping("/{clubId}/predict")
    public ResponseEntity<List<PredictionObject>> predictProjectsAndFundsInPeriod(
            @PathVariable Long clubId,
            @RequestParam int periodInMonths) {

        Map<String, Object> predictionMap = clubServices.predictProjectsAndFundsInPeriod(clubId, periodInMonths);
        List<PredictionObject> predictionList = clubServices.convertPredictionMapToList(predictionMap, clubId, periodInMonths);

        return ResponseEntity.ok(predictionList);
    }

    @PostMapping("/analyze-attendance-and-predict-attrition/{clubId}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>>
    analyzeAttendanceAndPredictAttrition(@PathVariable Long clubId) {
        Map<String, List<Map<String, Object>>> predictions = clubServices.analyzeAttendanceAndPredictAttrition(clubId);

        if (!predictions.isEmpty()) {
            return new ResponseEntity<>(predictions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}







