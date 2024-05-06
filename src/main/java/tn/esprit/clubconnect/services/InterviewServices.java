package tn.esprit.clubconnect.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.Interview;
import tn.esprit.clubconnect.repositories.InterviewRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class InterviewServices implements IInterviewServices {

    InterviewRepository interviewRepository;

    @Override
    public Interview addInterviewWithImage(Interview interview) {
        return interviewRepository.save(interview);
    }

    @Override
    public Interview updateInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    @Override
    public void deleteInterview(Integer idI) {
        interviewRepository.deleteById(idI);
    }

    @Override
    public Interview getInterviewById(Integer idI) {
        return interviewRepository.findById(idI).orElse(null);
    }

    @Override
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    @Override
    public void exportInterviewsToExcel(String filePath) {
        List<Interview> interviews = interviewRepository.findAll();
        // Logic to export interviews to Excel
    }


}
