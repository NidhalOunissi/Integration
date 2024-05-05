package tn.esprit.clubconnect.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idE;
    String title;
    String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate;

    int  duration;
    String location;
    @OneToOne(cascade = CascadeType.MERGE)
    Image image;
    @ManyToOne(cascade = CascadeType.MERGE)
    User user;



    @ManyToOne
    Club club;



}
