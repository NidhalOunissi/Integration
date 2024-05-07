package tn.esprit.clubconnect.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ProjectC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idP;
    String nameP;
    String descriptionP;
    Double resourceP;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id")

    private Club club;

}
