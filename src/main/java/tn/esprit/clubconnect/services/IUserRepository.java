package tn.esprit.clubconnect.services;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.User;

public interface IUserRepository extends JpaRepository<User,Long> {
}
