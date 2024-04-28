package tn.esprit.clubconnect.requestAndresponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.clubconnect.entities.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String pseudoname;
    String firstName;
    String lastName;
    String email;
    String phone;
    String password;
    String confirmPassword;
    Role role;
    boolean tfaEnbaled;

}
