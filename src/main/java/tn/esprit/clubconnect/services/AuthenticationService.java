package tn.esprit.clubconnect.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.EmailTemplateName;
import tn.esprit.clubconnect.entities.Role;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.repositories.IUserRepository;
import tn.esprit.clubconnect.requestAndresponse.AuthenticationRequest;
import tn.esprit.clubconnect.requestAndresponse.AuthenticationResponse;
import tn.esprit.clubconnect.requestAndresponse.RegisterRequest;
import tn.esprit.clubconnect.requestAndresponse.VerificationRequest;
import tn.esprit.clubconnect.tfa.TwoFactorAuthenticationService;
import tn.esprit.clubconnect.token.ITokenRepository;
import tn.esprit.clubconnect.token.Token;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final ITokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TwoFactorAuthenticationService tfaService;
//    private final String activationUrl = "http://localhost:8090/api/v1/auth/activate-account";
private final String activationUrl = "http://localhost:4200/activate-account";


    public void confirmAccount (String token) throws MessagingException {
        Token tkn = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid Exception"));
        if(LocalDateTime.now().isAfter(tkn.getExpiresdAt())){
            sendValidationEmail(tkn.getUser());
            throw new RuntimeException("Activation token has expired, a new token has been sent");
        }
        var user = userRepository.findById(tkn.getUser().getIdU()).orElseThrow(() -> new UsernameNotFoundException("User Not found"));
        user.setEnabled(true);
        tkn.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(tkn);



    }



    private void sendValidationEmail(User user) throws MessagingException {
        //var newToken = jwtService.generateToken(user);
        var newToken = jwtService.generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullname(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );


    }


    public AuthenticationResponse register(RegisterRequest request) throws MessagingException {
        if(userRepository.findByEmail(request.getEmail()).isEmpty()) {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .pseudoname(request.getPseudoname())
                    .phone(request.getPhone())
                    .role(request.getRole())
                    .tfaEnabled(request.isTfaEnbaled())
                    .enabled(false)
                    .build();


            //Partie 2FAuth
            if(request.isTfaEnbaled()){
                user.setSecret(tfaService.generateNewSecret());
            }

            var savedUser = userRepository.save(user);
            sendValidationEmail(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshtoken = jwtService.generateRefreshToken(user);
            revokeAllUserToken(user);
            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder()
                    .user(savedUser)
                    .token(jwtToken)
                    .secretImageUri(tfaService.generateQrCodeUri(user.getSecret()))
                    .refresh_token(refreshtoken)
                    .tfaEnbaled(user.isTfaEnabled()).build();
        }else {
            throw new RuntimeException("User exists");
        }


    }

    private void saveUserToken(User savedUser, String jwtToken) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .revoked(false)
                .expired(false)
                .createdAt(LocalDateTime.now())
                .expiresdAt(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserToken(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getIdU());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(
                token -> {
                    token.setRevoked(true);
                    token.setExpired(true);
                }
        );

        tokenRepository.saveAll(validUserTokens);
    }







    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

            if(user.isTfaEnabled()){
                return AuthenticationResponse.builder()
                        .user(user)
                        .token("")
                        .refresh_token("")
                        .tfaEnbaled(true)
                        .build();

            }

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserToken(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .user(user)
                    .token(jwtToken)
                    .tfaEnbaled(false)
                    .refresh_token(refreshToken)
                    .build();
        }else {
            throw new RuntimeException("User not found");
        }

    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null){
            //UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var userDetails = this.userRepository.findByEmail(userEmail).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, userDetails)){
                var accessToken = jwtService.generateToken(userDetails);
                revokeAllUserToken(userDetails);
                saveUserToken(userDetails, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .token(accessToken)
                        .refresh_token(refreshToken)
                        .tfaEnbaled(false)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);

            }
        }

    }

    public AuthenticationResponse verifyCode(VerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new EntityNotFoundException(
                String.format("User Not Found %S", request.getEmail())
        ));
        if(tfaService.isOTPNotValid(user.getSecret(), request.getCode())){
            throw new BadCredentialsException("Code is not valid");
        }
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .tfaEnbaled(user.isTfaEnabled())
                .build();
    }


//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // Extract refresh token from request header
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            // If the authorization header is missing or the token format is incorrect, return 403 Forbidden
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing or invalid token");
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//
//        // Validate refresh token
//        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            var userDetailsOpt = this.userRepository.findByEmail(userEmail);
//            if (userDetailsOpt.isPresent()) {
//                var userDetails = userDetailsOpt.get();
//                if(jwtService.isTokenValid(refreshToken, userDetails)) {
//                    // Generate new access token
//                    var accessToken = jwtService.generateToken(userDetails);
//                    // Revoke old tokens and save new token
//                    revokeAllUserToken(userDetails);
//                    saveUserToken(userDetails, accessToken);
//                    // Return new access token
//                    var authResponse = AuthenticationResponse.builder()
//                            .token(accessToken)
//                            .refresh_token(refreshToken)
//                            .build();
//                    response.setStatus(HttpServletResponse.SC_OK);
//                    response.setContentType("application/json");
//                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//                    return;
//                }
//            }
//        }
//        // If the token is invalid or user not found, return 403 Forbidden
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token");
//    }



}
