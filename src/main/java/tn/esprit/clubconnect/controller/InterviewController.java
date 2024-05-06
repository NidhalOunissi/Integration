package tn.esprit.clubconnect.controller;

import lombok.Builder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import tn.esprit.clubconnect.entities.Interview;
import tn.esprit.clubconnect.services.IInterviewServices;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Interviews")
@Builder
public class InterviewController {

    private final IInterviewServices interviewService;

    @Autowired
    public InterviewController(IInterviewServices interviewService) {
        this.interviewService = interviewService;
    }
    public static String uploadDirectory= System.getProperty("user.dir")+"/src/main/resources/static/images";

//    @PostMapping("/addInterview")
//    public ResponseEntity<Interview> addInterview(@RequestBody Interview interview) {
//        Interview addedInterview = interviewService.addInterview(interview);
//        return new ResponseEntity<>(addedInterview, HttpStatus.CREATED);
//    }

    @PutMapping("/updateInterview/{id}")
public Interview updateInterview(@PathVariable Integer id, @RequestPart Interview interview, @RequestParam("images") List<MultipartFile> files) throws IOException {
    // Retrieve the existing interview
    Interview existingInterview = interviewService.getInterviewById(id);

        // Update the interview's fields
        existingInterview.setTitle(interview.getTitle());
        existingInterview.setDate(interview.getDate());
        existingInterview.setTime(interview.getTime());
        existingInterview.setLocation(interview.getLocation());
        existingInterview.setInterviewer(interview.getInterviewer());
        existingInterview.setRequiredSkills(interview.getRequiredSkills());
        existingInterview.setContactEmail(interview.getContactEmail());
        existingInterview.setContactPhone(interview.getContactPhone());
        existingInterview.setRegistrationDeadline(interview.getRegistrationDeadline());
        existingInterview.setMaxParticipants(interview.getMaxParticipants());
        existingInterview.setDuration(interview.getDuration());
        existingInterview.setDescription(interview.getDescription());
        //

    // Handle image uploads
    List<String> imageNames = new ArrayList<>();
    for (MultipartFile file : files) {
        String originalFilename = file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
        Files.write(fileNameAndPath, file.getBytes());
        imageNames.add(originalFilename);
    }
    // Update the interview's images
    existingInterview.setImages(imageNames);

    // Save the updated interview
    return interviewService.updateInterview(existingInterview);
}




    @DeleteMapping("/deleteInterview/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Integer id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getInterviewById/{id}")
    public ResponseEntity<Interview> getInterviewById(@PathVariable Integer id) {
        Interview interview = interviewService.getInterviewById(id);
        if (interview == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(interview, HttpStatus.OK);
    }


    @GetMapping("/getAllInterviews")
    public List<Interview> getAllInterviews() {
        return interviewService.getAllInterviews();
    }

    @GetMapping("/exportInterviewsToExcel")
    public ResponseEntity<StreamingResponseBody> exportInterviewsToExcel() {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header("Content-Disposition", "attachment; filename=interviews.xlsx")
                .body(outputStream -> {
                    try (Workbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("Interviews");

                        // Header row
                        Row headerRow = sheet.createRow(0);
                        headerRow.createCell(0).setCellValue("ID");
                        headerRow.createCell(1).setCellValue("Title");
                        headerRow.createCell(2).setCellValue("Date");
                        headerRow.createCell(3).setCellValue("Time");
                        headerRow.createCell(4).setCellValue("Location");
                        headerRow.createCell(5).setCellValue("Interviewer");
                        headerRow.createCell(6).setCellValue("Required Skills");
                        headerRow.createCell(7).setCellValue("Contact Email");
                        headerRow.createCell(8).setCellValue("Contact Phone");
                        headerRow.createCell(9).setCellValue("Registration Deadline");
                        headerRow.createCell(10).setCellValue("Max Participants");
                        headerRow.createCell(11).setCellValue("Duration");
                        headerRow.createCell(12).setCellValue("Description");


                        // Data rows
                        List<Interview> interviews = interviewService.getAllInterviews();
                        int rowNum = 1;
                        for (Interview interview : interviews) {
                            Row row = sheet.createRow(rowNum++);
                            row.createCell(0).setCellValue(interview.getId());
                            row.createCell(1).setCellValue(interview.getTitle());
                            row.createCell(2).setCellValue(interview.getDate().toString());
                            row.createCell(3).setCellValue(interview.getTime().toString());
                            row.createCell(4).setCellValue(interview.getLocation());
                            row.createCell(5).setCellValue(interview.getInterviewer());
                            row.createCell(6).setCellValue(interview.getRequiredSkills());
                            row.createCell(7).setCellValue(interview.getContactEmail());
                            row.createCell(8).setCellValue(interview.getContactPhone());
                            row.createCell(9).setCellValue(interview.getRegistrationDeadline().toString());
                            row.createCell(10).setCellValue(interview.getMaxParticipants());
                            row.createCell(11).setCellValue(interview.getDuration());
                            row.createCell(12).setCellValue(interview.getDescription());

                        }

                        // Write to output stream
                        workbook.write(outputStream);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to export interviews to Excel", e);
                    }
                });


    }

////    // public static String uploadDirectory = System.getProperty("user.dir") + "C:" + File.separator +"xampp" + File.separator + "htdocs" + File.separator + "img" + File.separator + "imgPI";

    @PostMapping("/addInterviewWithImage")
    public Interview addInterviewWithImage(@RequestPart Interview interview, @RequestParam("images") List<MultipartFile> files) throws IOException {
        List<String> imageNames = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
            Files.write(fileNameAndPath, file.getBytes());
            imageNames.add(originalFilename);
        }

        interview.setImages(imageNames);
        return interviewService.addInterviewWithImage(interview);
    }



    @PutMapping("/shareInterview/{id}")
    public Interview shareInterview(@PathVariable Integer id) {
        // Retrieve the existing interview
        Interview existingInterview = interviewService.getInterviewById(id);

        // Toggle the share attribute between 0 and 1
        existingInterview.setShare(existingInterview.getShare() == 0 ? 1 : 0);

        // Save the updated interview
        return interviewService.updateInterview(existingInterview);
    }


}