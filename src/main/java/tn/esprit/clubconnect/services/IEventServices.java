package tn.esprit.clubconnect.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.Event;

import java.util.List;

public interface IEventServices {
    Event addEvent(Event event, MultipartFile imageFile);
    Event updateEvent(Event event);
    void deleteEvent( int idE);
    Event getbyid (int idE);
    List<Event> getall();


    void addEventWithImage(Event event, MultipartFile imageFile);
    public Event updateEventById(int id, Event updatedEvent);

    public Event paiement (int idE);


}
