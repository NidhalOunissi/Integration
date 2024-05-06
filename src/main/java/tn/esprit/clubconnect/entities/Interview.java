package tn.esprit.clubconnect.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "Interview")
public class Interview implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idI;

    @Column(nullable = false, length = 100)
    private String title;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false, length = 100)
    private String interviewer;

    @Column(nullable = false, length = 255)
    private String requiredSkills;

    @Column(nullable = false, length = 100)
    private String contactEmail;

    @Column(nullable = false, length = 20)
    private String contactPhone;

    @Temporal(TemporalType.DATE)
    @Column(name = "registration_deadline", nullable = false)
    private Date registrationDeadline;

    @Column(name = "max_participants", nullable = false)
    private int maxParticipants;

    @Column(nullable = false)
    private int duration;


    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "share", nullable = false)
    private int share ; // Default value is 1

    @ElementCollection
    private List<String> images;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "registrations",
//            joinColumns = @JoinColumn(name = "interview_id"),
//            inverseJoinColumns = @JoinColumn(name = "registration_id")
//    )
//    private List<Registration> registrations = new ArrayList<>();



    // Getter method for the ID field
    public Integer getId() {
        return idI;
    }

    // Setter method for the ID field
    public void setId(Integer id) {
        this.idI = id;
    }}


