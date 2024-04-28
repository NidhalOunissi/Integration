package tn.esprit.clubconnect.requestAndresponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.clubconnect.entities.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleChangeRequest {

    Role role;

}
