package tn.esprit.clubconnect.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime startDate;
    int nbParticipants;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalTime duration;
    String location;
    @OneToOne(cascade = CascadeType.MERGE)
    Image poster;
    double price;
    @ElementCollection
    Set<Integer> userWinners = new HashSet<>();

    @ManyToOne
    @JsonManagedReference
    @JsonIgnore
    Club club;

    @ManyToMany(mappedBy = "trainings")
    @JsonManagedReference
    @JsonIgnore
    Set<User> users;

}