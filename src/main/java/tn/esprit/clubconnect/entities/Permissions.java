package tn.esprit.clubconnect.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {



    ADMIN_READ("admin:get"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:post"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_PATCH("admin:patch"),


    CLUBADMIN_READ("clubadmin:get"),
    CLUBADMIN_UPDATE("clubadmin:update"),
    CLUBADMIN_CREATE("clubadmin:post"),
    CLUBADMIN_DELETE("clubadmin:delete"),
    CLUBADMIN_PATCH("clubadmin:patch"),


    MEMBER_READ("clubadmin:get"),
    MEMBER_UPDATE("clubadmin:update"),
    MEMBER_CREATE("clubadmin:post"),
    MEMBER_DELETE("clubadmin:delete"),
    MEMBER_PATCH("member:patch")

    ;

    @Getter
    private final String permission;







}
