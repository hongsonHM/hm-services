package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.domain.enumeration.Role;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.service.dto.OrgUserDTO;
import com.overnetcontact.dvvs.service.mapper.OrgUserMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrgUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgUserResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Role DEFAULT_ROLE = Role.SERVICE_MANAGER;
    private static final Role UPDATED_ROLE = Role.SUPERVISOR;

    private static final String ENTITY_API_URL = "/api/org-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgUserRepository orgUserRepository;

    @Autowired
    private OrgUserMapper orgUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgUserMockMvc;

    private OrgUser orgUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUser createEntity(EntityManager em) {
        OrgUser orgUser = new OrgUser()
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .role(DEFAULT_ROLE);
        return orgUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUser createUpdatedEntity(EntityManager em) {
        OrgUser orgUser = new OrgUser()
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .role(UPDATED_ROLE);
        return orgUser;
    }

    @BeforeEach
    public void initTest() {
        orgUser = createEntity(em);
    }

    @Test
    @Transactional
    void createOrgUser() throws Exception {
        int databaseSizeBeforeCreate = orgUserRepository.findAll().size();
        // Create the OrgUser
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);
        restOrgUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserDTO)))
            .andExpect(status().isCreated());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUser testOrgUser = orgUserList.get(orgUserList.size() - 1);
        assertThat(testOrgUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrgUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testOrgUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOrgUser.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void createOrgUserWithExistingId() throws Exception {
        // Create the OrgUser with an existing ID
        orgUser.setId(1L);
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        int databaseSizeBeforeCreate = orgUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUserRepository.findAll().size();
        // set the field null
        orgUser.setEmail(null);

        // Create the OrgUser, which fails.
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        restOrgUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserDTO)))
            .andExpect(status().isBadRequest());

        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUserRepository.findAll().size();
        // set the field null
        orgUser.setPassword(null);

        // Create the OrgUser, which fails.
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        restOrgUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserDTO)))
            .andExpect(status().isBadRequest());

        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUserRepository.findAll().size();
        // set the field null
        orgUser.setPhoneNumber(null);

        // Create the OrgUser, which fails.
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        restOrgUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserDTO)))
            .andExpect(status().isBadRequest());

        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUserRepository.findAll().size();
        // set the field null
        orgUser.setRole(null);

        // Create the OrgUser, which fails.
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        restOrgUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserDTO)))
            .andExpect(status().isBadRequest());

        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrgUsers() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList
        restOrgUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    void getOrgUser() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get the orgUser
        restOrgUserMockMvc
            .perform(get(ENTITY_API_URL_ID, orgUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgUser.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrgUser() throws Exception {
        // Get the orgUser
        restOrgUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrgUser() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();

        // Update the orgUser
        OrgUser updatedOrgUser = orgUserRepository.findById(orgUser.getId()).get();
        // Disconnect from session so that the updates on updatedOrgUser are not directly saved in db
        em.detach(updatedOrgUser);
        updatedOrgUser.email(UPDATED_EMAIL).password(UPDATED_PASSWORD).phoneNumber(UPDATED_PHONE_NUMBER).role(UPDATED_ROLE);
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(updatedOrgUser);

        restOrgUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
        OrgUser testOrgUser = orgUserList.get(orgUserList.size() - 1);
        assertThat(testOrgUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrgUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testOrgUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOrgUser.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void putNonExistingOrgUser() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();
        orgUser.setId(count.incrementAndGet());

        // Create the OrgUser
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgUser() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();
        orgUser.setId(count.incrementAndGet());

        // Create the OrgUser
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgUser() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();
        orgUser.setId(count.incrementAndGet());

        // Create the OrgUser
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrgUserWithPatch() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();

        // Update the orgUser using partial update
        OrgUser partialUpdatedOrgUser = new OrgUser();
        partialUpdatedOrgUser.setId(orgUser.getId());

        partialUpdatedOrgUser.email(UPDATED_EMAIL).password(UPDATED_PASSWORD);

        restOrgUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgUser))
            )
            .andExpect(status().isOk());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
        OrgUser testOrgUser = orgUserList.get(orgUserList.size() - 1);
        assertThat(testOrgUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrgUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testOrgUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOrgUser.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void fullUpdateOrgUserWithPatch() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();

        // Update the orgUser using partial update
        OrgUser partialUpdatedOrgUser = new OrgUser();
        partialUpdatedOrgUser.setId(orgUser.getId());

        partialUpdatedOrgUser.email(UPDATED_EMAIL).password(UPDATED_PASSWORD).phoneNumber(UPDATED_PHONE_NUMBER).role(UPDATED_ROLE);

        restOrgUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgUser))
            )
            .andExpect(status().isOk());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
        OrgUser testOrgUser = orgUserList.get(orgUserList.size() - 1);
        assertThat(testOrgUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrgUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testOrgUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOrgUser.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void patchNonExistingOrgUser() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();
        orgUser.setId(count.incrementAndGet());

        // Create the OrgUser
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgUser() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();
        orgUser.setId(count.incrementAndGet());

        // Create the OrgUser
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgUser() throws Exception {
        int databaseSizeBeforeUpdate = orgUserRepository.findAll().size();
        orgUser.setId(count.incrementAndGet());

        // Create the OrgUser
        OrgUserDTO orgUserDTO = orgUserMapper.toDto(orgUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orgUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUser in the database
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrgUser() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        int databaseSizeBeforeDelete = orgUserRepository.findAll().size();

        // Delete the orgUser
        restOrgUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgUser> orgUserList = orgUserRepository.findAll();
        assertThat(orgUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
