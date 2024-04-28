package tn.esprit.clubconnect.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.Event;

@Service
@RequiredArgsConstructor
public class ClubAdminService implements IClubAdminService {

    @Override
    public void addClub(Club club) {

    }

    @Override
    public void deleteClub(int idC) {

    }

    @Override
    public void deleteMember(int idC, int idU) {

    }

    @Override
    public void addEvent(Event event) {

    }

    @Override
    public void deleteEvent(int idE) {

    }
}
