package tn.esprit.clubconnect.services;

import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.requestAndresponse.ChangePasswordRequest;
import tn.esprit.clubconnect.requestAndresponse.ResetPasswordRequest;

import java.security.Principal;

public interface IMemberService {

    void desactivateAccount (int idU);
    void joinRequest (int idC, int idU);
    void updateProfile (User user, int idUe);
    void quitClub (int idC, int idU);
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
    void resetPassword(ResetPasswordRequest request, String email);
    void sendForgotPasswordMail(String email);
}
