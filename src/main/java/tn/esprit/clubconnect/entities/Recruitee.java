package tn.esprit.clubconnect.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Recruitee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idRc;
    @ManyToOne
    @JoinColumn(name = "interview_idi")
    Interview interview;

}
