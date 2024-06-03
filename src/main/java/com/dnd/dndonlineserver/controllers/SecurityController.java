package com.dnd.dndonlineserver.controllers;

import com.dnd.dndonlineserver.controllers.request_forms.AuthForm;
import com.dnd.dndonlineserver.controllers.response_forms.ResponseForm;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SecurityController {
    private final Log log = LogFactory.getLog(getClass());
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseForm authenticate(@RequestBody AuthForm authForm, HttpSession session) {

        log.info("Trying to authenticate [" + authForm.getToken() + "]");
        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(null, authForm, null);
        final Authentication auth = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        String sessionId = session.getId(); // JSESSIONID
        return ResponseForm.successWithResult("Authenticated", sessionId);
    }
}