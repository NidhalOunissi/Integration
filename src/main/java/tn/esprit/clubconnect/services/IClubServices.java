package tn.esprit.clubconnect.services;


import tn.esprit.clubconnect.entities.Club;

import java.util.List;

public interface IClubServices {

    Club getById (Integer idC);
    List<Club> getAll();
}
