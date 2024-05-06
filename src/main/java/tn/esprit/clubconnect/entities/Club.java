package tn.esprit.clubconnect.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
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
    Long idC;
    String nameC;
    String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    Categ categorie;
//@OneToOne(cascade = CascadeType.MERGE)
@OneToOne(cascade = CascadeType.ALL)
    Image logo;
   // String logo;

    @JsonIgnore

    String websiteURL;

    @JsonIgnore


    @ManyToMany(mappedBy = "clubs")
    Set<User> users;

    @JsonIgnore


    @OneToMany(mappedBy = "club", cascade = CascadeType.PERSIST)
   @JsonManagedReference
    Set<Department> departments;

    @JsonIgnore


    @OneToMany(mappedBy = "club", cascade = CascadeType.PERSIST)
    Set<Event> events;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.PERSIST)
    Set<Training> trainings;

    @JsonIgnore
    @OneToMany(mappedBy = "club")
    private List<FundRaisingC> fundraisings;

    @JsonIgnore
    @OneToMany(mappedBy = "club")
    private List<ProjectC> projects;


}
