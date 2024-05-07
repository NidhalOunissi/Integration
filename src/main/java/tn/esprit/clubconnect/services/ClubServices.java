package tn.esprit.clubconnect.services;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.*;
import tn.esprit.clubconnect.repositories.*;
import tn.esprit.clubconnect.repositories.IClubRepository;
import tn.esprit.clubconnect.repositories.IEventRepository;
import tn.esprit.clubconnect.repositories.IImageRepository;
import tn.esprit.clubconnect.repositories.ITrainingRepository;
import tn.esprit.clubconnect.repositories.IUserRepository;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@AllArgsConstructor
@Service
public class ClubServices implements IClubServices {

    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private IEventRepository eventRepository;

    @Autowired
    private ITrainingRepository trainingRepository;
    @Autowired
    private IClubRepository iClubRepository;
    @Autowired
    private IUserRepository userRepository;

    private ApplicationEventPublisher eventPublisher;
    private NotificationService notificationService;
    @Autowired
    private IImageRepository imageRepository;
    private ICloudinaryService iCloudinaryService;
    @Autowired
    private CloudinaryServiceImpl cloudinaryService;


    @Autowired
    public ClubServices(IClubRepository iClubRepository, ApplicationEventPublisher eventPublisher, NotificationService notificationService) {
        this.iClubRepository = iClubRepository;
        this.eventPublisher = eventPublisher;
        this.notificationService = notificationService;
    }



  /*  @Override
    public Club addClub(Club club) {
        Club savedClub = iClubRepository.save(club);
        String clubName = club.getNameC();
        eventPublisher.publishEvent(new ClubCreatedEvent(this, clubName));
        return savedClub;
    }*/


    @Override
    public Club addClub(Club club, MultipartFile imageFile) {
        String imageUrl = cloudinaryService.uploadFile(imageFile, "club_images"); // Change "event_images" to "club_images"

        if (imageUrl == null) {
            throw new RuntimeException("Failed to upload image");
        }

        Image image = Image.builder()
                .name(imageFile.getOriginalFilename())
                .url(imageUrl)
                .build();

        imageRepository.save(image);
        club.setLogo(image); // Set the image object instead of just the URL

        return iClubRepository.save(club); // Save the club with the image
    }
  /* public Club addClub(Club club){
       return iClubRepository.save(club);
   }*/

  /*  @Override
    public Club update(Club club) {

        return iClubRepository.save(club);
    }*/

    @Override
    public Club update(Club club, MultipartFile newImageFile) {
        Club existingClub = iClubRepository.findById(club.getIdC())
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));

        if (newImageFile != null) {
            String imageUrl = cloudinaryService.uploadFile(newImageFile, "club_images");

            if (imageUrl == null) {
                throw new RuntimeException("Failed to upload image");
            }

            Image image = Image.builder()
                    .name(newImageFile.getOriginalFilename())
                    .url(imageUrl)
                    .build();
            imageRepository.save(image);

            existingClub.setLogo(image);
        }

        // Update other club details if needed
        existingClub.setNameC(club.getNameC());
        existingClub.setDescription(club.getDescription());

        return iClubRepository.save(existingClub);
    }


    @Override
    public void delete(Long numClub) {

        iClubRepository.deleteById(numClub);
    }

    @Override
    public Club getById(Long numClub) {

        return iClubRepository.findById(numClub).get();
    }

    @Override
    public List<Club> getAll() {

        return (List<Club>) iClubRepository.findAll();
    }


    public String generateQRCodeForClubWebsite(Club club) {
        String websiteUrl = club.getWebsiteURL();
        int width = 300;
        int height = 300;

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(websiteUrl, com.google.zxing.BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage bufferedImage = QRCodeImageUtil.toBufferedImage(bitMatrix);
            byte[] imageBytes = QRCodeImageUtil.toByteArray(bufferedImage, "png");

            String base64QRCode = Base64.getEncoder().encodeToString(imageBytes);

            return "data:image/png;base64," + base64QRCode;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getNumberOfDepartmentsPerClub(Long clubId) {
        Club club = iClubRepository.findById(clubId).orElse(null);
        return club != null ? club.getDepartments().size() : -1;
    }


    public int getNumberOfUsersPerClub(Long clubId) {
        Club club = iClubRepository.findById(clubId).orElse(null);
        return club != null ? club.getUsers().size() : -1;
    }


    public int getNumberOfEventsPerClub(Long clubId) {
        Club club = iClubRepository.findById(clubId).orElse(null);
        return club != null ? club.getEvents().size() : -1;
    }


    public void assignUserToClub(Long userId, Long clubId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Club club = iClubRepository.findById(clubId).orElseThrow(() -> new IllegalArgumentException("Club not found"));

        user.getClubs().add(club);
        userRepository.save(user);
    }


    public void assignUserToEvent(Long userId, int eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));

        user.getEvents().add(event);
        userRepository.save(user);
    }

    public void assignUserToTraining(Long userId, int trainingId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new IllegalArgumentException("Training not found"));

        user.getTrainings().add(training);
        userRepository.save(user);
    }


    public Map<String, Object> predictProjectsAndFunds(Long clubId, double averageProjectCost) {
        Map<String, Object> prediction = new HashMap<>();

        // Calculate total funds raised
        double totalFundsRaised = calculateTotalFundsRaised(clubId);

        // Determine number of projects currently associated with the club
        int numberOfProjects = calculateNumberOfProjects(clubId);

        if (totalFundsRaised == -1) {
            // Handle case where club is not found
            prediction.put("error", "Club not found");
            return prediction;
        }

        // Calculate available budget for new projects
        double availableBudget = totalFundsRaised - (numberOfProjects * averageProjectCost);

        // Estimate number of additional projects that can be undertaken
        int additionalProjects = (int) (availableBudget / averageProjectCost);

        // Calculate required funds for each additional project
        double fundsPerProject = availableBudget / additionalProjects;

        prediction.put("totalFundsRaised", totalFundsRaised);
        prediction.put("numberOfProjects", numberOfProjects);
        prediction.put("averageProjectCost", averageProjectCost);
        prediction.put("availableBudget", availableBudget);
        prediction.put("additionalProjects", additionalProjects);
        prediction.put("fundsPerProject", fundsPerProject);

        return prediction;
    }

    private double calculateTotalFundsRaised(Long clubId) {
        Club club = iClubRepository.findById(clubId).orElse(null);
        if (club != null) {
            List<FundRaisingC> fundraisings = club.getFundraisings();
            double totalFundsRaised = 0.0;
            for (FundRaisingC fundraising : fundraisings) {
                totalFundsRaised += fundraising.getSumFr();
            }
            return totalFundsRaised;
        }
        return -1; // Indicate club not found
    }

    private int calculateNumberOfProjects(Long clubId) {
        Club club = iClubRepository.findById(clubId).orElse(null);
        return club != null ? club.getProjects().size() : -1;
    }




   public Map<String, Object> predictProjectsAndFundsInPeriod(Long clubId, int periodInMonths) {
        Map<String, Object> prediction = new HashMap<>();

        // Calculate total funds raised by the club
        double totalFundsRaised = calculateTotalFundsRaised(clubId);

        // Historical data or estimation for average project cost
        double averageProjectCost = calculateAverageProjectCost(clubId); // Implement this method separately

        // Calculate the total number of months that the available funds can sustain the projects
        int monthsSustainable = totalFundsRaised > 0 ? (int) (totalFundsRaised / averageProjectCost) : 0;

        // Predict the number of projects feasible within the calculated sustainable period based on available funds
        int numberOfProjectsInPeriod = monthsSustainable > 0 ? (int) (monthsSustainable * 0.8) : 0; // 80% utilization for buffer

        // Calculate the required funds to complete the predicted projects if the available funds are insufficient
        double requiredFunds = Math.max(0, (numberOfProjectsInPeriod * averageProjectCost) - totalFundsRaised);

        // Provide advice based on the financial situation
        String adviceMessage = "";
        if (totalFundsRaised < requiredFunds) {
            double additionalFundsNeeded = requiredFunds - totalFundsRaised;

            if (totalFundsRaised == 0) {
                adviceMessage = "Consider starting fundraising initiatives to gather the necessary funds.";
            } else {
                int percentageIncrease = (int) ((additionalFundsNeeded / totalFundsRaised) * 100);
                adviceMessage = "You may need to increase your fundraising efforts by approximately " + percentageIncrease + "%.";
            }
        } else {
            double costPerProject = totalFundsRaised > 0 ? totalFundsRaised / numberOfProjectsInPeriod : 0;
            adviceMessage = "Evaluate ways to reduce the average project cost to make the budget more feasible, potentially by reviewing project scopes or seeking cost-effective alternatives.";
        }

        // Include the advice message in the prediction map
        prediction.put("adviceMessage", adviceMessage);

        // Populate the prediction map with detailed descriptions
       /* prediction.put("totalFundsRaised", totalFundsRaised);
        prediction.put("averageProjectCost", averageProjectCost);
        prediction.put("monthsSustainable", monthsSustainable);
        prediction.put("numberOfProjectsInPeriod", numberOfProjectsInPeriod);
        prediction.put("requiredFunds", requiredFunds);*/


        prediction.put("totalFundsRaised :  totalFundsRaised is the total amount of funds raised by the club", totalFundsRaised);
        prediction.put("averageProjectCost :  averageProjectCost is the average cost of a project, derived from historical data or estimations", averageProjectCost);
        prediction.put("monthsSustainable :  monthsSustainable is a variable representing the total number of months that the available funds can sustain the projects", monthsSustainable);
        prediction.put("numberOfProjectsInPeriod :  numberOfProjectsInPeriod is the predicted number of projects feasible within the calculated sustainable period based on available funds", numberOfProjectsInPeriod);
        prediction.put("requiredFunds :  requiredFunds is the additional funds required to complete the predicted projects if the available funds are insufficient", requiredFunds);




        // Return the populated prediction map
        return prediction;
    }




    /*public Set<Object> predictProjectsAndFundsInPeriodSet2(Long clubId, int periodInMonths) {
        Map<String, Object> prediction = new HashMap<>();

        // Calculate total funds raised by the club
        double totalFundsRaised = calculateTotalFundsRaised(clubId);

        // Historical data or estimation for average project cost
        double averageProjectCost = calculateAverageProjectCost(clubId); // Implement this method separately

        // Calculate the total number of months that the available funds can sustain the projects
        int monthsSustainable = totalFundsRaised > 0 ? (int) (totalFundsRaised / averageProjectCost) : 0;

        // Predict the number of projects feasible within the calculated sustainable period based on available funds
        int numberOfProjectsInPeriod = monthsSustainable > 0 ? (int) (monthsSustainable * 0.8) : 0; // 80% utilization for buffer

        // Calculate the required funds to complete the predicted projects if the available funds are insufficient
        double requiredFunds = Math.max(0, (numberOfProjectsInPeriod * averageProjectCost) - totalFundsRaised);

        // Provide advice based on the financial situation
        String adviceMessage = "";
        if (totalFundsRaised < requiredFunds) {
            double additionalFundsNeeded = requiredFunds - totalFundsRaised;

            if (totalFundsRaised == 0) {
                adviceMessage = "Consider starting fundraising initiatives to gather the necessary funds.";
            } else {
                int percentageIncrease = (int) ((additionalFundsNeeded / totalFundsRaised) * 100);
                adviceMessage = "You may need to increase your fundraising efforts by approximately " + percentageIncrease + "%.";
            }
        } else {
            double costPerProject = totalFundsRaised > 0 ? totalFundsRaised / numberOfProjectsInPeriod : 0;
            adviceMessage = "Evaluate ways to reduce the average project cost to make the budget more feasible, potentially by reviewing project scopes or seeking cost-effective alternatives.";
        }

        // Include the advice message in the prediction set
        Set<Object> predictionSet = new HashSet<>();
        predictionSet.add(adviceMessage);
        predictionSet.add(totalFundsRaised);
        predictionSet.add(averageProjectCost);
        predictionSet.add(monthsSustainable);
        predictionSet.add(numberOfProjectsInPeriod);
        predictionSet.add(requiredFunds);

        return predictionSet;
    }*/


    @Transactional
    public void predictProjectsAndFundsInPeriodSet2(Long clubId, int periodInMonths) {

        // Calculate total funds raised by the club
        double totalFundsRaised = calculateTotalFundsRaised(clubId);

        // Historical data or estimation for average project cost
        double averageProjectCost = calculateAverageProjectCost(clubId); // Implement this method separately

        // Calculate the total number of months that the available funds can sustain the projects
        int monthsSustainable = totalFundsRaised > 0 ? (int) (totalFundsRaised / averageProjectCost) : 0;

        // Predict the number of projects feasible within the calculated sustainable period based on available funds
        int numberOfProjectsInPeriod = monthsSustainable > 0 ? (int) (monthsSustainable * 0.8) : 0; // 80% utilization for buffer

        // Calculate the required funds to complete the predicted projects if the available funds are insufficient
        double requiredFunds = Math.max(0, (numberOfProjectsInPeriod * averageProjectCost) - totalFundsRaised);

        // Provide advice based on the financial situation
        String adviceMessage = "";
        if (totalFundsRaised < requiredFunds) {
            double additionalFundsNeeded = requiredFunds - totalFundsRaised;

            if (totalFundsRaised == 0) {
                adviceMessage = "Consider starting fundraising initiatives to gather the necessary funds.";
            } else {
                int percentageIncrease = (int) ((additionalFundsNeeded / totalFundsRaised) * 100);
                adviceMessage = "You may need to increase your fundraising efforts by approximately " + percentageIncrease + "%.";
            }
        } else {
            double costPerProject = totalFundsRaised > 0 ? totalFundsRaised / numberOfProjectsInPeriod : 0;
            adviceMessage = "Evaluate ways to reduce the average project cost to make the budget more feasible, potentially by reviewing project scopes or seeking cost-effective alternatives.";
        }

        // Create a Prediction entity and save it to the database
        Prediction predictionEntity = new Prediction();
        predictionEntity.setClubId(clubId);
        predictionEntity.setPeriodInMonths(periodInMonths);
        predictionEntity.setTotalFundsRaised(totalFundsRaised);
        predictionEntity.setAverageProjectCost(averageProjectCost);
        predictionEntity.setMonthsSustainable(monthsSustainable);
        predictionEntity.setNumberOfProjectsInPeriod(numberOfProjectsInPeriod);
        predictionEntity.setRequiredFunds(requiredFunds);
        predictionEntity.setAdviceMessage(adviceMessage);

        predictionRepository.save(predictionEntity);
    }

    public List<Prediction> getAllPredictions() {
        return predictionRepository.findAll();
    }



    public double calculateAverageProjectCost(Long clubId) {
        Club club = iClubRepository.findById(clubId).orElse(null);
        if (club != null) {
            List<ProjectC> projects = club.getProjects();

            if (projects != null && !projects.isEmpty()) {
                double totalCost = 0.0;
                for (ProjectC project : projects) {
                    totalCost += calculateProjectCost(project);
                }

                return totalCost / projects.size();
            } else {
                return 0.0; // Assuming zero cost if no projects are available
            }
        }
        return 0.0; // Indicate club not found or no projects available
    }
    private double calculateProjectCost(ProjectC project) {
        // Assuming resourcesP directly represents the project cost
        return project.getResourceP();
    }



  /*  public List<PredictionObject> convertPredictionMapToList(Map<String, Object> predictionMap, Long clubId, int periodInMonths) {
        List<PredictionObject> predictionList = new ArrayList<>();

        if (!predictionMap.isEmpty()) {
            PredictionObject predictionObject = new PredictionObject();

            // Extract and set each data point from the predictionMap
            if (predictionMap.containsKey("totalFundsRaised") && predictionMap.get("totalFundsRaised") instanceof Double) {
                predictionObject.setTotalFundsRaised((Double) predictionMap.get("totalFundsRaised"));
            } else {
                predictionObject.setTotalFundsRaised(0.0);
            }

            if (predictionMap.containsKey("averageProjectCost") && predictionMap.get("averageProjectCost") instanceof Double) {
                predictionObject.setAverageProjectCost((Double) predictionMap.get("averageProjectCost"));
            } else {
                predictionObject.setAverageProjectCost(0.0);
            }

            if (predictionMap.containsKey("monthsSustainable") && predictionMap.get("monthsSustainable") instanceof Integer) {
                predictionObject.setMonthsSustainable((Integer) predictionMap.get("monthsSustainable"));
            } else {
                predictionObject.setMonthsSustainable(0);
            }

            if (predictionMap.containsKey("numberOfProjectsInPeriod") && predictionMap.get("numberOfProjectsInPeriod") instanceof Integer) {
                predictionObject.setInPeriod((Integer) predictionMap.get("numberOfProjectsInPeriod"));
            } else {
                predictionObject.setInPeriod(0);
            }

           if (predictionMap.containsKey("requiredFunds") && predictionMap.get("requiredFunds") instanceof Double) {
                predictionObject.setRequiredFunds((Double) predictionMap.get("requiredFunds"));
            } else {
                predictionObject.setRequiredFunds(0.0);
            }


            if (predictionMap.containsKey("adviceMessage")) {
                predictionObject.setAdviceMessage((String) predictionMap.get("adviceMessage"));
            }

            // Add the populated PredictionObject to the list
            predictionList.add(predictionObject);
        }

        return predictionList;
    }*/





    private static final int involvementThreshold = 3; // Example threshold value - adjust as needed








    public Map<String, List<Map<String, Object>>> analyzeAttendanceAndPredictAttrition(Long clubId) {
        Club club = iClubRepository.findById(clubId).orElse(null);

        List<Map<String, Object>> predictedLeaversInfo = new ArrayList<>();
        List<Map<String, Object>> highlyInvolvedUsersInfo = new ArrayList<>();

        if (club != null) {
            Set<User> users = club.getUsers();

            for (User user : users) {
                int totalEventsAttended = eventRepository.countEventsByUser(user);
                int totalTrainingsAttended = trainingRepository.countTrainingsByUser(user);

                double expectedAttendanceValue = 1.0; // Example expected attendance value - replace with the actual value

                double attendanceScore = (totalEventsAttended + totalTrainingsAttended) / expectedAttendanceValue;
                double attritionProbability = 1 - attendanceScore;

                user.setAttritionProbability(attritionProbability);

                Map<String, Object> userInformation = new LinkedHashMap<>();
                userInformation.put("firstName", user.getFirstName());
                userInformation.put("lastName", user.getLastName());
                userInformation.put("email", user.getEmail());
                userInformation.put("role", user.getRole());
                userInformation.put("attritionProbability", attritionProbability);

                if (attendanceScore < involvementThreshold) {
                    userInformation.put("highlyInvolved", false);
                    predictedLeaversInfo.add(userInformation);
                } else {
                    userInformation.put("highlyInvolved", true);
                    highlyInvolvedUsersInfo.add(userInformation);
                }
                userRepository.save(user);
            }
        }

        // Generate messages for the predicted leavers and highly involved users
        String predictedLeaversMessage = "Users predicted to leave: " + predictedLeaversInfo.size() + " users";
        String highlyInvolvedUsersMessage = "Highly involved users: " + highlyInvolvedUsersInfo.size() + " users";

        Map<String, List<Map<String, Object>>> predictions = new HashMap<>();
        predictions.put(predictedLeaversMessage, predictedLeaversInfo);
        predictions.put(highlyInvolvedUsersMessage, highlyInvolvedUsersInfo);

        return predictions;
    }




    private double calculateAttritionProbability(User user) {
        int totalEventsAttended = eventRepository.countEventsByUser(user);
        int totalTrainingsAttended = trainingRepository.countTrainingsByUser(user);

        // Define or retrieve the expected attendance value based on club norms or historical data
        double expectedAttendanceValue = 50.0; // Example expected attendance value - replace with actual value

        // Calculate the attendance score and attrition probability
        double attendanceScore = (totalEventsAttended + totalTrainingsAttended) / expectedAttendanceValue;
        double attritionProbability = 1 - attendanceScore;

        return attritionProbability;
    }

    private void generateAnalysisReport(Set<User> users) {
        for (User user : users) {
            double attritionProbability = calculateAttritionProbability(user);

            // Generate a detailed analysis report for each user
            String report = "Analysis Report for User " + user.getFirstName() + " " + user.getLastName() + ": Attrition Probability - " + attritionProbability;
            System.out.println(report);
        }
    }

    private List<User> identifyPotentialLeavers(Set<User> users) {
        List<User> potentialLeavers = new ArrayList<>();

        for (User user : users) {
            double attritionProbability = calculateAttritionProbability(user);

            if (attritionProbability > 0.5) { // Set a threshold for potential leavers
                potentialLeavers.add(user);
            }
        }

        return potentialLeavers;
    }




    private void generateAnalysisReport(List<User> potentialLeavers, List<User> highlyInvolvedUsers) {
        for (User user : potentialLeavers) {
            String message = "User " + user.getFirstName() + " " + user.getLastName() + " is predicted to leave the club due to low attendance";
            System.out.println(message);
        }

        for (User user : highlyInvolvedUsers) {
            String message = "User " + user.getFirstName() + " " + user.getLastName() + " is highly involved with the club due to high attendance";
            System.out.println(message);
        }
    }


    private List<User> identifyHighlyInvolvedUsers(Set<User> users) {
        List<User> highlyInvolvedUsers = new ArrayList<>();

        for (User user : users) {
            if (user.isHighlyInvolved()) { // Assuming there is a method to check if the user is highly involved
                highlyInvolvedUsers.add(user);
            }
        }

        return highlyInvolvedUsers;
    }










}



