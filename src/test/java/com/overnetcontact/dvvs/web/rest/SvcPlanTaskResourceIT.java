package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcPlanTask;
import com.overnetcontact.dvvs.domain.SvcPlanUnit;
import com.overnetcontact.dvvs.repository.SvcPlanTaskRepository;
import com.overnetcontact.dvvs.service.criteria.SvcPlanTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanTaskMapper;
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
 * Integration tests for the {@link SvcPlanTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcPlanTaskResourceIT {

    private static final Long DEFAULT_CORE_TASK_ID = 1L;
    private static final Long UPDATED_CORE_TASK_ID = 2L;
    private static final Long SMALLER_CORE_TASK_ID = 1L - 1L;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-plan-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcPlanTaskRepository svcPlanTaskRepository;

    @Autowired
    private SvcPlanTaskMapper svcPlanTaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcPlanTaskMockMvc;

    private SvcPlanTask svcPlanTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcPlanTask createEntity(EntityManager em) {
        SvcPlanTask svcPlanTask = new SvcPlanTask().coreTaskId(DEFAULT_CORE_TASK_ID).note(DEFAULT_NOTE);
        return svcPlanTask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcPlanTask createUpdatedEntity(EntityManager em) {
        SvcPlanTask svcPlanTask = new SvcPlanTask().coreTaskId(UPDATED_CORE_TASK_ID).note(UPDATED_NOTE);
        return svcPlanTask;
    }

    @BeforeEach
    public void initTest() {
        svcPlanTask = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcPlanTask() throws Exception {
        int databaseSizeBeforeCreate = svcPlanTaskRepository.findAll().size();
        // Create the SvcPlanTask
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);
        restSvcPlanTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeCreate + 1);
        SvcPlanTask testSvcPlanTask = svcPlanTaskList.get(svcPlanTaskList.size() - 1);
        assertThat(testSvcPlanTask.getCoreTaskId()).isEqualTo(DEFAULT_CORE_TASK_ID);
        assertThat(testSvcPlanTask.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createSvcPlanTaskWithExistingId() throws Exception {
        // Create the SvcPlanTask with an existing ID
        svcPlanTask.setId(1L);
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);

        int databaseSizeBeforeCreate = svcPlanTaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcPlanTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCoreTaskIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcPlanTaskRepository.findAll().size();
        // set the field null
        svcPlanTask.setCoreTaskId(null);

        // Create the SvcPlanTask, which fails.
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);

        restSvcPlanTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasks() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList
        restSvcPlanTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcPlanTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].coreTaskId").value(hasItem(DEFAULT_CORE_TASK_ID.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getSvcPlanTask() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get the svcPlanTask
        restSvcPlanTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, svcPlanTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcPlanTask.getId().intValue()))
            .andExpect(jsonPath("$.coreTaskId").value(DEFAULT_CORE_TASK_ID.intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getSvcPlanTasksByIdFiltering() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        Long id = svcPlanTask.getId();

        defaultSvcPlanTaskShouldBeFound("id.equals=" + id);
        defaultSvcPlanTaskShouldNotBeFound("id.notEquals=" + id);

        defaultSvcPlanTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcPlanTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcPlanTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcPlanTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByCoreTaskIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where coreTaskId equals to DEFAULT_CORE_TASK_ID
        defaultSvcPlanTaskShouldBeFound("coreTaskId.equals=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcPlanTaskList where coreTaskId equals to UPDATED_CORE_TASK_ID
        defaultSvcPlanTaskShouldNotBeFound("coreTaskId.equals=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByCoreTaskIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where coreTaskId not equals to DEFAULT_CORE_TASK_ID
        defaultSvcPlanTaskShouldNotBeFound("coreTaskId.notEquals=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcPlanTaskList where coreTaskId not equals to UPDATED_CORE_TASK_ID
        defaultSvcPlanTaskShouldBeFound("coreTaskId.notEquals=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByCoreTaskIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where coreTaskId in DEFAULT_CORE_TASK_ID or UPDATED_CORE_TASK_ID
        defaultSvcPlanTaskShouldBeFound("coreTaskId.in=" + DEFAULT_CORE_TASK_ID + "," + UPDATED_CORE_TASK_ID);

        // Get all the svcPlanTaskList where coreTaskId equals to UPDATED_CORE_TASK_ID
        defaultSvcPlanTaskShouldNotBeFound("coreTaskId.in=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByCoreTaskIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where coreTaskId is not null
        defaultSvcPlanTaskShouldBeFound("coreTaskId.specified=true");

        // Get all the svcPlanTaskList where coreTaskId is null
        defaultSvcPlanTaskShouldNotBeFound("coreTaskId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByCoreTaskIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where coreTaskId is greater than or equal to DEFAULT_CORE_TASK_ID
        defaultSvcPlanTaskShouldBeFound("coreTaskId.greaterThanOrEqual=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcPlanTaskList where coreTaskId is greater than or equal to UPDATED_CORE_TASK_ID
        defaultSvcPlanTaskShouldNotBeFound("coreTaskId.greaterThanOrEqual=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByCoreTaskIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where coreTaskId is less than or equal to DEFAULT_CORE_TASK_ID
        defaultSvcPlanTaskShouldBeFound("coreTaskId.lessThanOrEqual=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcPlanTaskList where coreTaskId is less than or equal to SMALLER_CORE_TASK_ID
        defaultSvcPlanTaskShouldNotBeFound("coreTaskId.lessThanOrEqual=" + SMALLER_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByCoreTaskIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where coreTaskId is less than DEFAULT_CORE_TASK_ID
        defaultSvcPlanTaskShouldNotBeFound("coreTaskId.lessThan=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcPlanTaskList where coreTaskId is less than UPDATED_CORE_TASK_ID
        defaultSvcPlanTaskShouldBeFound("coreTaskId.lessThan=" + UPDATED_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByCoreTaskIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where coreTaskId is greater than DEFAULT_CORE_TASK_ID
        defaultSvcPlanTaskShouldNotBeFound("coreTaskId.greaterThan=" + DEFAULT_CORE_TASK_ID);

        // Get all the svcPlanTaskList where coreTaskId is greater than SMALLER_CORE_TASK_ID
        defaultSvcPlanTaskShouldBeFound("coreTaskId.greaterThan=" + SMALLER_CORE_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where note equals to DEFAULT_NOTE
        defaultSvcPlanTaskShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the svcPlanTaskList where note equals to UPDATED_NOTE
        defaultSvcPlanTaskShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where note not equals to DEFAULT_NOTE
        defaultSvcPlanTaskShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the svcPlanTaskList where note not equals to UPDATED_NOTE
        defaultSvcPlanTaskShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultSvcPlanTaskShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the svcPlanTaskList where note equals to UPDATED_NOTE
        defaultSvcPlanTaskShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where note is not null
        defaultSvcPlanTaskShouldBeFound("note.specified=true");

        // Get all the svcPlanTaskList where note is null
        defaultSvcPlanTaskShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByNoteContainsSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where note contains DEFAULT_NOTE
        defaultSvcPlanTaskShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the svcPlanTaskList where note contains UPDATED_NOTE
        defaultSvcPlanTaskShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        // Get all the svcPlanTaskList where note does not contain DEFAULT_NOTE
        defaultSvcPlanTaskShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the svcPlanTaskList where note does not contain UPDATED_NOTE
        defaultSvcPlanTaskShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanTasksBySvcPlanUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);
        SvcPlanUnit svcPlanUnit = SvcPlanUnitResourceIT.createEntity(em);
        em.persist(svcPlanUnit);
        em.flush();
        svcPlanTask.setSvcPlanUnit(svcPlanUnit);
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);
        Long svcPlanUnitId = svcPlanUnit.getId();

        // Get all the svcPlanTaskList where svcPlanUnit equals to svcPlanUnitId
        defaultSvcPlanTaskShouldBeFound("svcPlanUnitId.equals=" + svcPlanUnitId);

        // Get all the svcPlanTaskList where svcPlanUnit equals to (svcPlanUnitId + 1)
        defaultSvcPlanTaskShouldNotBeFound("svcPlanUnitId.equals=" + (svcPlanUnitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcPlanTaskShouldBeFound(String filter) throws Exception {
        restSvcPlanTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcPlanTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].coreTaskId").value(hasItem(DEFAULT_CORE_TASK_ID.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restSvcPlanTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcPlanTaskShouldNotBeFound(String filter) throws Exception {
        restSvcPlanTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcPlanTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcPlanTask() throws Exception {
        // Get the svcPlanTask
        restSvcPlanTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcPlanTask() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();

        // Update the svcPlanTask
        SvcPlanTask updatedSvcPlanTask = svcPlanTaskRepository.findById(svcPlanTask.getId()).get();
        // Disconnect from session so that the updates on updatedSvcPlanTask are not directly saved in db
        em.detach(updatedSvcPlanTask);
        updatedSvcPlanTask.coreTaskId(UPDATED_CORE_TASK_ID).note(UPDATED_NOTE);
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(updatedSvcPlanTask);

        restSvcPlanTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcPlanTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanTask testSvcPlanTask = svcPlanTaskList.get(svcPlanTaskList.size() - 1);
        assertThat(testSvcPlanTask.getCoreTaskId()).isEqualTo(UPDATED_CORE_TASK_ID);
        assertThat(testSvcPlanTask.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingSvcPlanTask() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();
        svcPlanTask.setId(count.incrementAndGet());

        // Create the SvcPlanTask
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcPlanTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcPlanTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcPlanTask() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();
        svcPlanTask.setId(count.incrementAndGet());

        // Create the SvcPlanTask
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcPlanTask() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();
        svcPlanTask.setId(count.incrementAndGet());

        // Create the SvcPlanTask
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcPlanTaskWithPatch() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();

        // Update the svcPlanTask using partial update
        SvcPlanTask partialUpdatedSvcPlanTask = new SvcPlanTask();
        partialUpdatedSvcPlanTask.setId(svcPlanTask.getId());

        partialUpdatedSvcPlanTask.note(UPDATED_NOTE);

        restSvcPlanTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcPlanTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcPlanTask))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanTask testSvcPlanTask = svcPlanTaskList.get(svcPlanTaskList.size() - 1);
        assertThat(testSvcPlanTask.getCoreTaskId()).isEqualTo(DEFAULT_CORE_TASK_ID);
        assertThat(testSvcPlanTask.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateSvcPlanTaskWithPatch() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();

        // Update the svcPlanTask using partial update
        SvcPlanTask partialUpdatedSvcPlanTask = new SvcPlanTask();
        partialUpdatedSvcPlanTask.setId(svcPlanTask.getId());

        partialUpdatedSvcPlanTask.coreTaskId(UPDATED_CORE_TASK_ID).note(UPDATED_NOTE);

        restSvcPlanTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcPlanTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcPlanTask))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanTask testSvcPlanTask = svcPlanTaskList.get(svcPlanTaskList.size() - 1);
        assertThat(testSvcPlanTask.getCoreTaskId()).isEqualTo(UPDATED_CORE_TASK_ID);
        assertThat(testSvcPlanTask.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingSvcPlanTask() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();
        svcPlanTask.setId(count.incrementAndGet());

        // Create the SvcPlanTask
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcPlanTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcPlanTaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcPlanTask() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();
        svcPlanTask.setId(count.incrementAndGet());

        // Create the SvcPlanTask
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcPlanTask() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanTaskRepository.findAll().size();
        svcPlanTask.setId(count.incrementAndGet());

        // Create the SvcPlanTask
        SvcPlanTaskDTO svcPlanTaskDTO = svcPlanTaskMapper.toDto(svcPlanTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanTaskMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcPlanTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcPlanTask in the database
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcPlanTask() throws Exception {
        // Initialize the database
        svcPlanTaskRepository.saveAndFlush(svcPlanTask);

        int databaseSizeBeforeDelete = svcPlanTaskRepository.findAll().size();

        // Delete the svcPlanTask
        restSvcPlanTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcPlanTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcPlanTask> svcPlanTaskList = svcPlanTaskRepository.findAll();
        assertThat(svcPlanTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
