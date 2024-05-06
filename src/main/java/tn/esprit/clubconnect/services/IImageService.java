package tn.esprit.clubconnect.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IImageService {
    ResponseEntity<Map> uploadImage(MultipartFile file);

}
