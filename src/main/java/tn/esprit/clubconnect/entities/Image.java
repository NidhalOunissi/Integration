package tn.esprit.clubconnect.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String url;
}
