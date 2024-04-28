package tn.esprit.clubconnect.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.Event;
import tn.esprit.clubconnect.entities.Rating;
import tn.esprit.clubconnect.repositories.IEventRepository;
import tn.esprit.clubconnect.repositories.IratingRepository;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final IratingRepository iratingRepository;
    private final IEventRepository eventRepository;

    @Transactional
    public Rating addrating(int id , int rating){

        Event event=eventRepository.findById(id).orElse(null);


        Rating rating1=new Rating();
        rating1.setEvent(event);
        rating1.setRating(rating);

        return iratingRepository.save(rating1);


    }

}
