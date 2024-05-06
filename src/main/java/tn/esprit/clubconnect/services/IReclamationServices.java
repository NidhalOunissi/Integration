package tn.esprit.clubconnect.services;

import tn.esprit.clubconnect.entities.Reclamation;

import java.util.List;

public interface IReclamationServices {
    Reclamation addReclamation(Reclamation reclamation);
    Reclamation updateReclamation(Reclamation reclamation);
    void delete(Long idR);
    Reclamation getById(Long idR);
    List<Reclamation> getAll();
    // Définition de la méthode de requête personnalisée pour obtenir des statistiques sur les réclamations en fonction de leurs types


}
