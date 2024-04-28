package tn.esprit.clubconnect.services;

import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.Role;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.requestAndresponse.RoleChangeRequest;

import java.util.List;

public interface IAdminService  {

    List<User> getAllUsers ();
    void deleteUser (int id);
    void changeUserRole (int id, RoleChangeRequest request);
    void lockUser (int id);
    List<Club> getAllClubs();




}
