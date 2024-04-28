package tn.esprit.clubconnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clubadmin")
@RequiredArgsConstructor
public class ClubAdminController {

    @GetMapping
    public String get(){
        return "Ena el GET mta3 el CLUBadmin";
    }


    @PostMapping
    public String post(){
        return "Ena el POST mta3 el CLUBadmin";
    }


    @PutMapping
    public String put(){
        return "Ena el PUT mta3 el CLUBadmin";
    }

    @DeleteMapping
    public String delete(){
        return "Ena el DELETE mta3 el CLUBadmin";
    }

}
