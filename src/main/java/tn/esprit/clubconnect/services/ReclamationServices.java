package tn.esprit.clubconnect.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.Reclamation;
import tn.esprit.clubconnect.entities.TypeR;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.repositories.IReclamationRepository;
import tn.esprit.clubconnect.repositories.IUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@Primary pour dire que c un bean par defaut
@AllArgsConstructor
public class ReclamationServices implements IReclamationServices{
    @Autowired
    IReclamationRepository reclamationRepository;
    IUserRepository userRepository;

    @Override
    public Reclamation addReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation updateReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    @Override
    public void delete(Long idR) {
        reclamationRepository.deleteById(idR);

    }

    @Override
    public Reclamation getById(Long idR) {
        return reclamationRepository.findById(idR).orElse(null);
    }

    @Override
    public List<Reclamation> getAll() {
        return reclamationRepository.findAll();
    }

    public List<Reclamation> getReclamationsByPriority() {
        List<Reclamation> reclamations = reclamationRepository.findAll();
        List<Reclamation> urgentReclamations = new ArrayList<>();
        List<Reclamation> otherReclamations = new ArrayList<>();

        // Séparer les réclamations en "urgent" et "autre"
        for (Reclamation reclamation : reclamations) {
            if (reclamation.getDescription().toLowerCase().contains("urgent") ||
                    reclamation.getDescription().toLowerCase().contains("urgently")) {
                urgentReclamations.add(reclamation);
            } else {
                otherReclamations.add(reclamation);
            }
        }

        // Placer les réclamations "urgent" en tête de liste
        urgentReclamations.addAll(otherReclamations);
        return urgentReclamations;
    }

    public Map<TypeR, Long> getReclamationStatisticsByType() {
        List<Object[]> result = reclamationRepository.countByType();
        return result.stream()
                .collect(Collectors.toMap(
                        data -> (TypeR) data[0], // Cast to TypeR
                        data -> (Long) data[1] // Cast to Long
                ));
    }

    public TypeR getTypeWithHighestAverageRate() {
        Map<TypeR, Long> statistics = getReclamationStatisticsByType();

        // Calculer la somme des taux pour chaque type
        Map<TypeR, Long> totalRates = new HashMap<>();
        for (Map.Entry<TypeR, Long> entry : statistics.entrySet()) {
            totalRates.put(entry.getKey(), entry.getValue());
        }

        // Calculer la moyenne de rate pour chaque type
        Map<TypeR, Double> averageRates = new HashMap<>();
        for (Map.Entry<TypeR, Long> entry : totalRates.entrySet()) {
            long count = getCountOfReclamationsByType(entry.getKey());
            averageRates.put(entry.getKey(), count > 0 ? entry.getValue() / (double) count : 0);
        }

        // Trouver le type avec la moyenne de rate la plus élevée
        return averageRates.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    // Méthode pour compter le nombre de réclamations par type
    private long getCountOfReclamationsByType(TypeR type) {
        List<Object[]> result = reclamationRepository.countByType();
        return result.stream()
                .filter(array -> array[0].equals(type))
                .mapToLong(array -> (Long) array[1])
                .findFirst()
                .orElse(0L);
    }

    public Reclamation addAndAssignReclamationToUser(Reclamation reclamation) {
        // Retrieve the user with ID 1
        User user = userRepository.findById(1).orElse(null); // Assuming you have UserRepository injected

        if (user != null) {
            // Assign the user to the reclamation
            reclamation.setUser(user);

            // Save the reclamation with the assigned user
            return reclamationRepository.save(reclamation);
        } else {
            throw new IllegalArgumentException("User with ID 1 not found.");
        }


    }

}

