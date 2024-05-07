package tn.esprit.clubconnect.services;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.clubconnect.entities.Image;

public interface IImageRepository extends JpaRepository<Image, Integer> {
}
