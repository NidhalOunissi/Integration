package tn.esprit.clubconnect.entities;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
public class ImageModel {
    private  String name;
    private MultipartFile file;


}
