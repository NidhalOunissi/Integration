package tn.esprit.clubconnect.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.security.config.annotation.web.SecurityMarker;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "ClubConnect - Team"
                ),
                description = "OpenAPI Docs for ClubClonnect API",
                title = "OpenAPI Specs - ClubConnect Team"

        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8090"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
        )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER



)
public class OpenApiConfig {


}
