package tn.esprit.clubconnect.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Club implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idC;
    String nameC;
    String description;
    LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    Categ categorie;
    String logo;
    String websiteURL;

    @ManyToMany(mappedBy = "clubs")
    Set<User> users;

    @OneToMany(mappedBy = "club", cascade = CascadeType.PERSIST)
    Set<Department> departments;

    @OneToMany(mappedBy = "club", cascade = CascadeType.PERSIST)
    Set<Event> events;

    @OneToMany(mappedBy = "club", cascade = CascadeType.PERSIST)
    Set<Training> trainings;




}
