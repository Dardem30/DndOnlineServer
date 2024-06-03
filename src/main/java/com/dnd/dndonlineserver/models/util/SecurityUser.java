package com.dnd.dndonlineserver.models.util;

import com.dnd.dndonlineserver.models.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class SecurityUser extends User {
    private AppUser user;
    private String[] roles;
    private static final List<GrantedAuthority> predefinedAuthorities;
    private static SecurityUser systemUser;

    static {
        // add USER_ROLE to all users as predefined
        predefinedAuthorities = new ArrayList<>();
        predefinedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public SecurityUser(AppUser user) {
        super(user.getFirebaseUid(), "null", true, true, true, true, getUserAuthorities());
        this.user = user;
    }

    private static List<GrantedAuthority> getUserAuthorities() {
        return new ArrayList<>(predefinedAuthorities);
    }

    public AppUser getAppUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public static SecurityUser get() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof SecurityUser) {
                return (SecurityUser) principal;
            }
        }
        return null;
    }
    public static AppUser currentLoggedInUser() {
        SecurityUser securityUser = get();
        if (securityUser != null) {
            return securityUser.getAppUser();
        }
        return null;
    }
    public static AppUser getCurrentLoggedInUser() {
        return get().getAppUser();
    }


    private static SecurityUser getSystemUser() {
        if (systemUser == null) {
            AppUser appUser = new AppUser();
            appUser.setFirebaseUid("system");
            systemUser = new SecurityUser(appUser);
        }
        return systemUser;
    }

}
