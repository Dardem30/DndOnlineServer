package com.dnd.dndonlineserver.config;

import com.dnd.dndonlineserver.controllers.request_forms.AuthForm;
import com.dnd.dndonlineserver.models.AppUser;
import com.dnd.dndonlineserver.models.util.SecurityUser;
import com.dnd.dndonlineserver.services.FirebaseService;
import com.dnd.dndonlineserver.services.UserService;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider extends DaoAuthenticationProvider {
    private final UserService userService;
    private final FirebaseService firebaseService;

    public AuthenticationProvider(UserService userService, FirebaseService firebaseService) {
        this.userService = userService;
        this.firebaseService = firebaseService;
        this.setUserDetailsService(username -> null);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthForm authForm = (AuthForm) authentication.getCredentials();
        final FirebaseToken firebaseToken = firebaseService.verifyToken(authForm.getToken());

        AppUser user = userService.findAndRefreshUserByFirebaseUid(firebaseToken.getUid(), authForm);
        if (user == null) {
            user = userService.signUp(firebaseToken, authForm);
        }
        final SecurityUser securityUser = new SecurityUser(user);
        return super.createSuccessAuthentication(securityUser, authentication, securityUser);
    }
}
