package tn.esprit.clubconnect.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.Club;

import java.util.List;
import java.util.Map;

public interface IClubServices {

    Club addClub(Club club, MultipartFile file);
    public Club update(Club club, MultipartFile newImageFile);
   // Club update(Club club);
    void delete(Long numClub);
    Club getById(Long numClub);
    List<Club> getAll();
    public String generateQRCodeForClubWebsite(Club club);
    public int getNumberOfDepartmentsPerClub(Long clubId);
    public int getNumberOfUsersPerClub(Long clubId);
    public int getNumberOfEventsPerClub(Long clubId);

    /*public double calculateTotalFundsRaised(Long clubId);
    public int calculateNumberOfProjects(Long clubId);*/

    public void assignUserToTraining(Long userId, int trainingId);
    public void assignUserToEvent(Long userId, int eventId);
    public void assignUserToClub(Long userId, Long clubId);

    public Map<String, Object> predictProjectsAndFunds(Long clubId, double averageProjectCost);

    public Map<String, Object> predictProjectsAndFundsInPeriod(Long clubId, int periodInMonths);
// public Set<User> analyzeAttendanceAndPredictAttrition(Long clubId);
// public List<Map<String, Object>> analyzeAttendanceAndPredictAttrition(Long clubId);
 //public Map<String, List<Map<String, Object>>> analyzeAttendanceAndPredictAttrition(Long clubId);

 //public List<Map<String, Object>> analyzeAttendanceAndPredictAttrition(Long clubId);
 public Map<String, List<Map<String, Object>>> analyzeAttendanceAndPredictAttrition(Long clubId);
 //public List<PredictionObject> convertPredictionMapToList(Map<String, Object> predictionMap);
 //public List<PredictionObject> convertPredictionMapToList(Map<String, Object> predictionMap, Long clubId, int periodInMonths);

 //public Set<Object> predictProjectsAndFundsInPeriodSet2(Long clubId, int periodInMonths);
 public void predictProjectsAndFundsInPeriodSet2(Long clubId, int periodInMonths);




}
