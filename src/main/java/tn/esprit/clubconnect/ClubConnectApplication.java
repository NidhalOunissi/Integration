package tn.esprit.clubconnect;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import tn.esprit.clubconnect.entities.Role;
import tn.esprit.clubconnect.requestAndresponse.RegisterRequest;
import tn.esprit.clubconnect.services.AuthenticationService;


@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class ClubConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubConnectApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(
//            AuthenticationService service
//    ){
//        return args -> {
//            var admin = RegisterRequest.builder()
//                    .firstName("Nidhal")
//                    .lastName("Ounissi2")
//                    .email("admin@gmail.com")
//                    .password("password")
//                    .role(Role.ADMIN)
//                    .build();
//            System.out.println("Admin token: " + service.register(admin).getToken());
//
//            var clubadmin = RegisterRequest.builder()
//                    .firstName("Nidhal")
//                    .lastName("Ounissi2")
//                    .email("admin2@gmail.com")
//                    .password("password")
//                    .role(Role.CLUBADMIN)
//                    .build();
//            System.out.println("clubadmin token: " + service.register(clubadmin).getToken());
//
//
//        };
//    }


}
