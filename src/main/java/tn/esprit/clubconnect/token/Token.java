package tn.esprit.clubconnect.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.clubconnect.entities.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idTk;
    String token;
    @Enumerated(EnumType.STRING)
    TokenType tokenType;
    boolean expired;
    boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;
    LocalDateTime createdAt;
    LocalDateTime expiresdAt;
    LocalDateTime validatedAt;





}
