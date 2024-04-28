package tn.esprit.clubconnect.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.Interview;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.repositories.IClubRepository;
import tn.esprit.clubconnect.repositories.IInterviewRepository;
import tn.esprit.clubconnect.repositories.IUserRepository;
import tn.esprit.clubconnect.requestAndresponse.ChangePasswordRequest;
import tn.esprit.clubconnect.requestAndresponse.ResetPasswordRequest;
import tn.esprit.clubconnect.token.ITokenRepository;
import tn.esprit.clubconnect.token.Token;

import java.security.Principal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MemberService implements IMemberService{

    private final IUserRepository userRepository;
    private final IClubRepository clubRepository;
    private final IInterviewRepository iInterviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final ITokenRepository tokenRepository;
    private final String  resetPwd = "http://localhost:4200/";



    @Override
    public void desactivateAccount(int idU) {
        userRepository.deleteById(idU);
    }

    @Override
    public void joinRequest(int idC, int idU) {
        User user = userRepository.findById(idU).orElse(null);
        assert user != null;
        Interview interview = Interview.builder()
                .candidateName(user.getFirstName()+" "+ user.getLastName()) // Replace with actual candidate name
                .candidateEmail(user.getEmail()) // Replace with actual candidate email
                .build();

    }

    @Override
    public void updateProfile(User user, int idU) {
        user = userRepository.findById(idU).orElse(null);
        assert user != null;
        userRepository.save(user);

    }

    @Override
    public void quitClub(int idC, int idU) {
        User user = userRepository.findById(idU).orElse(null);
        if (user != null) {
            user.getClubs().removeIf(club -> club.getIdC() == idC);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Club not found");
        }
    }


    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser ).getPrincipal() );
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new IllegalStateException("Wrong Password");
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalStateException("Password are not the same");
        }
        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newPasswordHash);
        userRepository.save(user);

    }

    @Override
    public void sendForgotPasswordMail(String email) {
        //TODO: Activation code should be in the mail
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found" +email));
        try {
            //var code =jwtService.generateActivationCode(6);
            var code = jwtService.generateAndSaveActivationToken(user);
            emailService.sendResetPasswordEmail(email,user.getLastName()+" "+user.getLastName(),resetPwd,code,"Reset Password");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request, String token){

        Token tkn = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid Exception"));
        if(LocalDateTime.now().isAfter(tkn.getExpiresdAt())){
            throw new RuntimeException("Activation token has expired, restart");
        }
        var user = userRepository.findById(tkn.getUser().getIdU()).orElseThrow(() -> new UsernameNotFoundException("User Not found"));
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new IllegalStateException("Password are not the same");
        }
        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newPasswordHash);
        userRepository.save(user);

    }
}
