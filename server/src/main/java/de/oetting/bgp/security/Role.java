package de.oetting.bgp.security;

import java.util.Arrays;

public enum Role {

    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), OWNER("ROLE_OWNER");

    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String HAS_ROLE_USER = "hasRole('ROLE_USER')";
    public static final String HAS_ROLE_OWNER = "hasRole('ROLE_OWNER')";

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Role from(String name) {
        return Arrays.stream(Role.values()).filter(role -> role.getName().equals(name)).findAny().orElseThrow();
    }
}
