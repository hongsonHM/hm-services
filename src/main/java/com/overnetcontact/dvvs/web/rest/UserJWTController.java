package com.overnetcontact.dvvs.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.overnetcontact.dvvs.domain.Authority;
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.security.UserNotActivatedException;
import com.overnetcontact.dvvs.security.jwt.JWTFilter;
import com.overnetcontact.dvvs.security.jwt.TokenProvider;
import com.overnetcontact.dvvs.web.rest.vm.LoginVM;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final OrgUserRepository orgUserRepository;

    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        OrgUserRepository orgUserRepository
    ) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.orgUserRepository = orgUserRepository;
    }

    @PostMapping("/authenticate")
    @Transactional
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        OrgUser orgUser = orgUserRepository
            .findByInternalUser_Login(loginVM.getUsername())
            .orElseThrow(() -> new UserNotActivatedException("User don't has role defined"));

        return new ResponseEntity<>(new JWTToken(jwt, orgUser), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    @Data
    static class JWTToken {

        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private Collection<String> roles;
        private String phone;
        private String token;

        JWTToken(String idToken, OrgUser orgUser) {
            this.token = idToken;
            this.id = orgUser.getInternalUser().getId();
            this.username = orgUser.getInternalUser().getLogin();
            this.roles = orgUser.getInternalUser().getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
            this.firstName = orgUser.getInternalUser().getFirstName();
            this.lastName = orgUser.getInternalUser().getLastName();
            this.phone = orgUser.getPhone();
        }
    }
}
