package tn.esprit.clubconnect.services;//package tn.esprit.clubconnect.Services;




import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.clubconnect.entities.Image;
import tn.esprit.clubconnect.repositories.IImageRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService {


    private ICloudinaryService cloudinaryService;
    private IImageRepository imageRepository;

    // Méthode pour télécharger une image
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
            // Attribution du nom de l'image à partir du nom de fichier
            image.setName(imageModel.getName());
            // Téléchargement de l'image sur Cloudinary et récupération de son URL
            image.setUrl(cloudinaryService.uploadFile(imageModel, "folder_1"));
            // Vérification si l'URL de l'image est nulle
            if(image.getUrl() == null) {
                return ResponseEntity.badRequest().build();
            }
            // Enregistrement de l'objet Image dans la base de données
            imageRepository.save(image);
            // Renvoie une réponse HTTP 200 OK avec l'URL de l'image téléchargée

            return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
