package tn.esprit.clubconnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.clubconnect.entities.Rating;
import tn.esprit.clubconnect.services.RatingService;

@RestController
@RequiredArgsConstructor
public class RatingController {


    private final RatingService ratingService;
@PostMapping("rating/{id}/{rating}")
    public Rating addrating(@PathVariable("id") int id ,@PathVariable("rating") int rating) {
            return ratingService.addrating(id,rating);

    }





    }
