package tn.esprit.clubconnect.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Training implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idT;
    String titleT;
    String description;
    int numberPlace;
    LocalDateTime startDate;
    LocalTime duration;
    String location;
    String affiche;

    @ManyToOne
    Club club;

    @ManyToMany(mappedBy = "trainings")
    @JsonIgnore
    Set<User> users;


}
