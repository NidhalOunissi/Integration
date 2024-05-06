package tn.esprit.clubconnect.services;

import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.Training;
import tn.esprit.clubconnect.entities.User;

import java.io.IOException;
import java.util.List;

public interface ITrainingServices {
    Training addTraining(Training training, MultipartFile imageFile);
    Training updateTraining(int idT , Training training);
    void deleteTraining(Integer idT);
    Training getById(Integer idT);
    List<Training> getAll();
    User assignToTraining( Integer idT) throws BadRequestException;
    void upcomingTrainings() throws BadRequestException;
    List<Training> searchTrainings(String searchTerm);
    byte[] generateChartImage();
    byte[] generateTrainingExcel(List<Training> trainings);
    byte[] generateQRCode(Training training) throws IOException;
    List<Training> trainingDiscount(Integer userId);
}
