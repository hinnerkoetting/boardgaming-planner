package de.oetting.wwp.infrastructure;

import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {

    public static String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
