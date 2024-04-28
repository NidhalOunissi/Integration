package tn.esprit.clubconnect.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account"),
    REQUEST_RECEIVED("request-received"),
    REQUEST_APPROUVED("request_approved"),
    PASSWORD_RESET("password_reset")
    ;

    private final String name;

    EmailTemplateName(String name) {
        this.name=name;
    }


}
