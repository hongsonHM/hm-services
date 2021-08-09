package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcGroup;
import com.overnetcontact.dvvs.repository.SvcGroupRepository;
import com.overnetcontact.dvvs.service.criteria.SvcGroupCriteria;
import com.overnetcontact.dvvs.service.dto.SvcGroupDTO;
import com.overnetcontact.dvvs.service.mapper.SvcGroupMapper;
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
 * Integration tests for the {@link SvcGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcGroupRepository svcGroupRepository;

    @Autowired
    private SvcGroupMapper svcGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcGroupMockMvc;

    private SvcGroup svcGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcGroup createEntity(EntityManager em) {
        SvcGroup svcGroup = new SvcGroup().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).address(DEFAULT_ADDRESS);
        return svcGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcGroup createUpdatedEntity(EntityManager em) {
        SvcGroup svcGroup = new SvcGroup().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS);
        return svcGroup;
    }

    @BeforeEach
    public void initTest() {
        svcGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcGroup() throws Exception {
        int databaseSizeBeforeCreate = svcGroupRepository.findAll().size();
        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);
        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeCreate + 1);
        SvcGroup testSvcGroup = svcGroupList.get(svcGroupList.size() - 1);
        assertThat(testSvcGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSvcGroup.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createSvcGroupWithExistingId() throws Exception {
        // Create the SvcGroup with an existing ID
        svcGroup.setId(1L);
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        int databaseSizeBeforeCreate = svcGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcGroupRepository.findAll().size();
        // set the field null
        svcGroup.setName(null);

        // Create the SvcGroup, which fails.
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isBadRequest());

        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcGroupRepository.findAll().size();
        // set the field null
        svcGroup.setDescription(null);

        // Create the SvcGroup, which fails.
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isBadRequest());

        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcGroupRepository.findAll().size();
        // set the field null
        svcGroup.setAddress(null);

        // Create the SvcGroup, which fails.
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isBadRequest());

        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcGroups() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList
        restSvcGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getSvcGroup() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get the svcGroup
        restSvcGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, svcGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getSvcGroupsByIdFiltering() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        Long id = svcGroup.getId();

        defaultSvcGroupShouldBeFound("id.equals=" + id);
        defaultSvcGroupShouldNotBeFound("id.notEquals=" + id);

        defaultSvcGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where name equals to DEFAULT_NAME
        defaultSvcGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the svcGroupList where name equals to UPDATED_NAME
        defaultSvcGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where name not equals to DEFAULT_NAME
        defaultSvcGroupShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the svcGroupList where name not equals to UPDATED_NAME
        defaultSvcGroupShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSvcGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the svcGroupList where name equals to UPDATED_NAME
        defaultSvcGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where name is not null
        defaultSvcGroupShouldBeFound("name.specified=true");

        // Get all the svcGroupList where name is null
        defaultSvcGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where name contains DEFAULT_NAME
        defaultSvcGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the svcGroupList where name contains UPDATED_NAME
        defaultSvcGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where name does not contain DEFAULT_NAME
        defaultSvcGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the svcGroupList where name does not contain UPDATED_NAME
        defaultSvcGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where description equals to DEFAULT_DESCRIPTION
        defaultSvcGroupShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the svcGroupList where description equals to UPDATED_DESCRIPTION
        defaultSvcGroupShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where description not equals to DEFAULT_DESCRIPTION
        defaultSvcGroupShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the svcGroupList where description not equals to UPDATED_DESCRIPTION
        defaultSvcGroupShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSvcGroupShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the svcGroupList where description equals to UPDATED_DESCRIPTION
        defaultSvcGroupShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where description is not null
        defaultSvcGroupShouldBeFound("description.specified=true");

        // Get all the svcGroupList where description is null
        defaultSvcGroupShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcGroupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where description contains DEFAULT_DESCRIPTION
        defaultSvcGroupShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the svcGroupList where description contains UPDATED_DESCRIPTION
        defaultSvcGroupShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where description does not contain DEFAULT_DESCRIPTION
        defaultSvcGroupShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the svcGroupList where description does not contain UPDATED_DESCRIPTION
        defaultSvcGroupShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where address equals to DEFAULT_ADDRESS
        defaultSvcGroupShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the svcGroupList where address equals to UPDATED_ADDRESS
        defaultSvcGroupShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where address not equals to DEFAULT_ADDRESS
        defaultSvcGroupShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the svcGroupList where address not equals to UPDATED_ADDRESS
        defaultSvcGroupShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSvcGroupShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the svcGroupList where address equals to UPDATED_ADDRESS
        defaultSvcGroupShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where address is not null
        defaultSvcGroupShouldBeFound("address.specified=true");

        // Get all the svcGroupList where address is null
        defaultSvcGroupShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcGroupsByAddressContainsSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where address contains DEFAULT_ADDRESS
        defaultSvcGroupShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the svcGroupList where address contains UPDATED_ADDRESS
        defaultSvcGroupShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcGroupsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList where address does not contain DEFAULT_ADDRESS
        defaultSvcGroupShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the svcGroupList where address does not contain UPDATED_ADDRESS
        defaultSvcGroupShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcGroupShouldBeFound(String filter) throws Exception {
        restSvcGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restSvcGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcGroupShouldNotBeFound(String filter) throws Exception {
        restSvcGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcGroup() throws Exception {
        // Get the svcGroup
        restSvcGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcGroup() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();

        // Update the svcGroup
        SvcGroup updatedSvcGroup = svcGroupRepository.findById(svcGroup.getId()).get();
        // Disconnect from session so that the updates on updatedSvcGroup are not directly saved in db
        em.detach(updatedSvcGroup);
        updatedSvcGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS);
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(updatedSvcGroup);

        restSvcGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
        SvcGroup testSvcGroup = svcGroupList.get(svcGroupList.size() - 1);
        assertThat(testSvcGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSvcGroup.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcGroupWithPatch() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();

        // Update the svcGroup using partial update
        SvcGroup partialUpdatedSvcGroup = new SvcGroup();
        partialUpdatedSvcGroup.setId(svcGroup.getId());

        partialUpdatedSvcGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS);

        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcGroup))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
        SvcGroup testSvcGroup = svcGroupList.get(svcGroupList.size() - 1);
        assertThat(testSvcGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSvcGroup.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateSvcGroupWithPatch() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();

        // Update the svcGroup using partial update
        SvcGroup partialUpdatedSvcGroup = new SvcGroup();
        partialUpdatedSvcGroup.setId(svcGroup.getId());

        partialUpdatedSvcGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS);

        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcGroup))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
        SvcGroup testSvcGroup = svcGroupList.get(svcGroupList.size() - 1);
        assertThat(testSvcGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSvcGroup.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcGroup() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        int databaseSizeBeforeDelete = svcGroupRepository.findAll().size();

        // Delete the svcGroup
        restSvcGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
