package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.OrgGroup;
import com.overnetcontact.dvvs.domain.OrgNotification;
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.domain.User;
import com.overnetcontact.dvvs.domain.enumeration.Role;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.service.criteria.OrgUserCriteria;
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

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

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
        OrgUser orgUser = new OrgUser().deviceId(DEFAULT_DEVICE_ID).phone(DEFAULT_PHONE).role(DEFAULT_ROLE);
        return orgUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUser createUpdatedEntity(EntityManager em) {
        OrgUser orgUser = new OrgUser().deviceId(UPDATED_DEVICE_ID).phone(UPDATED_PHONE).role(UPDATED_ROLE);
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
        assertThat(testOrgUser.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testOrgUser.getPhone()).isEqualTo(DEFAULT_PHONE);
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
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUserRepository.findAll().size();
        // set the field null
        orgUser.setPhone(null);

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
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
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
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    void getOrgUsersByIdFiltering() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        Long id = orgUser.getId();

        defaultOrgUserShouldBeFound("id.equals=" + id);
        defaultOrgUserShouldNotBeFound("id.notEquals=" + id);

        defaultOrgUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrgUserShouldNotBeFound("id.greaterThan=" + id);

        defaultOrgUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrgUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrgUsersByDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where deviceId equals to DEFAULT_DEVICE_ID
        defaultOrgUserShouldBeFound("deviceId.equals=" + DEFAULT_DEVICE_ID);

        // Get all the orgUserList where deviceId equals to UPDATED_DEVICE_ID
        defaultOrgUserShouldNotBeFound("deviceId.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllOrgUsersByDeviceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where deviceId not equals to DEFAULT_DEVICE_ID
        defaultOrgUserShouldNotBeFound("deviceId.notEquals=" + DEFAULT_DEVICE_ID);

        // Get all the orgUserList where deviceId not equals to UPDATED_DEVICE_ID
        defaultOrgUserShouldBeFound("deviceId.notEquals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllOrgUsersByDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where deviceId in DEFAULT_DEVICE_ID or UPDATED_DEVICE_ID
        defaultOrgUserShouldBeFound("deviceId.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID);

        // Get all the orgUserList where deviceId equals to UPDATED_DEVICE_ID
        defaultOrgUserShouldNotBeFound("deviceId.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllOrgUsersByDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where deviceId is not null
        defaultOrgUserShouldBeFound("deviceId.specified=true");

        // Get all the orgUserList where deviceId is null
        defaultOrgUserShouldNotBeFound("deviceId.specified=false");
    }

    @Test
    @Transactional
    void getAllOrgUsersByDeviceIdContainsSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where deviceId contains DEFAULT_DEVICE_ID
        defaultOrgUserShouldBeFound("deviceId.contains=" + DEFAULT_DEVICE_ID);

        // Get all the orgUserList where deviceId contains UPDATED_DEVICE_ID
        defaultOrgUserShouldNotBeFound("deviceId.contains=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllOrgUsersByDeviceIdNotContainsSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where deviceId does not contain DEFAULT_DEVICE_ID
        defaultOrgUserShouldNotBeFound("deviceId.doesNotContain=" + DEFAULT_DEVICE_ID);

        // Get all the orgUserList where deviceId does not contain UPDATED_DEVICE_ID
        defaultOrgUserShouldBeFound("deviceId.doesNotContain=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllOrgUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where phone equals to DEFAULT_PHONE
        defaultOrgUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the orgUserList where phone equals to UPDATED_PHONE
        defaultOrgUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllOrgUsersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where phone not equals to DEFAULT_PHONE
        defaultOrgUserShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the orgUserList where phone not equals to UPDATED_PHONE
        defaultOrgUserShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllOrgUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultOrgUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the orgUserList where phone equals to UPDATED_PHONE
        defaultOrgUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllOrgUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where phone is not null
        defaultOrgUserShouldBeFound("phone.specified=true");

        // Get all the orgUserList where phone is null
        defaultOrgUserShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllOrgUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where phone contains DEFAULT_PHONE
        defaultOrgUserShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the orgUserList where phone contains UPDATED_PHONE
        defaultOrgUserShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllOrgUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where phone does not contain DEFAULT_PHONE
        defaultOrgUserShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the orgUserList where phone does not contain UPDATED_PHONE
        defaultOrgUserShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllOrgUsersByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where role equals to DEFAULT_ROLE
        defaultOrgUserShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the orgUserList where role equals to UPDATED_ROLE
        defaultOrgUserShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllOrgUsersByRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where role not equals to DEFAULT_ROLE
        defaultOrgUserShouldNotBeFound("role.notEquals=" + DEFAULT_ROLE);

        // Get all the orgUserList where role not equals to UPDATED_ROLE
        defaultOrgUserShouldBeFound("role.notEquals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllOrgUsersByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultOrgUserShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the orgUserList where role equals to UPDATED_ROLE
        defaultOrgUserShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllOrgUsersByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);

        // Get all the orgUserList where role is not null
        defaultOrgUserShouldBeFound("role.specified=true");

        // Get all the orgUserList where role is null
        defaultOrgUserShouldNotBeFound("role.specified=false");
    }

    @Test
    @Transactional
    void getAllOrgUsersByInternalUserIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);
        User internalUser = UserResourceIT.createEntity(em);
        em.persist(internalUser);
        em.flush();
        orgUser.setInternalUser(internalUser);
        orgUserRepository.saveAndFlush(orgUser);
        Long internalUserId = internalUser.getId();

        // Get all the orgUserList where internalUser equals to internalUserId
        defaultOrgUserShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the orgUserList where internalUser equals to (internalUserId + 1)
        defaultOrgUserShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    @Test
    @Transactional
    void getAllOrgUsersByNotificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);
        OrgNotification notifications = OrgNotificationResourceIT.createEntity(em);
        em.persist(notifications);
        em.flush();
        orgUser.addNotifications(notifications);
        orgUserRepository.saveAndFlush(orgUser);
        Long notificationsId = notifications.getId();

        // Get all the orgUserList where notifications equals to notificationsId
        defaultOrgUserShouldBeFound("notificationsId.equals=" + notificationsId);

        // Get all the orgUserList where notifications equals to (notificationsId + 1)
        defaultOrgUserShouldNotBeFound("notificationsId.equals=" + (notificationsId + 1));
    }

    @Test
    @Transactional
    void getAllOrgUsersByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUserRepository.saveAndFlush(orgUser);
        OrgGroup group = OrgGroupResourceIT.createEntity(em);
        em.persist(group);
        em.flush();
        orgUser.setGroup(group);
        orgUserRepository.saveAndFlush(orgUser);
        Long groupId = group.getId();

        // Get all the orgUserList where group equals to groupId
        defaultOrgUserShouldBeFound("groupId.equals=" + groupId);

        // Get all the orgUserList where group equals to (groupId + 1)
        defaultOrgUserShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrgUserShouldBeFound(String filter) throws Exception {
        restOrgUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));

        // Check, that the count call also returns 1
        restOrgUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrgUserShouldNotBeFound(String filter) throws Exception {
        restOrgUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrgUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        updatedOrgUser.deviceId(UPDATED_DEVICE_ID).phone(UPDATED_PHONE).role(UPDATED_ROLE);
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
        assertThat(testOrgUser.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testOrgUser.getPhone()).isEqualTo(UPDATED_PHONE);
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

        partialUpdatedOrgUser.deviceId(UPDATED_DEVICE_ID).phone(UPDATED_PHONE);

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
        assertThat(testOrgUser.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testOrgUser.getPhone()).isEqualTo(UPDATED_PHONE);
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

        partialUpdatedOrgUser.deviceId(UPDATED_DEVICE_ID).phone(UPDATED_PHONE).role(UPDATED_ROLE);

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
        assertThat(testOrgUser.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testOrgUser.getPhone()).isEqualTo(UPDATED_PHONE);
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
