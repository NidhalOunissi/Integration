package tn.esprit.clubconnect.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static tn.esprit.clubconnect.entities.Permissions.*;


@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
            ADMIN_READ,
            ADMIN_CREATE,
            ADMIN_DELETE,
            ADMIN_UPDATE,
            CLUBADMIN_READ,
            CLUBADMIN_UPDATE,
            CLUBADMIN_CREATE,
            CLUBADMIN_DELETE
    )),
    CLUBADMIN(
            Set.of(
                    CLUBADMIN_READ,
                    CLUBADMIN_UPDATE,
                    CLUBADMIN_CREATE,
                    CLUBADMIN_DELETE
            )
    ),
    MEMBER(Collections.emptySet())

    ;


    @Getter
    private final Set<Permissions> permissionsSet;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissionsSet()
                .stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }


}
