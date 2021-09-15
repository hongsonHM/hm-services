package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.CoreSupplies;
import com.overnetcontact.dvvs.domain.CoreTask;
import com.overnetcontact.dvvs.repository.CoreTaskRepository;
import com.overnetcontact.dvvs.service.CoreTaskService;
import com.overnetcontact.dvvs.service.criteria.CoreTaskCriteria;
import com.overnetcontact.dvvs.service.dto.CoreTaskDTO;
import com.overnetcontact.dvvs.service.mapper.CoreTaskMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CoreTaskResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CoreTaskResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/core-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoreTaskRepository coreTaskRepository;

    @Mock
    private CoreTaskRepository coreTaskRepositoryMock;

    @Autowired
    private CoreTaskMapper coreTaskMapper;

    @Mock
    private CoreTaskService coreTaskServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoreTaskMockMvc;

    private CoreTask coreTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoreTask createEntity(EntityManager em) {
        CoreTask coreTask = new CoreTask().name(DEFAULT_NAME).unit(DEFAULT_UNIT).category(DEFAULT_CATEGORY).note(DEFAULT_NOTE);
        return coreTask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoreTask createUpdatedEntity(EntityManager em) {
        CoreTask coreTask = new CoreTask().name(UPDATED_NAME).unit(UPDATED_UNIT).category(UPDATED_CATEGORY).note(UPDATED_NOTE);
        return coreTask;
    }

    @BeforeEach
    public void initTest() {
        coreTask = createEntity(em);
    }

    @Test
    @Transactional
    void createCoreTask() throws Exception {
        int databaseSizeBeforeCreate = coreTaskRepository.findAll().size();
        // Create the CoreTask
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(coreTask);
        restCoreTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coreTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeCreate + 1);
        CoreTask testCoreTask = coreTaskList.get(coreTaskList.size() - 1);
        assertThat(testCoreTask.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCoreTask.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testCoreTask.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testCoreTask.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createCoreTaskWithExistingId() throws Exception {
        // Create the CoreTask with an existing ID
        coreTask.setId(1L);
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(coreTask);

        int databaseSizeBeforeCreate = coreTaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoreTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coreTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCoreTasks() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList
        restCoreTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coreTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoreTasksWithEagerRelationshipsIsEnabled() throws Exception {
        when(coreTaskServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCoreTaskMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(coreTaskServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoreTasksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(coreTaskServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCoreTaskMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(coreTaskServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCoreTask() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get the coreTask
        restCoreTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, coreTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coreTask.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getCoreTasksByIdFiltering() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        Long id = coreTask.getId();

        defaultCoreTaskShouldBeFound("id.equals=" + id);
        defaultCoreTaskShouldNotBeFound("id.notEquals=" + id);

        defaultCoreTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCoreTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultCoreTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCoreTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where name equals to DEFAULT_NAME
        defaultCoreTaskShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the coreTaskList where name equals to UPDATED_NAME
        defaultCoreTaskShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where name not equals to DEFAULT_NAME
        defaultCoreTaskShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the coreTaskList where name not equals to UPDATED_NAME
        defaultCoreTaskShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCoreTaskShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the coreTaskList where name equals to UPDATED_NAME
        defaultCoreTaskShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where name is not null
        defaultCoreTaskShouldBeFound("name.specified=true");

        // Get all the coreTaskList where name is null
        defaultCoreTaskShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCoreTasksByNameContainsSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where name contains DEFAULT_NAME
        defaultCoreTaskShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the coreTaskList where name contains UPDATED_NAME
        defaultCoreTaskShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where name does not contain DEFAULT_NAME
        defaultCoreTaskShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the coreTaskList where name does not contain UPDATED_NAME
        defaultCoreTaskShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoreTasksByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where unit equals to DEFAULT_UNIT
        defaultCoreTaskShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the coreTaskList where unit equals to UPDATED_UNIT
        defaultCoreTaskShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreTasksByUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where unit not equals to DEFAULT_UNIT
        defaultCoreTaskShouldNotBeFound("unit.notEquals=" + DEFAULT_UNIT);

        // Get all the coreTaskList where unit not equals to UPDATED_UNIT
        defaultCoreTaskShouldBeFound("unit.notEquals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreTasksByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultCoreTaskShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the coreTaskList where unit equals to UPDATED_UNIT
        defaultCoreTaskShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreTasksByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where unit is not null
        defaultCoreTaskShouldBeFound("unit.specified=true");

        // Get all the coreTaskList where unit is null
        defaultCoreTaskShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    void getAllCoreTasksByUnitContainsSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where unit contains DEFAULT_UNIT
        defaultCoreTaskShouldBeFound("unit.contains=" + DEFAULT_UNIT);

        // Get all the coreTaskList where unit contains UPDATED_UNIT
        defaultCoreTaskShouldNotBeFound("unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreTasksByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where unit does not contain DEFAULT_UNIT
        defaultCoreTaskShouldNotBeFound("unit.doesNotContain=" + DEFAULT_UNIT);

        // Get all the coreTaskList where unit does not contain UPDATED_UNIT
        defaultCoreTaskShouldBeFound("unit.doesNotContain=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllCoreTasksByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where category equals to DEFAULT_CATEGORY
        defaultCoreTaskShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the coreTaskList where category equals to UPDATED_CATEGORY
        defaultCoreTaskShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCoreTasksByCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where category not equals to DEFAULT_CATEGORY
        defaultCoreTaskShouldNotBeFound("category.notEquals=" + DEFAULT_CATEGORY);

        // Get all the coreTaskList where category not equals to UPDATED_CATEGORY
        defaultCoreTaskShouldBeFound("category.notEquals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCoreTasksByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultCoreTaskShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the coreTaskList where category equals to UPDATED_CATEGORY
        defaultCoreTaskShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCoreTasksByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where category is not null
        defaultCoreTaskShouldBeFound("category.specified=true");

        // Get all the coreTaskList where category is null
        defaultCoreTaskShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    void getAllCoreTasksByCategoryContainsSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where category contains DEFAULT_CATEGORY
        defaultCoreTaskShouldBeFound("category.contains=" + DEFAULT_CATEGORY);

        // Get all the coreTaskList where category contains UPDATED_CATEGORY
        defaultCoreTaskShouldNotBeFound("category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCoreTasksByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where category does not contain DEFAULT_CATEGORY
        defaultCoreTaskShouldNotBeFound("category.doesNotContain=" + DEFAULT_CATEGORY);

        // Get all the coreTaskList where category does not contain UPDATED_CATEGORY
        defaultCoreTaskShouldBeFound("category.doesNotContain=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where note equals to DEFAULT_NOTE
        defaultCoreTaskShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the coreTaskList where note equals to UPDATED_NOTE
        defaultCoreTaskShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where note not equals to DEFAULT_NOTE
        defaultCoreTaskShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the coreTaskList where note not equals to UPDATED_NOTE
        defaultCoreTaskShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultCoreTaskShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the coreTaskList where note equals to UPDATED_NOTE
        defaultCoreTaskShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where note is not null
        defaultCoreTaskShouldBeFound("note.specified=true");

        // Get all the coreTaskList where note is null
        defaultCoreTaskShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllCoreTasksByNoteContainsSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where note contains DEFAULT_NOTE
        defaultCoreTaskShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the coreTaskList where note contains UPDATED_NOTE
        defaultCoreTaskShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllCoreTasksByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        // Get all the coreTaskList where note does not contain DEFAULT_NOTE
        defaultCoreTaskShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the coreTaskList where note does not contain UPDATED_NOTE
        defaultCoreTaskShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllCoreTasksByCoreSuppliesIsEqualToSomething() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);
        CoreSupplies coreSupplies = CoreSuppliesResourceIT.createEntity(em);
        em.persist(coreSupplies);
        em.flush();
        coreTask.addCoreSupplies(coreSupplies);
        coreTaskRepository.saveAndFlush(coreTask);
        Long coreSuppliesId = coreSupplies.getId();

        // Get all the coreTaskList where coreSupplies equals to coreSuppliesId
        defaultCoreTaskShouldBeFound("coreSuppliesId.equals=" + coreSuppliesId);

        // Get all the coreTaskList where coreSupplies equals to (coreSuppliesId + 1)
        defaultCoreTaskShouldNotBeFound("coreSuppliesId.equals=" + (coreSuppliesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCoreTaskShouldBeFound(String filter) throws Exception {
        restCoreTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coreTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restCoreTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCoreTaskShouldNotBeFound(String filter) throws Exception {
        restCoreTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoreTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCoreTask() throws Exception {
        // Get the coreTask
        restCoreTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCoreTask() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();

        // Update the coreTask
        CoreTask updatedCoreTask = coreTaskRepository.findById(coreTask.getId()).get();
        // Disconnect from session so that the updates on updatedCoreTask are not directly saved in db
        em.detach(updatedCoreTask);
        updatedCoreTask.name(UPDATED_NAME).unit(UPDATED_UNIT).category(UPDATED_CATEGORY).note(UPDATED_NOTE);
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(updatedCoreTask);

        restCoreTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coreTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coreTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
        CoreTask testCoreTask = coreTaskList.get(coreTaskList.size() - 1);
        assertThat(testCoreTask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoreTask.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testCoreTask.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCoreTask.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingCoreTask() throws Exception {
        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();
        coreTask.setId(count.incrementAndGet());

        // Create the CoreTask
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(coreTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoreTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coreTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coreTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoreTask() throws Exception {
        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();
        coreTask.setId(count.incrementAndGet());

        // Create the CoreTask
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(coreTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoreTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coreTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoreTask() throws Exception {
        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();
        coreTask.setId(count.incrementAndGet());

        // Create the CoreTask
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(coreTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoreTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coreTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoreTaskWithPatch() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();

        // Update the coreTask using partial update
        CoreTask partialUpdatedCoreTask = new CoreTask();
        partialUpdatedCoreTask.setId(coreTask.getId());

        partialUpdatedCoreTask.name(UPDATED_NAME).unit(UPDATED_UNIT).category(UPDATED_CATEGORY);

        restCoreTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoreTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoreTask))
            )
            .andExpect(status().isOk());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
        CoreTask testCoreTask = coreTaskList.get(coreTaskList.size() - 1);
        assertThat(testCoreTask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoreTask.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testCoreTask.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCoreTask.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateCoreTaskWithPatch() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();

        // Update the coreTask using partial update
        CoreTask partialUpdatedCoreTask = new CoreTask();
        partialUpdatedCoreTask.setId(coreTask.getId());

        partialUpdatedCoreTask.name(UPDATED_NAME).unit(UPDATED_UNIT).category(UPDATED_CATEGORY).note(UPDATED_NOTE);

        restCoreTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoreTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoreTask))
            )
            .andExpect(status().isOk());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
        CoreTask testCoreTask = coreTaskList.get(coreTaskList.size() - 1);
        assertThat(testCoreTask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoreTask.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testCoreTask.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCoreTask.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingCoreTask() throws Exception {
        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();
        coreTask.setId(count.incrementAndGet());

        // Create the CoreTask
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(coreTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoreTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coreTaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coreTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoreTask() throws Exception {
        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();
        coreTask.setId(count.incrementAndGet());

        // Create the CoreTask
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(coreTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoreTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coreTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoreTask() throws Exception {
        int databaseSizeBeforeUpdate = coreTaskRepository.findAll().size();
        coreTask.setId(count.incrementAndGet());

        // Create the CoreTask
        CoreTaskDTO coreTaskDTO = coreTaskMapper.toDto(coreTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoreTaskMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coreTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoreTask in the database
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoreTask() throws Exception {
        // Initialize the database
        coreTaskRepository.saveAndFlush(coreTask);

        int databaseSizeBeforeDelete = coreTaskRepository.findAll().size();

        // Delete the coreTask
        restCoreTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, coreTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CoreTask> coreTaskList = coreTaskRepository.findAll();
        assertThat(coreTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
