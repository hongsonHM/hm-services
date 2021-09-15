package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcArea;
import com.overnetcontact.dvvs.domain.SvcGroupTask;
import com.overnetcontact.dvvs.domain.SvcSpendTask;
import com.overnetcontact.dvvs.repository.SvcGroupTaskRepository;
import com.overnetcontact.dvvs.service.criteria.SvcGroupTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcGroupTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcGroupTaskMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SvcGroupTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcGroupTaskResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-group-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcGroupTaskRepository svcGroupTaskRepository;

    @Autowired
    private SvcGroupTaskMapper svcGroupTaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcGroupTaskMockMvc;

    private SvcGroupTask svcGroupTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcGroupTask createEntity(EntityManager em) {
        SvcGroupTask svcGroupTask = new SvcGroupTask()
            .name(DEFAULT_NAME)
            .createDate(DEFAULT_CREATE_DATE)
            .createBy(DEFAULT_CREATE_BY)
            .category(DEFAULT_CATEGORY);
        return svcGroupTask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcGroupTask createUpdatedEntity(EntityManager em) {
        SvcGroupTask svcGroupTask = new SvcGroupTask()
            .name(UPDATED_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .category(UPDATED_CATEGORY);
        return svcGroupTask;
    }

    @BeforeEach
    public void initTest() {
        svcGroupTask = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcGroupTask() throws Exception {
        int databaseSizeBeforeCreate = svcGroupTaskRepository.findAll().size();
        // Create the SvcGroupTask
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(svcGroupTask);
        restSvcGroupTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeCreate + 1);
        SvcGroupTask testSvcGroupTask = svcGroupTaskList.get(svcGroupTaskList.size() - 1);
        assertThat(testSvcGroupTask.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcGroupTask.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSvcGroupTask.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testSvcGroupTask.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void createSvcGroupTaskWithExistingId() throws Exception {
        // Create the SvcGroupTask with an existing ID
        svcGroupTask.setId(1L);
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(svcGroupTask);

        int databaseSizeBeforeCreate = svcGroupTaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcGroupTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasks() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList
        restSvcGroupTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcGroupTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)));
    }

    @Test
    @Transactional
    void getSvcGroupTask() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get the svcGroupTask
        restSvcGroupTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, svcGroupTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcGroupTask.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY));
    }

    @Test
    @Transactional
    void getSvcGroupTasksByIdFiltering() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        Long id = svcGroupTask.getId();

        defaultSvcGroupTaskShouldBeFound("id.equals=" + id);
        defaultSvcGroupTaskShouldNotBeFound("id.notEquals=" + id);

        defaultSvcGroupTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcGroupTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcGroupTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcGroupTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where name equals to DEFAULT_NAME
        defaultSvcGroupTaskShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the svcGroupTaskList where name equals to UPDATED_NAME
        defaultSvcGroupTaskShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where name not equals to DEFAULT_NAME
        defaultSvcGroupTaskShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the svcGroupTaskList where name not equals to UPDATED_NAME
        defaultSvcGroupTaskShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSvcGroupTaskShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the svcGroupTaskList where name equals to UPDATED_NAME
        defaultSvcGroupTaskShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where name is not null
        defaultSvcGroupTaskShouldBeFound("name.specified=true");

        // Get all the svcGroupTaskList where name is null
        defaultSvcGroupTaskShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByNameContainsSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where name contains DEFAULT_NAME
        defaultSvcGroupTaskShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the svcGroupTaskList where name contains UPDATED_NAME
        defaultSvcGroupTaskShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where name does not contain DEFAULT_NAME
        defaultSvcGroupTaskShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the svcGroupTaskList where name does not contain UPDATED_NAME
        defaultSvcGroupTaskShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createDate equals to DEFAULT_CREATE_DATE
        defaultSvcGroupTaskShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the svcGroupTaskList where createDate equals to UPDATED_CREATE_DATE
        defaultSvcGroupTaskShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSvcGroupTaskShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the svcGroupTaskList where createDate not equals to UPDATED_CREATE_DATE
        defaultSvcGroupTaskShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSvcGroupTaskShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the svcGroupTaskList where createDate equals to UPDATED_CREATE_DATE
        defaultSvcGroupTaskShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createDate is not null
        defaultSvcGroupTaskShouldBeFound("createDate.specified=true");

        // Get all the svcGroupTaskList where createDate is null
        defaultSvcGroupTaskShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSvcGroupTaskShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the svcGroupTaskList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSvcGroupTaskShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSvcGroupTaskShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the svcGroupTaskList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSvcGroupTaskShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createDate is less than DEFAULT_CREATE_DATE
        defaultSvcGroupTaskShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the svcGroupTaskList where createDate is less than UPDATED_CREATE_DATE
        defaultSvcGroupTaskShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSvcGroupTaskShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the svcGroupTaskList where createDate is greater than SMALLER_CREATE_DATE
        defaultSvcGroupTaskShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createBy equals to DEFAULT_CREATE_BY
        defaultSvcGroupTaskShouldBeFound("createBy.equals=" + DEFAULT_CREATE_BY);

        // Get all the svcGroupTaskList where createBy equals to UPDATED_CREATE_BY
        defaultSvcGroupTaskShouldNotBeFound("createBy.equals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createBy not equals to DEFAULT_CREATE_BY
        defaultSvcGroupTaskShouldNotBeFound("createBy.notEquals=" + DEFAULT_CREATE_BY);

        // Get all the svcGroupTaskList where createBy not equals to UPDATED_CREATE_BY
        defaultSvcGroupTaskShouldBeFound("createBy.notEquals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateByIsInShouldWork() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createBy in DEFAULT_CREATE_BY or UPDATED_CREATE_BY
        defaultSvcGroupTaskShouldBeFound("createBy.in=" + DEFAULT_CREATE_BY + "," + UPDATED_CREATE_BY);

        // Get all the svcGroupTaskList where createBy equals to UPDATED_CREATE_BY
        defaultSvcGroupTaskShouldNotBeFound("createBy.in=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateByIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createBy is not null
        defaultSvcGroupTaskShouldBeFound("createBy.specified=true");

        // Get all the svcGroupTaskList where createBy is null
        defaultSvcGroupTaskShouldNotBeFound("createBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateByContainsSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createBy contains DEFAULT_CREATE_BY
        defaultSvcGroupTaskShouldBeFound("createBy.contains=" + DEFAULT_CREATE_BY);

        // Get all the svcGroupTaskList where createBy contains UPDATED_CREATE_BY
        defaultSvcGroupTaskShouldNotBeFound("createBy.contains=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCreateByNotContainsSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where createBy does not contain DEFAULT_CREATE_BY
        defaultSvcGroupTaskShouldNotBeFound("createBy.doesNotContain=" + DEFAULT_CREATE_BY);

        // Get all the svcGroupTaskList where createBy does not contain UPDATED_CREATE_BY
        defaultSvcGroupTaskShouldBeFound("createBy.doesNotContain=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where category equals to DEFAULT_CATEGORY
        defaultSvcGroupTaskShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the svcGroupTaskList where category equals to UPDATED_CATEGORY
        defaultSvcGroupTaskShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where category not equals to DEFAULT_CATEGORY
        defaultSvcGroupTaskShouldNotBeFound("category.notEquals=" + DEFAULT_CATEGORY);

        // Get all the svcGroupTaskList where category not equals to UPDATED_CATEGORY
        defaultSvcGroupTaskShouldBeFound("category.notEquals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultSvcGroupTaskShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the svcGroupTaskList where category equals to UPDATED_CATEGORY
        defaultSvcGroupTaskShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where category is not null
        defaultSvcGroupTaskShouldBeFound("category.specified=true");

        // Get all the svcGroupTaskList where category is null
        defaultSvcGroupTaskShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCategoryContainsSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where category contains DEFAULT_CATEGORY
        defaultSvcGroupTaskShouldBeFound("category.contains=" + DEFAULT_CATEGORY);

        // Get all the svcGroupTaskList where category contains UPDATED_CATEGORY
        defaultSvcGroupTaskShouldNotBeFound("category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        // Get all the svcGroupTaskList where category does not contain DEFAULT_CATEGORY
        defaultSvcGroupTaskShouldNotBeFound("category.doesNotContain=" + DEFAULT_CATEGORY);

        // Get all the svcGroupTaskList where category does not contain UPDATED_CATEGORY
        defaultSvcGroupTaskShouldBeFound("category.doesNotContain=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksBySvcAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);
        SvcArea svcArea = SvcAreaResourceIT.createEntity(em);
        em.persist(svcArea);
        em.flush();
        svcGroupTask.setSvcArea(svcArea);
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);
        Long svcAreaId = svcArea.getId();

        // Get all the svcGroupTaskList where svcArea equals to svcAreaId
        defaultSvcGroupTaskShouldBeFound("svcAreaId.equals=" + svcAreaId);

        // Get all the svcGroupTaskList where svcArea equals to (svcAreaId + 1)
        defaultSvcGroupTaskShouldNotBeFound("svcAreaId.equals=" + (svcAreaId + 1));
    }

    @Test
    @Transactional
    void getAllSvcGroupTasksBySvcSpendTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);
        SvcSpendTask svcSpendTask = SvcSpendTaskResourceIT.createEntity(em);
        em.persist(svcSpendTask);
        em.flush();
        svcGroupTask.addSvcSpendTask(svcSpendTask);
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);
        Long svcSpendTaskId = svcSpendTask.getId();

        // Get all the svcGroupTaskList where svcSpendTask equals to svcSpendTaskId
        defaultSvcGroupTaskShouldBeFound("svcSpendTaskId.equals=" + svcSpendTaskId);

        // Get all the svcGroupTaskList where svcSpendTask equals to (svcSpendTaskId + 1)
        defaultSvcGroupTaskShouldNotBeFound("svcSpendTaskId.equals=" + (svcSpendTaskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcGroupTaskShouldBeFound(String filter) throws Exception {
        restSvcGroupTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcGroupTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)));

        // Check, that the count call also returns 1
        restSvcGroupTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcGroupTaskShouldNotBeFound(String filter) throws Exception {
        restSvcGroupTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcGroupTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcGroupTask() throws Exception {
        // Get the svcGroupTask
        restSvcGroupTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcGroupTask() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();

        // Update the svcGroupTask
        SvcGroupTask updatedSvcGroupTask = svcGroupTaskRepository.findById(svcGroupTask.getId()).get();
        // Disconnect from session so that the updates on updatedSvcGroupTask are not directly saved in db
        em.detach(updatedSvcGroupTask);
        updatedSvcGroupTask.name(UPDATED_NAME).createDate(UPDATED_CREATE_DATE).createBy(UPDATED_CREATE_BY).category(UPDATED_CATEGORY);
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(updatedSvcGroupTask);

        restSvcGroupTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcGroupTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcGroupTask testSvcGroupTask = svcGroupTaskList.get(svcGroupTaskList.size() - 1);
        assertThat(testSvcGroupTask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcGroupTask.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcGroupTask.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcGroupTask.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void putNonExistingSvcGroupTask() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();
        svcGroupTask.setId(count.incrementAndGet());

        // Create the SvcGroupTask
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(svcGroupTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcGroupTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcGroupTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcGroupTask() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();
        svcGroupTask.setId(count.incrementAndGet());

        // Create the SvcGroupTask
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(svcGroupTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcGroupTask() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();
        svcGroupTask.setId(count.incrementAndGet());

        // Create the SvcGroupTask
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(svcGroupTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupTaskMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcGroupTaskWithPatch() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();

        // Update the svcGroupTask using partial update
        SvcGroupTask partialUpdatedSvcGroupTask = new SvcGroupTask();
        partialUpdatedSvcGroupTask.setId(svcGroupTask.getId());

        partialUpdatedSvcGroupTask.createDate(UPDATED_CREATE_DATE).createBy(UPDATED_CREATE_BY).category(UPDATED_CATEGORY);

        restSvcGroupTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcGroupTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcGroupTask))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcGroupTask testSvcGroupTask = svcGroupTaskList.get(svcGroupTaskList.size() - 1);
        assertThat(testSvcGroupTask.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcGroupTask.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcGroupTask.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcGroupTask.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void fullUpdateSvcGroupTaskWithPatch() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();

        // Update the svcGroupTask using partial update
        SvcGroupTask partialUpdatedSvcGroupTask = new SvcGroupTask();
        partialUpdatedSvcGroupTask.setId(svcGroupTask.getId());

        partialUpdatedSvcGroupTask
            .name(UPDATED_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .createBy(UPDATED_CREATE_BY)
            .category(UPDATED_CATEGORY);

        restSvcGroupTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcGroupTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcGroupTask))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
        SvcGroupTask testSvcGroupTask = svcGroupTaskList.get(svcGroupTaskList.size() - 1);
        assertThat(testSvcGroupTask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcGroupTask.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcGroupTask.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcGroupTask.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void patchNonExistingSvcGroupTask() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();
        svcGroupTask.setId(count.incrementAndGet());

        // Create the SvcGroupTask
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(svcGroupTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcGroupTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcGroupTaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcGroupTask() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();
        svcGroupTask.setId(count.incrementAndGet());

        // Create the SvcGroupTask
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(svcGroupTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcGroupTask() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupTaskRepository.findAll().size();
        svcGroupTask.setId(count.incrementAndGet());

        // Create the SvcGroupTask
        SvcGroupTaskDTO svcGroupTaskDTO = svcGroupTaskMapper.toDto(svcGroupTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupTaskMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcGroupTask in the database
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcGroupTask() throws Exception {
        // Initialize the database
        svcGroupTaskRepository.saveAndFlush(svcGroupTask);

        int databaseSizeBeforeDelete = svcGroupTaskRepository.findAll().size();

        // Delete the svcGroupTask
        restSvcGroupTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcGroupTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcGroupTask> svcGroupTaskList = svcGroupTaskRepository.findAll();
        assertThat(svcGroupTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
