package ru.kubsu.applicationservice.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, OPERATOR, CLIENT, GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}