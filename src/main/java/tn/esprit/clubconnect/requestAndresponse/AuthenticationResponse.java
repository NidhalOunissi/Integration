package tn.esprit.clubconnect.requestAndresponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.clubconnect.entities.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthenticationResponse {
     @JsonProperty("access_token")
     String token;
     User user;
     @JsonProperty("refresh_token")
     String refresh_token;
     boolean tfaEnbaled;
     String secretImageUri;



}
