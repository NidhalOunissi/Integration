package tn.esprit.clubconnect.services;//package tn.esprit.clubconnect.Services;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//    @Service
//public class CloudinaryService {
//
//    Cloudinary cloudinary;
//    public CloudinaryService() {
//
//    }
//
//    private File convert(MultipartFile multipartFile) throws IOException {
//        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        FileOutputStream fo = new FileOutputStream(file);
//        fo.write(multipartFile.getBytes());
//        fo.close();
//        return file;
//    }
//    public Map upload(MultipartFile multipartFile) throws IOException {
//        File file = convert(multipartFile);
//        // Spécifiez le type de ressource que vous souhaitez uploader (dans ce cas, un IMG)
//        Map params = ObjectUtils.asMap(
//                "resource_type", "auto" // Vous pouvez spécifier "auto" pour détecter automatiquement le type de ressource
//        );
//
//        Map result = cloudinary.uploader().upload(file, params);
//
//        if (!Files.deleteIfExists(file.toPath())) {
//            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
//        }
//        return result;
//    }
//    public Map delete(String id) throws IOException {
//        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
//    }
//}
//

import com.cloudinary.Cloudinary;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements ICloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}

