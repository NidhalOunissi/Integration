package tn.esprit.clubconnect.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.esprit.clubconnect.services.CloudinaryServiceImpl;

@Configuration

public class CloudinaryNour {
    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;

    public CloudinaryNour(
            @Value("dnh3lwyc0") String cloudName,
            @Value("971147733483765") String apiKey,
            @Value("B0KOu62djgfyzaq2RaIFgMHRMI4") String apiSecret) {
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret))
                ;
    }

    @Bean
    public CloudinaryServiceImpl cloudinaryService(Cloudinary cloudinary) {
        return new CloudinaryServiceImpl(cloudinary);
    }

}
