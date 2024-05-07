package tn.esprit.clubconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.esprit.clubconnect.repositories.NotificationRepository;
import tn.esprit.clubconnect.services.NotificationService;

@Configuration
public class ServiceConfig {
    private final NotificationRepository notificationRepository;

    public ServiceConfig(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Bean
    public NotificationService notificationService() {
        return new NotificationService(notificationRepository);
    }
}

