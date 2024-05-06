package tn.esprit.clubconnect.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.Image;
import tn.esprit.clubconnect.repositories.IImageRepository;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService{
    private ICloudinaryService cloudinaryService;
    private IImageRepository imageRepository;


    @Override
    public ResponseEntity<Map> uploadImage(MultipartFile imageModel) {
        try {
            if (imageModel.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (imageModel.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Image image = new Image();
            image.setName(imageModel.getName());
            image.setUrl(cloudinaryService.uploadFile(imageModel, "folder_1"));
            if(image.getUrl() == null) {
                return ResponseEntity.badRequest().build();
            }
            imageRepository.save(image);
            return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
