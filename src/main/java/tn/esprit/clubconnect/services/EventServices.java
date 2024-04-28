package tn.esprit.clubconnect.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.Event;
import tn.esprit.clubconnect.entities.Image;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.repositories.IClubRepository;
import tn.esprit.clubconnect.repositories.IEventRepository;
import tn.esprit.clubconnect.repositories.IImageRepository;
import tn.esprit.clubconnect.repositories.IUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServices implements IEventServices {

    private final IImageService imageService;
    private final IEventRepository eventRepository;
    private final ICloudinaryService cloudinaryService;
    private final IImageRepository imageRepository;
private final IClubRepository clubRepository;
private  final IUserRepository userRepository;

    @Override
    public Event addEvent(Event event, MultipartFile imageFile) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }
    @Override
    public Event updateEventById(int id, Event updatedEvent) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            event.setTitle(updatedEvent.getTitle());
            event.setDescription(updatedEvent.getDescription());
            event.setStartDate(updatedEvent.getStartDate());
            event.setLocation(updatedEvent.getLocation());
            event.setImage(updatedEvent.getImage());
            event.setClub(updatedEvent.getClub());
            // Assurez-vous de mettre à jour tous les autres champs nécessaires

            return eventRepository.save(event);
        }
        return null;
    }
   @Transactional
    @Override
    public Event paiement(int idE) {
        Event event = eventRepository.findById(idE).orElse(null);
       int idU=1;
        User user =  userRepository.findById(idU).orElse(null);
        event.setUser(user);

        return eventRepository.save(event);
    }





    @Override
    public void deleteEvent(int idE) {
        eventRepository.deleteById(idE);
    }

    @Override
    public Event getbyid(int idE) {
        return eventRepository.findById(idE).orElse(null);
    }

    @Override
    public List<Event> getall() {
        return (List<Event>) eventRepository.findAll();
    }




    @Transactional
    public void addEventWithImage(Event event, MultipartFile imageFile) {
        try {
            // Upload the image to Cloudinary (choose the appropriate method)
            String imageUrl = cloudinaryService.uploadFile(imageFile, "event_images"); // Specify folder

            if (imageUrl == null) {
                // Handle upload failure (e.g., log error, return informative message)
                throw new RuntimeException("Failed to upload image to Cloudinary!");
            }

            // Create and save Image object using @Builder
            Image image = Image.builder()
                    .name(imageFile.getOriginalFilename())
                    .url(imageUrl)
                    .build();

            imageRepository.save(image);

            // Set the image on the Event object and save
            event.setImage(image);
            eventRepository.save(event);
        } catch (Exception e) {
            // Handle other exceptions (e.g., database errors)
            e.printStackTrace();
        }

    }


}
