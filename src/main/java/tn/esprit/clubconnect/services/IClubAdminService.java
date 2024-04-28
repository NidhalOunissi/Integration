package tn.esprit.clubconnect.services;

import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.Event;
import tn.esprit.clubconnect.entities.User;


public interface IClubAdminService {

    void addClub (Club club);
    void deleteClub (int idC);
    void deleteMember (int idC, int idU);
    void addEvent (Event event);
    void deleteEvent (int idE);




}
