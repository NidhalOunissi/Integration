package tn.esprit.clubconnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import tn.esprit.clubconnect.entities.Event;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.requestAndresponse.ChangePasswordRequest;
import tn.esprit.clubconnect.services.ICloudinaryService;
import tn.esprit.clubconnect.services.IEventServices;
import tn.esprit.clubconnect.services.IMemberService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {


    private final IMemberService memberService;
    private final IEventServices eventServices;
    private final ICloudinaryService cloudinaryService;


    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
            ){
        memberService.changePassword(request, connectedUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/event/get/{idE}")
    public Event getEvent(@PathVariable int idE){return  eventServices.getbyid(idE);}


    @PutMapping("/event/paiement/{idE}")
    public Event paiement(@PathVariable int idE){return eventServices.paiement(idE);}

    @GetMapping("/event/all")
    public List<Event> getall(){
        return eventServices.getall();
    }








}
