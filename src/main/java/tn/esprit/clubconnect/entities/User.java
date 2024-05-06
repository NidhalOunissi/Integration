package tn.esprit.clubconnect.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idU;
    String firstName;

    String lastName;

    String email;

    String password;

    String pseudoname;

    @Enumerated(EnumType.STRING)
    Role role;

    private boolean highlyInvolved;

    private double attritionProbability;

    @OneToMany(mappedBy = "user")
    Set<Reclamation> reclamations;

    @ManyToMany
    Set<Club> clubs;

    @ManyToMany
    Set<Event> events;

    @ManyToMany
    Set<Training> trainings;

   /* @ManyToMany
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;*/

    /*@ManyToMany
    @JoinTable(
            name = "user_training",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id"))
    private List<Training> trainings;*/


    @OneToMany(mappedBy = "user")
    private List<FundRaisingC> fundraisings;

    @OneToMany(mappedBy = "user")
    private List<ProjectC> projects;



}
