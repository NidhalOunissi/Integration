package tn.esprit.clubconnect.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.clubconnect.requestAndresponse.*;
import tn.esprit.clubconnect.services.AuthenticationService;
import tn.esprit.clubconnect.services.IAuthenticationService;
import tn.esprit.clubconnect.services.IMemberService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final IMemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> register (RegisterRequest request) throws MessagingException {

        var response = authenticationService.register(request);
        if(request.isTfaEnbaled())
            return ResponseEntity.ok(response);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode (@RequestBody VerificationRequest request){
        return ResponseEntity.ok(authenticationService.verifyCode(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }


    @PutMapping("/activate-account")
    public void confirm(@RequestParam String token) throws MessagingException {
        authenticationService.confirmAccount(token);
    }

    @PutMapping("/reset-password-request")
    public void resetPasswordMail(@RequestParam String email) {
        memberService.sendForgotPasswordMail(email);
    }

    @PutMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest, String token) {
        memberService.resetPassword(resetPasswordRequest, token);
    }


}
