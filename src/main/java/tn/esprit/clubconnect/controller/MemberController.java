package tn.esprit.clubconnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.requestAndresponse.ChangePasswordRequest;
import tn.esprit.clubconnect.requestAndresponse.UpdateProfileRequest;
import tn.esprit.clubconnect.services.IMemberService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {


    private final IMemberService memberService;

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
            ){
        memberService.changePassword(request, connectedUser);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request,@RequestParam int idU){
        memberService.updateProfile(request,idU);
        return ResponseEntity.ok().build();
    }





}
