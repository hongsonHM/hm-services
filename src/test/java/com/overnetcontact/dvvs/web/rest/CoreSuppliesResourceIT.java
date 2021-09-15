package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.CoreSupplies;
import com.overnetcontact.dvvs.domain.CoreTask;
import com.overnetcontact.dvvs.repository.CoreSuppliesRepository;
import com.overnetcontact.dvvs.service.criteria.CoreSuppliesCriteria;
import com.overnetcontact.dvvs.service.dto.CoreSuppliesDTO;
import com.overnetcontact.dvvs.service.mapper.CoreSuppliesMapper;
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
 * Integration tests for the {@link CoreSuppliesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoreSuppliesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_EFFORT = "AAAAAAAAAA";
    private static final String UPDATED_EFFORT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/core-supplies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoreSuppliesRepository coreSuppliesRepository;

    @Autowired
    private CoreSuppliesMapper coreSuppliesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoreSuppliesMockMvc;

    private CoreSupplies coreSupplies;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoreSupplies createEntity(EntityManager em) {
        CoreSupplies coreSupplies = new CoreSupplies().name(DEFAULT_NAME).unit(DEFAULT_UNIT).effort(DEFAULT_EFFORT);
        return coreSupplies;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoreSupplies createUpdatedEntity(EntityManager em) {
        CoreSupplies coreSupplies = new CoreSupplies().name(UPDATED_NAME).unit(UPDATED_UNIT).effort(UPDATED_EFFORT);
        return coreSupplies;
    }

    @BeforeEach
    public void initTest() {
        coreSupplies = createEntity(em);
    }

    @Test
    @Transactional
    void createCoreSupplies() throws Exception {
        int databaseSizeBeforeCreate = coreSuppliesRepository.findAll().size();
        // Create the CoreSupplies
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(coreSupplies);
        restCoreSuppliesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeCreate + 1);
        CoreSupplies testCoreSupplies = coreSuppliesList.get(coreSuppliesList.size() - 1);
        assertThat(testCoreSupplies.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCoreSupplies.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testCoreSupplies.getEffort()).isEqualTo(DEFAULT_EFFORT);
    }

    @Test
    @Transactional
    void createCoreSuppliesWithExistingId() throws Exception {
        // Create the CoreSupplies with an existing ID
        coreSupplies.setId(1L);
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(coreSupplies);

        int databaseSizeBeforeCreate = coreSuppliesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoreSuppliesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCoreSupplies() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList
        restCoreSuppliesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coreSupplies.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].effort").value(hasItem(DEFAULT_EFFORT)));
    }

    @Test
    @Transactional
    void getCoreSupplies() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get the coreSupplies
        restCoreSuppliesMockMvc
            .perform(get(ENTITY_API_URL_ID, coreSupplies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coreSupplies.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.effort").value(DEFAULT_EFFORT));
    }

    @Test
    @Transactional
    void getCoreSuppliesByIdFiltering() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        Long id = coreSupplies.getId();

        defaultCoreSuppliesShouldBeFound("id.equals=" + id);
        defaultCoreSuppliesShouldNotBeFound("id.notEquals=" + id);

        defaultCoreSuppliesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCoreSuppliesShouldNotBeFound("id.greaterThan=" + id);

        defaultCoreSuppliesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCoreSuppliesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where name equals to DEFAULT_NAME
        defaultCoreSuppliesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the coreSuppliesList where name equals to UPDATED_NAME
        defaultCoreSuppliesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where name not equals to DEFAULT_NAME
        defaultCoreSuppliesShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the coreSuppliesList where name not equals to UPDATED_NAME
        defaultCoreSuppliesShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCoreSuppliesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the coreSuppliesList where name equals to UPDATED_NAME
        defaultCoreSuppliesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where name is not null
        defaultCoreSuppliesShouldBeFound("name.specified=true");

        // Get all the coreSuppliesList where name is null
        defaultCoreSuppliesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByNameContainsSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where name contains DEFAULT_NAME
        defaultCoreSuppliesShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the coreSuppliesList where name contains UPDATED_NAME
        defaultCoreSuppliesShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where name does not contain DEFAULT_NAME
        defaultCoreSuppliesShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the coreSuppliesList where name does not contain UPDATED_NAME
        defaultCoreSuppliesShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where unit equals to DEFAULT_UNIT
        defaultCoreSuppliesShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the coreSuppliesList where unit equals to UPDATED_UNIT
        defaultCoreSuppliesShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where unit not equals to DEFAULT_UNIT
        defaultCoreSuppliesShouldNotBeFound("unit.notEquals=" + DEFAULT_UNIT);

        // Get all the coreSuppliesList where unit not equals to UPDATED_UNIT
        defaultCoreSuppliesShouldBeFound("unit.notEquals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultCoreSuppliesShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the coreSuppliesList where unit equals to UPDATED_UNIT
        defaultCoreSuppliesShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where unit is not null
        defaultCoreSuppliesShouldBeFound("unit.specified=true");

        // Get all the coreSuppliesList where unit is null
        defaultCoreSuppliesShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByUnitContainsSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where unit contains DEFAULT_UNIT
        defaultCoreSuppliesShouldBeFound("unit.contains=" + DEFAULT_UNIT);

        // Get all the coreSuppliesList where unit contains UPDATED_UNIT
        defaultCoreSuppliesShouldNotBeFound("unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where unit does not contain DEFAULT_UNIT
        defaultCoreSuppliesShouldNotBeFound("unit.doesNotContain=" + DEFAULT_UNIT);

        // Get all the coreSuppliesList where unit does not contain UPDATED_UNIT
        defaultCoreSuppliesShouldBeFound("unit.doesNotContain=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByEffortIsEqualToSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where effort equals to DEFAULT_EFFORT
        defaultCoreSuppliesShouldBeFound("effort.equals=" + DEFAULT_EFFORT);

        // Get all the coreSuppliesList where effort equals to UPDATED_EFFORT
        defaultCoreSuppliesShouldNotBeFound("effort.equals=" + UPDATED_EFFORT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByEffortIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where effort not equals to DEFAULT_EFFORT
        defaultCoreSuppliesShouldNotBeFound("effort.notEquals=" + DEFAULT_EFFORT);

        // Get all the coreSuppliesList where effort not equals to UPDATED_EFFORT
        defaultCoreSuppliesShouldBeFound("effort.notEquals=" + UPDATED_EFFORT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByEffortIsInShouldWork() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where effort in DEFAULT_EFFORT or UPDATED_EFFORT
        defaultCoreSuppliesShouldBeFound("effort.in=" + DEFAULT_EFFORT + "," + UPDATED_EFFORT);

        // Get all the coreSuppliesList where effort equals to UPDATED_EFFORT
        defaultCoreSuppliesShouldNotBeFound("effort.in=" + UPDATED_EFFORT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByEffortIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where effort is not null
        defaultCoreSuppliesShouldBeFound("effort.specified=true");

        // Get all the coreSuppliesList where effort is null
        defaultCoreSuppliesShouldNotBeFound("effort.specified=false");
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByEffortContainsSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where effort contains DEFAULT_EFFORT
        defaultCoreSuppliesShouldBeFound("effort.contains=" + DEFAULT_EFFORT);

        // Get all the coreSuppliesList where effort contains UPDATED_EFFORT
        defaultCoreSuppliesShouldNotBeFound("effort.contains=" + UPDATED_EFFORT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByEffortNotContainsSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        // Get all the coreSuppliesList where effort does not contain DEFAULT_EFFORT
        defaultCoreSuppliesShouldNotBeFound("effort.doesNotContain=" + DEFAULT_EFFORT);

        // Get all the coreSuppliesList where effort does not contain UPDATED_EFFORT
        defaultCoreSuppliesShouldBeFound("effort.doesNotContain=" + UPDATED_EFFORT);
    }

    @Test
    @Transactional
    void getAllCoreSuppliesByCoreTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);
        CoreTask coreTask = CoreTaskResourceIT.createEntity(em);
        em.persist(coreTask);
        em.flush();
        coreSupplies.addCoreTask(coreTask);
        coreSuppliesRepository.saveAndFlush(coreSupplies);
        Long coreTaskId = coreTask.getId();

        // Get all the coreSuppliesList where coreTask equals to coreTaskId
        defaultCoreSuppliesShouldBeFound("coreTaskId.equals=" + coreTaskId);

        // Get all the coreSuppliesList where coreTask equals to (coreTaskId + 1)
        defaultCoreSuppliesShouldNotBeFound("coreTaskId.equals=" + (coreTaskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCoreSuppliesShouldBeFound(String filter) throws Exception {
        restCoreSuppliesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coreSupplies.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].effort").value(hasItem(DEFAULT_EFFORT)));

        // Check, that the count call also returns 1
        restCoreSuppliesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCoreSuppliesShouldNotBeFound(String filter) throws Exception {
        restCoreSuppliesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoreSuppliesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCoreSupplies() throws Exception {
        // Get the coreSupplies
        restCoreSuppliesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCoreSupplies() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();

        // Update the coreSupplies
        CoreSupplies updatedCoreSupplies = coreSuppliesRepository.findById(coreSupplies.getId()).get();
        // Disconnect from session so that the updates on updatedCoreSupplies are not directly saved in db
        em.detach(updatedCoreSupplies);
        updatedCoreSupplies.name(UPDATED_NAME).unit(UPDATED_UNIT).effort(UPDATED_EFFORT);
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(updatedCoreSupplies);

        restCoreSuppliesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coreSuppliesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isOk());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
        CoreSupplies testCoreSupplies = coreSuppliesList.get(coreSuppliesList.size() - 1);
        assertThat(testCoreSupplies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoreSupplies.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testCoreSupplies.getEffort()).isEqualTo(UPDATED_EFFORT);
    }

    @Test
    @Transactional
    void putNonExistingCoreSupplies() throws Exception {
        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();
        coreSupplies.setId(count.incrementAndGet());

        // Create the CoreSupplies
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(coreSupplies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoreSuppliesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coreSuppliesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoreSupplies() throws Exception {
        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();
        coreSupplies.setId(count.incrementAndGet());

        // Create the CoreSupplies
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(coreSupplies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoreSuppliesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoreSupplies() throws Exception {
        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();
        coreSupplies.setId(count.incrementAndGet());

        // Create the CoreSupplies
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(coreSupplies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoreSuppliesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoreSuppliesWithPatch() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();

        // Update the coreSupplies using partial update
        CoreSupplies partialUpdatedCoreSupplies = new CoreSupplies();
        partialUpdatedCoreSupplies.setId(coreSupplies.getId());

        partialUpdatedCoreSupplies.name(UPDATED_NAME).unit(UPDATED_UNIT).effort(UPDATED_EFFORT);

        restCoreSuppliesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoreSupplies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoreSupplies))
            )
            .andExpect(status().isOk());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
        CoreSupplies testCoreSupplies = coreSuppliesList.get(coreSuppliesList.size() - 1);
        assertThat(testCoreSupplies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoreSupplies.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testCoreSupplies.getEffort()).isEqualTo(UPDATED_EFFORT);
    }

    @Test
    @Transactional
    void fullUpdateCoreSuppliesWithPatch() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();

        // Update the coreSupplies using partial update
        CoreSupplies partialUpdatedCoreSupplies = new CoreSupplies();
        partialUpdatedCoreSupplies.setId(coreSupplies.getId());

        partialUpdatedCoreSupplies.name(UPDATED_NAME).unit(UPDATED_UNIT).effort(UPDATED_EFFORT);

        restCoreSuppliesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoreSupplies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoreSupplies))
            )
            .andExpect(status().isOk());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
        CoreSupplies testCoreSupplies = coreSuppliesList.get(coreSuppliesList.size() - 1);
        assertThat(testCoreSupplies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoreSupplies.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testCoreSupplies.getEffort()).isEqualTo(UPDATED_EFFORT);
    }

    @Test
    @Transactional
    void patchNonExistingCoreSupplies() throws Exception {
        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();
        coreSupplies.setId(count.incrementAndGet());

        // Create the CoreSupplies
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(coreSupplies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoreSuppliesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coreSuppliesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoreSupplies() throws Exception {
        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();
        coreSupplies.setId(count.incrementAndGet());

        // Create the CoreSupplies
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(coreSupplies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoreSuppliesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoreSupplies() throws Exception {
        int databaseSizeBeforeUpdate = coreSuppliesRepository.findAll().size();
        coreSupplies.setId(count.incrementAndGet());

        // Create the CoreSupplies
        CoreSuppliesDTO coreSuppliesDTO = coreSuppliesMapper.toDto(coreSupplies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoreSuppliesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coreSuppliesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoreSupplies in the database
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoreSupplies() throws Exception {
        // Initialize the database
        coreSuppliesRepository.saveAndFlush(coreSupplies);

        int databaseSizeBeforeDelete = coreSuppliesRepository.findAll().size();

        // Delete the coreSupplies
        restCoreSuppliesMockMvc
            .perform(delete(ENTITY_API_URL_ID, coreSupplies.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CoreSupplies> coreSuppliesList = coreSuppliesRepository.findAll();
        assertThat(coreSuppliesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
