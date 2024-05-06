package tn.esprit.clubconnect.services;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.coyote.BadRequestException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.Image;
import tn.esprit.clubconnect.entities.Training;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.repositories.IImageRepository;
import tn.esprit.clubconnect.repositories.ITrainingRepository;
import tn.esprit.clubconnect.repositories.IUserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
@Component
@RequiredArgsConstructor
@Service
public class TrainingServices implements ITrainingServices {
    private final ITrainingRepository trainingRepository;
    private final IUserRepository userRepository;
    private final CloudinaryServiceImpl cloudinaryService;
    private final IImageRepository imageRepository;


    @Override
    public Training addTraining(Training training, MultipartFile imageFile) {

        String imageUrl = cloudinaryService.uploadFile(imageFile, "event_images"); // Specify folder


        if (imageUrl == null) {
            throw new RuntimeException("failed");
        }

        Image image = Image.builder()
                .name(imageFile.getOriginalFilename())
                .url(imageUrl)
                .build();

        imageRepository.save(image);

        training.setPoster(image);

        return trainingRepository.save(training);
    }

    /*
        @Override
        public Training updateTraining(Training training) {
            return trainingRepository.save(training);
        }*/
    @Override
    public Training updateTraining(int idT, Training trainingDetails) {
        Training training = getById(idT);
        if (training != null) {
            training.setTitleT(trainingDetails.getTitleT());
            // Assuming you want to update other fields if necessary, uncomment and adjust:
            training.setDescription(trainingDetails.getDescription());
            training.setNumberPlace(trainingDetails.getNumberPlace());
            // training.setStartDate(trainingDetails.getStartDate());
            // training.setDuration(trainingDetails.getDuration());
            training.setLocation(trainingDetails.getLocation());
            training.setPrice(trainingDetails.getPrice());
            // You might need to handle related entities like poster, clubs, users if applicable
            return trainingRepository.save(training);
        }
        return null;
    }


    @Override
    public void deleteTraining(Integer idT) {
        trainingRepository.deleteById(idT);
    }

    @Override
    public Training getById(Integer idT) {
        return trainingRepository.findById(idT).orElse(null);
    }

    @Override
    public List<Training> getAll() {
        return trainingRepository.findAll();
    }

    @Override
    public User assignToTraining( Integer idT) throws BadRequestException {
        User user = userRepository.findById(1).orElse(null);
        Training training = trainingRepository.findById(idT).orElse(null);
        if (user == null || training == null) {
            throw new BadRequestException("Training or user does not exist");
        } else if (training.getStartDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Training date has expired");
        } else if (training.getNumberPlace() == 0) {
            throw new BadRequestException("No available places");
        } else if (user.getTrainings().contains(training)) {
            throw new BadRequestException("User is already registered");
        } else {
            Set<Training> userTrainings = user.getTrainings();
            userTrainings.add(training);
            training.setNumberPlace(training.getNumberPlace() - 1);
            training.setNbParticipants(training.getNbParticipants() + 1);
            user.setTrainings(userTrainings);
            trainingRepository.save(training);
            return userRepository.save(user);
        }
    }

    @Scheduled(cron = "0 34 11 * * *") // Exécute à 9h30 chaque jour
    @Override
    public void upcomingTrainings() throws BadRequestException {
        List<String> listTrainings = new ArrayList<>();

        // Récupérer tous les utilisateurs
        List<User> users = (List<User>) userRepository.findAll();

        for (User user : users) {
            Hibernate.initialize(user.getTrainings());
            listTrainings.clear();
            for (Training training : user.getTrainings()) {
                long daysUntilStart = ChronoUnit.DAYS.between(LocalDate.now(), training.getStartDate().toLocalDate());
                if (daysUntilStart == 1) {
                    listTrainings.add(training.getTitleT());
                    System.out.println(training.getTitleT() + "* for the user " + user.getIdU());
                }
            }

            if (!listTrainings.isEmpty()) {
                String ACCOUNT_SID = "ACf0900874f5973a30bea1ef066419dd93";
                String AUTH_TOKEN = "b13f2f5931585bd09152ffba2f20694f";
                AtomicReference<String> trainingsNames = new AtomicReference<>("");
                listTrainings.forEach(s -> trainingsNames.set(trainingsNames.get() + s + " "));

                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                Message message = Message.creator(
                                new com.twilio.type.PhoneNumber("+216" + user.getPhoneNumber()),
                                new com.twilio.type.PhoneNumber("+12248756334"),
                                "training date is tomorrow : " + trainingsNames)
                        .create();
            }
        }
    }

    @Override
    public List<Training> searchTrainings(String searchTerm) {
        // Convert the search term to lowercase for case-insensitive search
        String searchQuery = searchTerm.toLowerCase();

        // Parse the search query to check if it's a date
        LocalDateTime startDate = null;
        try {
            startDate = LocalDateTime.parse(searchQuery);
        } catch (DateTimeParseException e) {
            // Ignore parsing error, continue searching
        }

        // Perform the search
        return trainingRepository.findByTitleTContainingIgnoreCaseOrStartDateOrLocationContainingIgnoreCase(
                searchTerm, startDate, searchTerm);
    }

    @Override
    public byte[] generateChartImage() {
// Récupérer les données des formations depuis votre service ou repository
        List<Training> trainings = trainingRepository.findAll(); // Supposons que vous utilisez JPA et que vous avez un repository pour les formations

        // Créer un dataset avec les données des formations et les nombres de places
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Training training : trainings) {
            dataset.setValue(training.getTitleT() + " (" + training.getNumberPlace() + " places)", training.getNumberPlace());
        }

        // Créer un graphique à partir du dataset
        JFreeChart chart = ChartFactory.createPieChart("Number of places per training", dataset, true, true, false);

        // Convertir le graphique en image
        byte[] imageBytes = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, 800, 600); // Ajustez la taille de l'image selon vos besoins
            imageBytes = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageBytes;
    }

    @Override
    public byte[] generateTrainingExcel(List<Training> trainings) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Formations");

            // Créer l'en-tête
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Titre", "Description", "Nombre de places", "Date de début", "Durée", "Lieu", "Prix"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Remplir les données des formations
            int rowNum = 1;
            for (Training training : trainings) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(training.getTitleT());
                row.createCell(1).setCellValue(training.getDescription());
                row.createCell(2).setCellValue(training.getNumberPlace());
                row.createCell(3).setCellValue(training.getStartDate().toString());
                row.createCell(4).setCellValue(training.getDuration().toString());
                row.createCell(5).setCellValue(training.getLocation());
                row.createCell(6).setCellValue(training.getPrice());
            }

            // Écrire le contenu dans un flux de sortie
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] generateQRCode(Training training) throws IOException {
        // Construire le contenu du QR code en combinant les détails de la formation
        String qrContent = "Title: " + training.getTitleT() + "\n"
                + "Description: " + training.getDescription() + "\n"
                + "Number of Places: " + training.getNumberPlace() + "\n"
                + "Start Date: " + training.getStartDate() + "\n"
                + "Duration: " + training.getDuration() + "\n"
                + "Location: " + training.getLocation() + "\n"
                + "Price: " + training.getPrice();

        // Générer le QR code en utilisant la bibliothèque QRGen
        ByteArrayOutputStream outputStream = QRCode.from(qrContent).to(ImageType.PNG).stream();

        // Convertir le flux de sortie en un tableau de bytes
        byte[] qrCodeBytes = outputStream.toByteArray();

        // Fermer le flux de sortie
        outputStream.close();

        // Retourner le tableau de bytes contenant le QR code
        return qrCodeBytes;
    }

    @Override
    public List<Training> trainingDiscount(Integer userId) {
        // Calculer la date actuelle
        LocalDateTime currentDate = LocalDateTime.now();

        // Calculer la date dans trois jours
        LocalDateTime threeDaysLater = currentDate.plusDays(3);

        // Récupérer tous les trainings dont la date de début est après la date actuelle et avant la date dans trois jours, et le nombre de places est supérieur à zéro
        List<Training> trainings = trainingRepository.findByStartDateBetweenAndNumberPlaceGreaterThan(currentDate, threeDaysLater, 0);

        // Vérifier si l'utilisateur est celui qui a droit à la remise
        if (userId != null && userId == 1) {
            Random random = new Random();
            for (Training training : trainings) {
                // Vérifier si l'utilisateur actuel est déjà un gagnant pour cet entraînement
                if (!training.getUserWinners().contains(userId)) {
                    // Générer un nombre aléatoire entre 0 et 1
                    double randomNumber = random.nextDouble();
                    // Si le nombre aléatoire est inférieur à 0.3 (30% de chance), appliquer une remise aléatoire entre 10% et 50%
                    if (randomNumber < 0.3) {
                        double discount = (random.nextDouble() * 0.4) + 0.1; // Entre 0.1 (10%) et 0.5 (50%)
                        double discountedPrice = training.getPrice() * (1 - discount);
                        // Arrondir le prix remisé à un seul chiffre après le virgule
                        discountedPrice = Math.round(discountedPrice * 10.0) / 10.0;
                        training.setPrice(discountedPrice);

                        // Ajouter l'ID de l'utilisateur actuel à la liste des gagnants pour cet entraînement
                        training.getUserWinners().add(userId);
                        System.out.println("Congratulations! You have been selected as the winner of the " + training.getTitleT() + " training.");
                    }
                } else {
                    System.out.println("Sorry, you have already received a discount for the training " + training.getTitleT());
                }
            }

            // Enregistrer les modifications dans la base de données
            trainingRepository.saveAll(trainings);
        }

        return trainings;
    }



}