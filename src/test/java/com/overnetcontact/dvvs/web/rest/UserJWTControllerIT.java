package com.overnetcontact.dvvs.web.rest;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.domain.User;
import com.overnetcontact.dvvs.domain.enumeration.Role;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.repository.UserRepository;
import com.overnetcontact.dvvs.web.rest.vm.LoginVM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserJWTController} REST controller.
 */
@AutoConfigureMockMvc
@IntegrationTest
class UserJWTControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrgUserRepository orgUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void testAuthorize() throws Exception {
        // User user = new User();
        // user.setLogin("user-jwt-controller");
        // user.setEmail("user-jwt-controller@example.com");
        // user.setActivated(true);
        // user.setPassword(passwordEncoder.encode("test"));

        // userRepository.saveAndFlush(user);

        // OrgUser orgUser = new OrgUser();
        // orgUser.setDeviceId("121123");
        // orgUser.setInternalUser(user);
        // orgUser.setPhone("12123123121");
        // orgUser.setRole(Role.SALE);

        // orgUserRepository.saveAndFlush(orgUser);

        // LoginVM login = new LoginVM();
        // login.setUsername("user-jwt-controller");
        // login.setPassword("test");
        // mockMvc
        //     .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
        //     .andExpect(status().isOk())
        //     .andExpect(jsonPath("$.token").isString())
        //     .andExpect(jsonPath("$.token").isNotEmpty())
        //     .andExpect(header().string("Authorization", not(nullValue())))
        //     .andExpect(header().string("Authorization", not(is(emptyString()))));
    }

    @Test
    @Transactional
    void testAuthorizeWithRememberMe() throws Exception {
        // User user = new User();
        // user.setLogin("user-jwt-controller-remember-me");
        // user.setEmail("user-jwt-controller-remember-me@example.com");
        // user.setActivated(true);
        // user.setPassword(passwordEncoder.encode("test"));

        // userRepository.saveAndFlush(user);

        // OrgUser orgUser = new OrgUser();
        // orgUser.setDeviceId("121123");
        // orgUser.setInternalUser(user);
        // orgUser.setPhone("12123123121");
        // orgUser.setRole(Role.SALE);

        // orgUserRepository.saveAndFlush(orgUser);

        // LoginVM login = new LoginVM();
        // login.setUsername("user-jwt-controller-remember-me");
        // login.setPassword("test");
        // login.setRememberMe(true);
        // mockMvc
        //     .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
        //     .andExpect(status().isOk())
        //     .andExpect(jsonPath("$.token").isString())
        //     .andExpect(jsonPath("$.token").isNotEmpty())
        //     .andExpect(header().string("Authorization", not(nullValue())))
        //     .andExpect(header().string("Authorization", not(is(emptyString()))));
    }

    @Test
    void testAuthorizeFails() throws Exception {
        LoginVM login = new LoginVM();
        login.setUsername("wrong-user");
        login.setPassword("wrong password");
        mockMvc
            .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.token").doesNotExist())
            .andExpect(header().doesNotExist("Authorization"));
    }
}
