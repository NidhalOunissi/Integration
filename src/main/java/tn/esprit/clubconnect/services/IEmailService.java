package tn.esprit.clubconnect.services;

import tn.esprit.clubconnect.entities.User;

public interface IEmailService {
    void sendValidationEmail(User user);

}
