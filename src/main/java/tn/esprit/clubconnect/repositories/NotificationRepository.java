package tn.esprit.clubconnect.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.clubconnect.entities.Notification;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Any custom methods can be added here
}

