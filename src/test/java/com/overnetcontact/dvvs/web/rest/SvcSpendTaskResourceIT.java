package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcGroupTask;
import com.overnetcontact.dvvs.domain.SvcSpendTask;
import com.overnetcontact.dvvs.repository.SvcSpendTaskRepository;
import com.overnetcontact.dvvs.service.criteria.SvcSpendTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcSpendTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcSpendTaskMapper;
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
 * Integration tests for the {@link SvcSpendTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcSpendTaskResourceIT {

    private static final Integer DEFAULT_CORE_TASK_ID = 1;
    private static final Integer UPDATED_CORE_TASK_ID = 2;
    private static final Integer SMALLER_CORE_TASK_ID = 1 - 1;

    private static final String DEFAULT_MASS = "AAAAAAAAAA";
    private static final String UPDATED_MASS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-spend-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcSpendTaskRepository svcSpendTaskRepository;

    @Autowired
    private SvcSpendTaskMapper svcSpendTaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcSpendTaskMockMvc;

    private SvcSpendTask svcSpendTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcSpendTask createEntity(EntityManager em) {
        SvcSpendTask svcSpendTask = new SvcSpendTask().coreTaskId(DEFAULT_CORE_TASK_ID).mass(DEFAULT_MASS).note(DEFAULT_NOTE);
        return svcSpendTask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcSpendTask createUpdatedEntity(EntityManager em) {
        SvcSpendTask svcSpendTask = new SvcSpendTask().coreTaskId(UPDATED_CORE_TASK_ID).mass(UPDATED_MASS).note(UPDATED_NOTE);
        return svcSpendTask;
    }

    @BeforeEach
    public void initTest() {
        svcSpendTask = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcSpendTask() throws Exception {
        int databaseSizeBeforeCreate = svcSpendTaskRepository.findAll().size();
        // Create the SvcSpendTask
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(svcSpendTask);
        restSvcSpendTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeCreate + 1);
        SvcSpendTask testSvcSpendTask = svcSpendTaskList.get(svcSpendTaskList.size() - 1);
        assertThat(testSvcSpendTask.getCoreTaskId()).isEqualTo(DEFAULT_CORE_TASK_ID);
        assertThat(testSvcSpendTask.getMass()).isEqualTo(DEFAULT_MASS);
        assertThat(testSvcSpendTask.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createSvcSpendTaskWithExistingId() throws Exception {
        // Create the SvcSpendTask with an existing ID
        svcSpendTask.setId(1L);
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(svcSpendTask);

        int databaseSizeBeforeCreate = svcSpendTaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcSpendTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasks() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList
        restSvcSpendTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcSpendTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].coreTaskId").value(hasItem(DEFAULT_CORE_TASK_ID)))
            .andExpect(jsonPath("$.[*].mass").value(hasItem(DEFAULT_MASS)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getSvcSpendTask() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get the svcSpendTask
        restSvcSpendTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, svcSpendTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcSpendTask.getId().intValue()))
            .andExpect(jsonPath("$.coreTaskId").value(DEFAULT_CORE_TASK_ID))
            .andExpect(jsonPath("$.mass").value(DEFAULT_MASS))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getSvcSpendTasksByIdFiltering() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        Long id = svcSpendTask.getId();

        defaultSvcSpendTaskShouldBeFound("id.equals=" + id);
        defaultSvcSpendTaskShouldNotBeFound("id.notEquals=" + id);

        defaultSvcSpendTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcSpendTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcSpendTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcSpendTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByCoreTaskIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where coreTaskId equals to DEFAULT_CORE_TASK_ID
        defaultSvcSpendTaskShouldBeFound("coreTaskId.equals=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcSpendTaskList where coreTaskId equals to UPDATED_CORE_TASK_ID
        defaultSvcSpendTaskShouldNotBeFound("coreTaskId.equals=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByCoreTaskIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where coreTaskId not equals to DEFAULT_CORE_TASK_ID
        defaultSvcSpendTaskShouldNotBeFound("coreTaskId.notEquals=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcSpendTaskList where coreTaskId not equals to UPDATED_CORE_TASK_ID
        defaultSvcSpendTaskShouldBeFound("coreTaskId.notEquals=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByCoreTaskIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where coreTaskId in DEFAULT_CORE_TASK_ID or UPDATED_CORE_TASK_ID
        defaultSvcSpendTaskShouldBeFound("coreTaskId.in=" + DEFAULT_CORE_TASK_ID + "," + UPDATED_CORE_TASK_ID);

        // Get all the svcSpendTaskList where coreTaskId equals to UPDATED_CORE_TASK_ID
        defaultSvcSpendTaskShouldNotBeFound("coreTaskId.in=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByCoreTaskIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where coreTaskId is not null
        defaultSvcSpendTaskShouldBeFound("coreTaskId.specified=true");

        // Get all the svcSpendTaskList where coreTaskId is null
        defaultSvcSpendTaskShouldNotBeFound("coreTaskId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByCoreTaskIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where coreTaskId is greater than or equal to DEFAULT_CORE_TASK_ID
        defaultSvcSpendTaskShouldBeFound("coreTaskId.greaterThanOrEqual=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcSpendTaskList where coreTaskId is greater than or equal to UPDATED_CORE_TASK_ID
        defaultSvcSpendTaskShouldNotBeFound("coreTaskId.greaterThanOrEqual=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByCoreTaskIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where coreTaskId is less than or equal to DEFAULT_CORE_TASK_ID
        defaultSvcSpendTaskShouldBeFound("coreTaskId.lessThanOrEqual=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcSpendTaskList where coreTaskId is less than or equal to SMALLER_CORE_TASK_ID
        defaultSvcSpendTaskShouldNotBeFound("coreTaskId.lessThanOrEqual=" + SMALLER_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByCoreTaskIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where coreTaskId is less than DEFAULT_CORE_TASK_ID
        defaultSvcSpendTaskShouldNotBeFound("coreTaskId.lessThan=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcSpendTaskList where coreTaskId is less than UPDATED_CORE_TASK_ID
        defaultSvcSpendTaskShouldBeFound("coreTaskId.lessThan=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByCoreTaskIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where coreTaskId is greater than DEFAULT_CORE_TASK_ID
        defaultSvcSpendTaskShouldNotBeFound("coreTaskId.greaterThan=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcSpendTaskList where coreTaskId is greater than SMALLER_CORE_TASK_ID
        defaultSvcSpendTaskShouldBeFound("coreTaskId.greaterThan=" + SMALLER_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByMassIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where mass equals to DEFAULT_MASS
        defaultSvcSpendTaskShouldBeFound("mass.equals=" + DEFAULT_MASS);

        // Get all the svcSpendTaskList where mass equals to UPDATED_MASS
        defaultSvcSpendTaskShouldNotBeFound("mass.equals=" + UPDATED_MASS);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByMassIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where mass not equals to DEFAULT_MASS
        defaultSvcSpendTaskShouldNotBeFound("mass.notEquals=" + DEFAULT_MASS);

        // Get all the svcSpendTaskList where mass not equals to UPDATED_MASS
        defaultSvcSpendTaskShouldBeFound("mass.notEquals=" + UPDATED_MASS);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByMassIsInShouldWork() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where mass in DEFAULT_MASS or UPDATED_MASS
        defaultSvcSpendTaskShouldBeFound("mass.in=" + DEFAULT_MASS + "," + UPDATED_MASS);

        // Get all the svcSpendTaskList where mass equals to UPDATED_MASS
        defaultSvcSpendTaskShouldNotBeFound("mass.in=" + UPDATED_MASS);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByMassIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where mass is not null
        defaultSvcSpendTaskShouldBeFound("mass.specified=true");

        // Get all the svcSpendTaskList where mass is null
        defaultSvcSpendTaskShouldNotBeFound("mass.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByMassContainsSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where mass contains DEFAULT_MASS
        defaultSvcSpendTaskShouldBeFound("mass.contains=" + DEFAULT_MASS);

        // Get all the svcSpendTaskList where mass contains UPDATED_MASS
        defaultSvcSpendTaskShouldNotBeFound("mass.contains=" + UPDATED_MASS);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByMassNotContainsSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where mass does not contain DEFAULT_MASS
        defaultSvcSpendTaskShouldNotBeFound("mass.doesNotContain=" + DEFAULT_MASS);

        // Get all the svcSpendTaskList where mass does not contain UPDATED_MASS
        defaultSvcSpendTaskShouldBeFound("mass.doesNotContain=" + UPDATED_MASS);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where note equals to DEFAULT_NOTE
        defaultSvcSpendTaskShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the svcSpendTaskList where note equals to UPDATED_NOTE
        defaultSvcSpendTaskShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where note not equals to DEFAULT_NOTE
        defaultSvcSpendTaskShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the svcSpendTaskList where note not equals to UPDATED_NOTE
        defaultSvcSpendTaskShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultSvcSpendTaskShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the svcSpendTaskList where note equals to UPDATED_NOTE
        defaultSvcSpendTaskShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where note is not null
        defaultSvcSpendTaskShouldBeFound("note.specified=true");

        // Get all the svcSpendTaskList where note is null
        defaultSvcSpendTaskShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByNoteContainsSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where note contains DEFAULT_NOTE
        defaultSvcSpendTaskShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the svcSpendTaskList where note contains UPDATED_NOTE
        defaultSvcSpendTaskShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        // Get all the svcSpendTaskList where note does not contain DEFAULT_NOTE
        defaultSvcSpendTaskShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the svcSpendTaskList where note does not contain UPDATED_NOTE
        defaultSvcSpendTaskShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcSpendTasksBySvcGroupTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);
        SvcGroupTask svcGroupTask = SvcGroupTaskResourceIT.createEntity(em);
        em.persist(svcGroupTask);
        em.flush();
        svcSpendTask.setSvcGroupTask(svcGroupTask);
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);
        Long svcGroupTaskId = svcGroupTask.getId();

        // Get all the svcSpendTaskList where svcGroupTask equals to svcGroupTaskId
        defaultSvcSpendTaskShouldBeFound("svcGroupTaskId.equals=" + svcGroupTaskId);

        // Get all the svcSpendTaskList where svcGroupTask equals to (svcGroupTaskId + 1)
        defaultSvcSpendTaskShouldNotBeFound("svcGroupTaskId.equals=" + (svcGroupTaskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcSpendTaskShouldBeFound(String filter) throws Exception {
        restSvcSpendTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcSpendTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].coreTaskId").value(hasItem(DEFAULT_CORE_TASK_ID)))
            .andExpect(jsonPath("$.[*].mass").value(hasItem(DEFAULT_MASS)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restSvcSpendTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcSpendTaskShouldNotBeFound(String filter) throws Exception {
        restSvcSpendTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcSpendTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcSpendTask() throws Exception {
        // Get the svcSpendTask
        restSvcSpendTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcSpendTask() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();

        // Update the svcSpendTask
        SvcSpendTask updatedSvcSpendTask = svcSpendTaskRepository.findById(svcSpendTask.getId()).get();
        // Disconnect from session so that the updates on updatedSvcSpendTask are not directly saved in db
        em.detach(updatedSvcSpendTask);
        updatedSvcSpendTask.coreTaskId(UPDATED_CORE_TASK_ID).mass(UPDATED_MASS).note(UPDATED_NOTE);
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(updatedSvcSpendTask);

        restSvcSpendTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcSpendTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcSpendTask testSvcSpendTask = svcSpendTaskList.get(svcSpendTaskList.size() - 1);
        assertThat(testSvcSpendTask.getCoreTaskId()).isEqualTo(UPDATED_CORE_TASK_ID);
        assertThat(testSvcSpendTask.getMass()).isEqualTo(UPDATED_MASS);
        assertThat(testSvcSpendTask.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingSvcSpendTask() throws Exception {
        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();
        svcSpendTask.setId(count.incrementAndGet());

        // Create the SvcSpendTask
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(svcSpendTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcSpendTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcSpendTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcSpendTask() throws Exception {
        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();
        svcSpendTask.setId(count.incrementAndGet());

        // Create the SvcSpendTask
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(svcSpendTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSpendTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcSpendTask() throws Exception {
        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();
        svcSpendTask.setId(count.incrementAndGet());

        // Create the SvcSpendTask
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(svcSpendTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSpendTaskMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcSpendTaskWithPatch() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();

        // Update the svcSpendTask using partial update
        SvcSpendTask partialUpdatedSvcSpendTask = new SvcSpendTask();
        partialUpdatedSvcSpendTask.setId(svcSpendTask.getId());

        partialUpdatedSvcSpendTask.note(UPDATED_NOTE);

        restSvcSpendTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcSpendTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcSpendTask))
            )
            .andExpect(status().isOk());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcSpendTask testSvcSpendTask = svcSpendTaskList.get(svcSpendTaskList.size() - 1);
        assertThat(testSvcSpendTask.getCoreTaskId()).isEqualTo(DEFAULT_CORE_TASK_ID);
        assertThat(testSvcSpendTask.getMass()).isEqualTo(DEFAULT_MASS);
        assertThat(testSvcSpendTask.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateSvcSpendTaskWithPatch() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();

        // Update the svcSpendTask using partial update
        SvcSpendTask partialUpdatedSvcSpendTask = new SvcSpendTask();
        partialUpdatedSvcSpendTask.setId(svcSpendTask.getId());

        partialUpdatedSvcSpendTask.coreTaskId(UPDATED_CORE_TASK_ID).mass(UPDATED_MASS).note(UPDATED_NOTE);

        restSvcSpendTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcSpendTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcSpendTask))
            )
            .andExpect(status().isOk());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcSpendTask testSvcSpendTask = svcSpendTaskList.get(svcSpendTaskList.size() - 1);
        assertThat(testSvcSpendTask.getCoreTaskId()).isEqualTo(UPDATED_CORE_TASK_ID);
        assertThat(testSvcSpendTask.getMass()).isEqualTo(UPDATED_MASS);
        assertThat(testSvcSpendTask.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingSvcSpendTask() throws Exception {
        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();
        svcSpendTask.setId(count.incrementAndGet());

        // Create the SvcSpendTask
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(svcSpendTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcSpendTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcSpendTaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcSpendTask() throws Exception {
        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();
        svcSpendTask.setId(count.incrementAndGet());

        // Create the SvcSpendTask
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(svcSpendTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSpendTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcSpendTask() throws Exception {
        int databaseSizeBeforeUpdate = svcSpendTaskRepository.findAll().size();
        svcSpendTask.setId(count.incrementAndGet());

        // Create the SvcSpendTask
        SvcSpendTaskDTO svcSpendTaskDTO = svcSpendTaskMapper.toDto(svcSpendTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSpendTaskMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSpendTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcSpendTask in the database
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcSpendTask() throws Exception {
        // Initialize the database
        svcSpendTaskRepository.saveAndFlush(svcSpendTask);

        int databaseSizeBeforeDelete = svcSpendTaskRepository.findAll().size();

        // Delete the svcSpendTask
        restSvcSpendTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcSpendTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcSpendTask> svcSpendTaskList = svcSpendTaskRepository.findAll();
        assertThat(svcSpendTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
