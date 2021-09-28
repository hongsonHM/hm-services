package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcArea;
import com.overnetcontact.dvvs.domain.SvcGroupTask;
import com.overnetcontact.dvvs.repository.SvcAreaRepository;
import com.overnetcontact.dvvs.service.criteria.SvcAreaCriteria;
import com.overnetcontact.dvvs.service.dto.SvcAreaDTO;
import com.overnetcontact.dvvs.service.mapper.SvcAreaMapper;
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
 * Integration tests for the {@link SvcAreaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcAreaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_CONTRACTS_ID = Long.valueOf(1);
    private static final Long UPDATED_CONTRACTS_ID = Long.valueOf(2);
    private static final Long SMALLER_CONTRACTS_ID = Long.valueOf(1 - 1);

    private static final String ENTITY_API_URL = "/api/svc-areas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcAreaRepository svcAreaRepository;

    @Autowired
    private SvcAreaMapper svcAreaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcAreaMockMvc;

    private SvcArea svcArea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcArea createEntity(EntityManager em) {
        SvcArea svcArea = new SvcArea().name(DEFAULT_NAME).location(DEFAULT_LOCATION).type(DEFAULT_TYPE).contractsId(DEFAULT_CONTRACTS_ID);
        return svcArea;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcArea createUpdatedEntity(EntityManager em) {
        SvcArea svcArea = new SvcArea().name(UPDATED_NAME).location(UPDATED_LOCATION).type(UPDATED_TYPE).contractsId(UPDATED_CONTRACTS_ID);
        return svcArea;
    }

    @BeforeEach
    public void initTest() {
        svcArea = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcArea() throws Exception {
        int databaseSizeBeforeCreate = svcAreaRepository.findAll().size();
        // Create the SvcArea
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(svcArea);
        restSvcAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcAreaDTO)))
            .andExpect(status().isCreated());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeCreate + 1);
        SvcArea testSvcArea = svcAreaList.get(svcAreaList.size() - 1);
        assertThat(testSvcArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcArea.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSvcArea.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSvcArea.getContractsId()).isEqualTo(DEFAULT_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void createSvcAreaWithExistingId() throws Exception {
        // Create the SvcArea with an existing ID
        svcArea.setId(1L);
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(svcArea);

        int databaseSizeBeforeCreate = svcAreaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSvcAreas() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList
        restSvcAreaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].contractsId").value(hasItem(DEFAULT_CONTRACTS_ID)));
    }

    @Test
    @Transactional
    void getSvcArea() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get the svcArea
        restSvcAreaMockMvc
            .perform(get(ENTITY_API_URL_ID, svcArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcArea.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.contractsId").value(DEFAULT_CONTRACTS_ID));
    }

    @Test
    @Transactional
    void getSvcAreasByIdFiltering() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        Long id = svcArea.getId();

        defaultSvcAreaShouldBeFound("id.equals=" + id);
        defaultSvcAreaShouldNotBeFound("id.notEquals=" + id);

        defaultSvcAreaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcAreaShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcAreaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcAreaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcAreasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where name equals to DEFAULT_NAME
        defaultSvcAreaShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the svcAreaList where name equals to UPDATED_NAME
        defaultSvcAreaShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcAreasByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where name not equals to DEFAULT_NAME
        defaultSvcAreaShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the svcAreaList where name not equals to UPDATED_NAME
        defaultSvcAreaShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcAreasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSvcAreaShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the svcAreaList where name equals to UPDATED_NAME
        defaultSvcAreaShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcAreasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where name is not null
        defaultSvcAreaShouldBeFound("name.specified=true");

        // Get all the svcAreaList where name is null
        defaultSvcAreaShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcAreasByNameContainsSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where name contains DEFAULT_NAME
        defaultSvcAreaShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the svcAreaList where name contains UPDATED_NAME
        defaultSvcAreaShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcAreasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where name does not contain DEFAULT_NAME
        defaultSvcAreaShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the svcAreaList where name does not contain UPDATED_NAME
        defaultSvcAreaShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcAreasByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where location equals to DEFAULT_LOCATION
        defaultSvcAreaShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the svcAreaList where location equals to UPDATED_LOCATION
        defaultSvcAreaShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcAreasByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where location not equals to DEFAULT_LOCATION
        defaultSvcAreaShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the svcAreaList where location not equals to UPDATED_LOCATION
        defaultSvcAreaShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcAreasByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultSvcAreaShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the svcAreaList where location equals to UPDATED_LOCATION
        defaultSvcAreaShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcAreasByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where location is not null
        defaultSvcAreaShouldBeFound("location.specified=true");

        // Get all the svcAreaList where location is null
        defaultSvcAreaShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcAreasByLocationContainsSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where location contains DEFAULT_LOCATION
        defaultSvcAreaShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the svcAreaList where location contains UPDATED_LOCATION
        defaultSvcAreaShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcAreasByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where location does not contain DEFAULT_LOCATION
        defaultSvcAreaShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the svcAreaList where location does not contain UPDATED_LOCATION
        defaultSvcAreaShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcAreasByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where type equals to DEFAULT_TYPE
        defaultSvcAreaShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the svcAreaList where type equals to UPDATED_TYPE
        defaultSvcAreaShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcAreasByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where type not equals to DEFAULT_TYPE
        defaultSvcAreaShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the svcAreaList where type not equals to UPDATED_TYPE
        defaultSvcAreaShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcAreasByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSvcAreaShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the svcAreaList where type equals to UPDATED_TYPE
        defaultSvcAreaShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcAreasByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where type is not null
        defaultSvcAreaShouldBeFound("type.specified=true");

        // Get all the svcAreaList where type is null
        defaultSvcAreaShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcAreasByTypeContainsSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where type contains DEFAULT_TYPE
        defaultSvcAreaShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the svcAreaList where type contains UPDATED_TYPE
        defaultSvcAreaShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcAreasByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where type does not contain DEFAULT_TYPE
        defaultSvcAreaShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the svcAreaList where type does not contain UPDATED_TYPE
        defaultSvcAreaShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcAreasByContractsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where contractsId equals to DEFAULT_CONTRACTS_ID
        defaultSvcAreaShouldBeFound("contractsId.equals=" + DEFAULT_CONTRACTS_ID);

        // Get all the svcAreaList where contractsId equals to UPDATED_CONTRACTS_ID
        defaultSvcAreaShouldNotBeFound("contractsId.equals=" + UPDATED_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void getAllSvcAreasByContractsIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where contractsId not equals to DEFAULT_CONTRACTS_ID
        defaultSvcAreaShouldNotBeFound("contractsId.notEquals=" + DEFAULT_CONTRACTS_ID);

        // Get all the svcAreaList where contractsId not equals to UPDATED_CONTRACTS_ID
        defaultSvcAreaShouldBeFound("contractsId.notEquals=" + UPDATED_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void getAllSvcAreasByContractsIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where contractsId in DEFAULT_CONTRACTS_ID or UPDATED_CONTRACTS_ID
        defaultSvcAreaShouldBeFound("contractsId.in=" + DEFAULT_CONTRACTS_ID + "," + UPDATED_CONTRACTS_ID);

        // Get all the svcAreaList where contractsId equals to UPDATED_CONTRACTS_ID
        defaultSvcAreaShouldNotBeFound("contractsId.in=" + UPDATED_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void getAllSvcAreasByContractsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where contractsId is not null
        defaultSvcAreaShouldBeFound("contractsId.specified=true");

        // Get all the svcAreaList where contractsId is null
        defaultSvcAreaShouldNotBeFound("contractsId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcAreasByContractsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where contractsId is greater than or equal to DEFAULT_CONTRACTS_ID
        defaultSvcAreaShouldBeFound("contractsId.greaterThanOrEqual=" + DEFAULT_CONTRACTS_ID);

        // Get all the svcAreaList where contractsId is greater than or equal to UPDATED_CONTRACTS_ID
        defaultSvcAreaShouldNotBeFound("contractsId.greaterThanOrEqual=" + UPDATED_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void getAllSvcAreasByContractsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where contractsId is less than or equal to DEFAULT_CONTRACTS_ID
        defaultSvcAreaShouldBeFound("contractsId.lessThanOrEqual=" + DEFAULT_CONTRACTS_ID);

        // Get all the svcAreaList where contractsId is less than or equal to SMALLER_CONTRACTS_ID
        defaultSvcAreaShouldNotBeFound("contractsId.lessThanOrEqual=" + SMALLER_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void getAllSvcAreasByContractsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where contractsId is less than DEFAULT_CONTRACTS_ID
        defaultSvcAreaShouldNotBeFound("contractsId.lessThan=" + DEFAULT_CONTRACTS_ID);

        // Get all the svcAreaList where contractsId is less than UPDATED_CONTRACTS_ID
        defaultSvcAreaShouldBeFound("contractsId.lessThan=" + UPDATED_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void getAllSvcAreasByContractsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        // Get all the svcAreaList where contractsId is greater than DEFAULT_CONTRACTS_ID
        defaultSvcAreaShouldNotBeFound("contractsId.greaterThan=" + DEFAULT_CONTRACTS_ID);

        // Get all the svcAreaList where contractsId is greater than SMALLER_CONTRACTS_ID
        defaultSvcAreaShouldBeFound("contractsId.greaterThan=" + SMALLER_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void getAllSvcAreasBySvcGroupTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);
        SvcGroupTask svcGroupTask = SvcGroupTaskResourceIT.createEntity(em);
        em.persist(svcGroupTask);
        em.flush();
        svcArea.setSvcGroupTask(svcGroupTask);
        svcGroupTask.setSvcArea(svcArea);
        svcAreaRepository.saveAndFlush(svcArea);
        Long svcGroupTaskId = svcGroupTask.getId();

        // Get all the svcAreaList where svcGroupTask equals to svcGroupTaskId
        defaultSvcAreaShouldBeFound("svcGroupTaskId.equals=" + svcGroupTaskId);

        // Get all the svcAreaList where svcGroupTask equals to (svcGroupTaskId + 1)
        defaultSvcAreaShouldNotBeFound("svcGroupTaskId.equals=" + (svcGroupTaskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcAreaShouldBeFound(String filter) throws Exception {
        restSvcAreaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].contractsId").value(hasItem(DEFAULT_CONTRACTS_ID)));

        // Check, that the count call also returns 1
        restSvcAreaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcAreaShouldNotBeFound(String filter) throws Exception {
        restSvcAreaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcAreaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcArea() throws Exception {
        // Get the svcArea
        restSvcAreaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcArea() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();

        // Update the svcArea
        SvcArea updatedSvcArea = svcAreaRepository.findById(svcArea.getId()).get();
        // Disconnect from session so that the updates on updatedSvcArea are not directly saved in db
        em.detach(updatedSvcArea);
        updatedSvcArea.name(UPDATED_NAME).location(UPDATED_LOCATION).type(UPDATED_TYPE).contractsId(UPDATED_CONTRACTS_ID);
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(updatedSvcArea);

        restSvcAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcAreaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcAreaDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
        SvcArea testSvcArea = svcAreaList.get(svcAreaList.size() - 1);
        assertThat(testSvcArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcArea.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSvcArea.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSvcArea.getContractsId()).isEqualTo(UPDATED_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void putNonExistingSvcArea() throws Exception {
        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();
        svcArea.setId(count.incrementAndGet());

        // Create the SvcArea
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(svcArea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcAreaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcAreaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcArea() throws Exception {
        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();
        svcArea.setId(count.incrementAndGet());

        // Create the SvcArea
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(svcArea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcAreaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcArea() throws Exception {
        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();
        svcArea.setId(count.incrementAndGet());

        // Create the SvcArea
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(svcArea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcAreaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcAreaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcAreaWithPatch() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();

        // Update the svcArea using partial update
        SvcArea partialUpdatedSvcArea = new SvcArea();
        partialUpdatedSvcArea.setId(svcArea.getId());

        partialUpdatedSvcArea.contractsId(UPDATED_CONTRACTS_ID);

        restSvcAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcArea))
            )
            .andExpect(status().isOk());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
        SvcArea testSvcArea = svcAreaList.get(svcAreaList.size() - 1);
        assertThat(testSvcArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcArea.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSvcArea.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSvcArea.getContractsId()).isEqualTo(UPDATED_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void fullUpdateSvcAreaWithPatch() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();

        // Update the svcArea using partial update
        SvcArea partialUpdatedSvcArea = new SvcArea();
        partialUpdatedSvcArea.setId(svcArea.getId());

        partialUpdatedSvcArea.name(UPDATED_NAME).location(UPDATED_LOCATION).type(UPDATED_TYPE).contractsId(UPDATED_CONTRACTS_ID);

        restSvcAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcArea))
            )
            .andExpect(status().isOk());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
        SvcArea testSvcArea = svcAreaList.get(svcAreaList.size() - 1);
        assertThat(testSvcArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcArea.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSvcArea.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSvcArea.getContractsId()).isEqualTo(UPDATED_CONTRACTS_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSvcArea() throws Exception {
        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();
        svcArea.setId(count.incrementAndGet());

        // Create the SvcArea
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(svcArea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcAreaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcAreaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcArea() throws Exception {
        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();
        svcArea.setId(count.incrementAndGet());

        // Create the SvcArea
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(svcArea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcAreaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcArea() throws Exception {
        int databaseSizeBeforeUpdate = svcAreaRepository.findAll().size();
        svcArea.setId(count.incrementAndGet());

        // Create the SvcArea
        SvcAreaDTO svcAreaDTO = svcAreaMapper.toDto(svcArea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcAreaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcAreaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcArea in the database
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcArea() throws Exception {
        // Initialize the database
        svcAreaRepository.saveAndFlush(svcArea);

        int databaseSizeBeforeDelete = svcAreaRepository.findAll().size();

        // Delete the svcArea
        restSvcAreaMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcArea.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcArea> svcAreaList = svcAreaRepository.findAll();
        assertThat(svcAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
