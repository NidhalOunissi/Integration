package tn.esprit.clubconnect.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.Event;
import tn.esprit.clubconnect.entities.User;

public interface IEventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT COUNT(e) FROM Event e WHERE :user MEMBER OF e.users AND e.club = :club")
    int countEventsByUserAndClub(@Param("user") User user, @Param("club") Club club);

    @Query("SELECT COUNT(e) FROM Event e WHERE :user MEMBER OF e.users")
    int countEventsByUser(@Param("user") User user);
}
