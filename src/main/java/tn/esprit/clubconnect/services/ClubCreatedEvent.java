package tn.esprit.clubconnect.services;

import org.springframework.context.ApplicationEvent;

public class ClubCreatedEvent extends ApplicationEvent {
    private String clubName;

    public ClubCreatedEvent(Object source, String clubName) {
        super(source);
        this.clubName = clubName;
    }

    public String getClubName() {
        return clubName;
    }
}
