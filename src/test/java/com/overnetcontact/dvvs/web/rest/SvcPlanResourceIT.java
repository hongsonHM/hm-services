package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcPlan;
import com.overnetcontact.dvvs.domain.SvcPlanUnit;
import com.overnetcontact.dvvs.repository.SvcPlanRepository;
import com.overnetcontact.dvvs.service.criteria.SvcPlanCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanMapper;
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
 * Integration tests for the {@link SvcPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcPlanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SERVICE_MANAGER_ID = 1L;
    private static final Long UPDATED_SERVICE_MANAGER_ID = 2L;
    private static final Long SMALLER_SERVICE_MANAGER_ID = 1L - 1L;

    private static final Long DEFAULT_DEFAULT_SUPPERVISOR_ID = 1L;
    private static final Long UPDATED_DEFAULT_SUPPERVISOR_ID = 2L;
    private static final Long SMALLER_DEFAULT_SUPPERVISOR_ID = 1L - 1L;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final LocalDate DEFAULT_START_PLAN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_PLAN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_PLAN = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_PLAN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_PLAN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_PLAN = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_CONTRACT_ID = 1L;
    private static final Long UPDATED_CONTRACT_ID = 2L;
    private static final Long SMALLER_CONTRACT_ID = 1L - 1L;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcPlanRepository svcPlanRepository;

    @Autowired
    private SvcPlanMapper svcPlanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcPlanMockMvc;

    private SvcPlan svcPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcPlan createEntity(EntityManager em) {
        SvcPlan svcPlan = new SvcPlan()
            .name(DEFAULT_NAME)
            .serviceManagerId(DEFAULT_SERVICE_MANAGER_ID)
            .defaultSuppervisorId(DEFAULT_DEFAULT_SUPPERVISOR_ID)
            .status(DEFAULT_STATUS)
            .startPlan(DEFAULT_START_PLAN)
            .endPlan(DEFAULT_END_PLAN)
            .createDate(DEFAULT_CREATE_DATE)
            .contractId(DEFAULT_CONTRACT_ID)
            .note(DEFAULT_NOTE);
        return svcPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcPlan createUpdatedEntity(EntityManager em) {
        SvcPlan svcPlan = new SvcPlan()
            .name(UPDATED_NAME)
            .serviceManagerId(UPDATED_SERVICE_MANAGER_ID)
            .defaultSuppervisorId(UPDATED_DEFAULT_SUPPERVISOR_ID)
            .status(UPDATED_STATUS)
            .startPlan(UPDATED_START_PLAN)
            .endPlan(UPDATED_END_PLAN)
            .createDate(UPDATED_CREATE_DATE)
            .contractId(UPDATED_CONTRACT_ID)
            .note(UPDATED_NOTE);
        return svcPlan;
    }

    @BeforeEach
    public void initTest() {
        svcPlan = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcPlan() throws Exception {
        int databaseSizeBeforeCreate = svcPlanRepository.findAll().size();
        // Create the SvcPlan
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);
        restSvcPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanDTO)))
            .andExpect(status().isCreated());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeCreate + 1);
        SvcPlan testSvcPlan = svcPlanList.get(svcPlanList.size() - 1);
        assertThat(testSvcPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcPlan.getServiceManagerId()).isEqualTo(DEFAULT_SERVICE_MANAGER_ID);
        assertThat(testSvcPlan.getDefaultSuppervisorId()).isEqualTo(DEFAULT_DEFAULT_SUPPERVISOR_ID);
        assertThat(testSvcPlan.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSvcPlan.getStartPlan()).isEqualTo(DEFAULT_START_PLAN);
        assertThat(testSvcPlan.getEndPlan()).isEqualTo(DEFAULT_END_PLAN);
        assertThat(testSvcPlan.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSvcPlan.getContractId()).isEqualTo(DEFAULT_CONTRACT_ID);
        assertThat(testSvcPlan.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createSvcPlanWithExistingId() throws Exception {
        // Create the SvcPlan with an existing ID
        svcPlan.setId(1L);
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        int databaseSizeBeforeCreate = svcPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcPlanRepository.findAll().size();
        // set the field null
        svcPlan.setName(null);

        // Create the SvcPlan, which fails.
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        restSvcPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanDTO)))
            .andExpect(status().isBadRequest());

        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceManagerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcPlanRepository.findAll().size();
        // set the field null
        svcPlan.setServiceManagerId(null);

        // Create the SvcPlan, which fails.
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        restSvcPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanDTO)))
            .andExpect(status().isBadRequest());

        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefaultSuppervisorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcPlanRepository.findAll().size();
        // set the field null
        svcPlan.setDefaultSuppervisorId(null);

        // Create the SvcPlan, which fails.
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        restSvcPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanDTO)))
            .andExpect(status().isBadRequest());

        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcPlans() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList
        restSvcPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].serviceManagerId").value(hasItem(DEFAULT_SERVICE_MANAGER_ID.intValue())))
            .andExpect(jsonPath("$.[*].defaultSuppervisorId").value(hasItem(DEFAULT_DEFAULT_SUPPERVISOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].startPlan").value(hasItem(DEFAULT_START_PLAN.toString())))
            .andExpect(jsonPath("$.[*].endPlan").value(hasItem(DEFAULT_END_PLAN.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractId").value(hasItem(DEFAULT_CONTRACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getSvcPlan() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get the svcPlan
        restSvcPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, svcPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcPlan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.serviceManagerId").value(DEFAULT_SERVICE_MANAGER_ID.intValue()))
            .andExpect(jsonPath("$.defaultSuppervisorId").value(DEFAULT_DEFAULT_SUPPERVISOR_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.startPlan").value(DEFAULT_START_PLAN.toString()))
            .andExpect(jsonPath("$.endPlan").value(DEFAULT_END_PLAN.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.contractId").value(DEFAULT_CONTRACT_ID.intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getSvcPlansByIdFiltering() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        Long id = svcPlan.getId();

        defaultSvcPlanShouldBeFound("id.equals=" + id);
        defaultSvcPlanShouldNotBeFound("id.notEquals=" + id);

        defaultSvcPlanShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcPlanShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcPlanShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcPlanShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where name equals to DEFAULT_NAME
        defaultSvcPlanShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the svcPlanList where name equals to UPDATED_NAME
        defaultSvcPlanShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where name not equals to DEFAULT_NAME
        defaultSvcPlanShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the svcPlanList where name not equals to UPDATED_NAME
        defaultSvcPlanShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSvcPlanShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the svcPlanList where name equals to UPDATED_NAME
        defaultSvcPlanShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where name is not null
        defaultSvcPlanShouldBeFound("name.specified=true");

        // Get all the svcPlanList where name is null
        defaultSvcPlanShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByNameContainsSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where name contains DEFAULT_NAME
        defaultSvcPlanShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the svcPlanList where name contains UPDATED_NAME
        defaultSvcPlanShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where name does not contain DEFAULT_NAME
        defaultSvcPlanShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the svcPlanList where name does not contain UPDATED_NAME
        defaultSvcPlanShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcPlansByServiceManagerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where serviceManagerId equals to DEFAULT_SERVICE_MANAGER_ID
        defaultSvcPlanShouldBeFound("serviceManagerId.equals=" + DEFAULT_SERVICE_MANAGER_ID);

        // Get all the svcPlanList where serviceManagerId equals to UPDATED_SERVICE_MANAGER_ID
        defaultSvcPlanShouldNotBeFound("serviceManagerId.equals=" + UPDATED_SERVICE_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByServiceManagerIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where serviceManagerId not equals to DEFAULT_SERVICE_MANAGER_ID
        defaultSvcPlanShouldNotBeFound("serviceManagerId.notEquals=" + DEFAULT_SERVICE_MANAGER_ID);

        // Get all the svcPlanList where serviceManagerId not equals to UPDATED_SERVICE_MANAGER_ID
        defaultSvcPlanShouldBeFound("serviceManagerId.notEquals=" + UPDATED_SERVICE_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByServiceManagerIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where serviceManagerId in DEFAULT_SERVICE_MANAGER_ID or UPDATED_SERVICE_MANAGER_ID
        defaultSvcPlanShouldBeFound("serviceManagerId.in=" + DEFAULT_SERVICE_MANAGER_ID + "," + UPDATED_SERVICE_MANAGER_ID);

        // Get all the svcPlanList where serviceManagerId equals to UPDATED_SERVICE_MANAGER_ID
        defaultSvcPlanShouldNotBeFound("serviceManagerId.in=" + UPDATED_SERVICE_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByServiceManagerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where serviceManagerId is not null
        defaultSvcPlanShouldBeFound("serviceManagerId.specified=true");

        // Get all the svcPlanList where serviceManagerId is null
        defaultSvcPlanShouldNotBeFound("serviceManagerId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByServiceManagerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where serviceManagerId is greater than or equal to DEFAULT_SERVICE_MANAGER_ID
        defaultSvcPlanShouldBeFound("serviceManagerId.greaterThanOrEqual=" + DEFAULT_SERVICE_MANAGER_ID);

        // Get all the svcPlanList where serviceManagerId is greater than or equal to UPDATED_SERVICE_MANAGER_ID
        defaultSvcPlanShouldNotBeFound("serviceManagerId.greaterThanOrEqual=" + UPDATED_SERVICE_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByServiceManagerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where serviceManagerId is less than or equal to DEFAULT_SERVICE_MANAGER_ID
        defaultSvcPlanShouldBeFound("serviceManagerId.lessThanOrEqual=" + DEFAULT_SERVICE_MANAGER_ID);

        // Get all the svcPlanList where serviceManagerId is less than or equal to SMALLER_SERVICE_MANAGER_ID
        defaultSvcPlanShouldNotBeFound("serviceManagerId.lessThanOrEqual=" + SMALLER_SERVICE_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByServiceManagerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where serviceManagerId is less than DEFAULT_SERVICE_MANAGER_ID
        defaultSvcPlanShouldNotBeFound("serviceManagerId.lessThan=" + DEFAULT_SERVICE_MANAGER_ID);

        // Get all the svcPlanList where serviceManagerId is less than UPDATED_SERVICE_MANAGER_ID
        defaultSvcPlanShouldBeFound("serviceManagerId.lessThan=" + UPDATED_SERVICE_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByServiceManagerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where serviceManagerId is greater than DEFAULT_SERVICE_MANAGER_ID
        defaultSvcPlanShouldNotBeFound("serviceManagerId.greaterThan=" + DEFAULT_SERVICE_MANAGER_ID);

        // Get all the svcPlanList where serviceManagerId is greater than SMALLER_SERVICE_MANAGER_ID
        defaultSvcPlanShouldBeFound("serviceManagerId.greaterThan=" + SMALLER_SERVICE_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByDefaultSuppervisorIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where defaultSuppervisorId equals to DEFAULT_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldBeFound("defaultSuppervisorId.equals=" + DEFAULT_DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanList where defaultSuppervisorId equals to UPDATED_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldNotBeFound("defaultSuppervisorId.equals=" + UPDATED_DEFAULT_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByDefaultSuppervisorIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where defaultSuppervisorId not equals to DEFAULT_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldNotBeFound("defaultSuppervisorId.notEquals=" + DEFAULT_DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanList where defaultSuppervisorId not equals to UPDATED_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldBeFound("defaultSuppervisorId.notEquals=" + UPDATED_DEFAULT_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByDefaultSuppervisorIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where defaultSuppervisorId in DEFAULT_DEFAULT_SUPPERVISOR_ID or UPDATED_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldBeFound("defaultSuppervisorId.in=" + DEFAULT_DEFAULT_SUPPERVISOR_ID + "," + UPDATED_DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanList where defaultSuppervisorId equals to UPDATED_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldNotBeFound("defaultSuppervisorId.in=" + UPDATED_DEFAULT_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByDefaultSuppervisorIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where defaultSuppervisorId is not null
        defaultSvcPlanShouldBeFound("defaultSuppervisorId.specified=true");

        // Get all the svcPlanList where defaultSuppervisorId is null
        defaultSvcPlanShouldNotBeFound("defaultSuppervisorId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByDefaultSuppervisorIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where defaultSuppervisorId is greater than or equal to DEFAULT_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldBeFound("defaultSuppervisorId.greaterThanOrEqual=" + DEFAULT_DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanList where defaultSuppervisorId is greater than or equal to UPDATED_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldNotBeFound("defaultSuppervisorId.greaterThanOrEqual=" + UPDATED_DEFAULT_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByDefaultSuppervisorIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where defaultSuppervisorId is less than or equal to DEFAULT_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldBeFound("defaultSuppervisorId.lessThanOrEqual=" + DEFAULT_DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanList where defaultSuppervisorId is less than or equal to SMALLER_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldNotBeFound("defaultSuppervisorId.lessThanOrEqual=" + SMALLER_DEFAULT_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByDefaultSuppervisorIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where defaultSuppervisorId is less than DEFAULT_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldNotBeFound("defaultSuppervisorId.lessThan=" + DEFAULT_DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanList where defaultSuppervisorId is less than UPDATED_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldBeFound("defaultSuppervisorId.lessThan=" + UPDATED_DEFAULT_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByDefaultSuppervisorIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where defaultSuppervisorId is greater than DEFAULT_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldNotBeFound("defaultSuppervisorId.greaterThan=" + DEFAULT_DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanList where defaultSuppervisorId is greater than SMALLER_DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanShouldBeFound("defaultSuppervisorId.greaterThan=" + SMALLER_DEFAULT_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where status equals to DEFAULT_STATUS
        defaultSvcPlanShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the svcPlanList where status equals to UPDATED_STATUS
        defaultSvcPlanShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where status not equals to DEFAULT_STATUS
        defaultSvcPlanShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the svcPlanList where status not equals to UPDATED_STATUS
        defaultSvcPlanShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSvcPlanShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the svcPlanList where status equals to UPDATED_STATUS
        defaultSvcPlanShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where status is not null
        defaultSvcPlanShouldBeFound("status.specified=true");

        // Get all the svcPlanList where status is null
        defaultSvcPlanShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByStartPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where startPlan equals to DEFAULT_START_PLAN
        defaultSvcPlanShouldBeFound("startPlan.equals=" + DEFAULT_START_PLAN);

        // Get all the svcPlanList where startPlan equals to UPDATED_START_PLAN
        defaultSvcPlanShouldNotBeFound("startPlan.equals=" + UPDATED_START_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStartPlanIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where startPlan not equals to DEFAULT_START_PLAN
        defaultSvcPlanShouldNotBeFound("startPlan.notEquals=" + DEFAULT_START_PLAN);

        // Get all the svcPlanList where startPlan not equals to UPDATED_START_PLAN
        defaultSvcPlanShouldBeFound("startPlan.notEquals=" + UPDATED_START_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStartPlanIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where startPlan in DEFAULT_START_PLAN or UPDATED_START_PLAN
        defaultSvcPlanShouldBeFound("startPlan.in=" + DEFAULT_START_PLAN + "," + UPDATED_START_PLAN);

        // Get all the svcPlanList where startPlan equals to UPDATED_START_PLAN
        defaultSvcPlanShouldNotBeFound("startPlan.in=" + UPDATED_START_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStartPlanIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where startPlan is not null
        defaultSvcPlanShouldBeFound("startPlan.specified=true");

        // Get all the svcPlanList where startPlan is null
        defaultSvcPlanShouldNotBeFound("startPlan.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByStartPlanIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where startPlan is greater than or equal to DEFAULT_START_PLAN
        defaultSvcPlanShouldBeFound("startPlan.greaterThanOrEqual=" + DEFAULT_START_PLAN);

        // Get all the svcPlanList where startPlan is greater than or equal to UPDATED_START_PLAN
        defaultSvcPlanShouldNotBeFound("startPlan.greaterThanOrEqual=" + UPDATED_START_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStartPlanIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where startPlan is less than or equal to DEFAULT_START_PLAN
        defaultSvcPlanShouldBeFound("startPlan.lessThanOrEqual=" + DEFAULT_START_PLAN);

        // Get all the svcPlanList where startPlan is less than or equal to SMALLER_START_PLAN
        defaultSvcPlanShouldNotBeFound("startPlan.lessThanOrEqual=" + SMALLER_START_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStartPlanIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where startPlan is less than DEFAULT_START_PLAN
        defaultSvcPlanShouldNotBeFound("startPlan.lessThan=" + DEFAULT_START_PLAN);

        // Get all the svcPlanList where startPlan is less than UPDATED_START_PLAN
        defaultSvcPlanShouldBeFound("startPlan.lessThan=" + UPDATED_START_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByStartPlanIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where startPlan is greater than DEFAULT_START_PLAN
        defaultSvcPlanShouldNotBeFound("startPlan.greaterThan=" + DEFAULT_START_PLAN);

        // Get all the svcPlanList where startPlan is greater than SMALLER_START_PLAN
        defaultSvcPlanShouldBeFound("startPlan.greaterThan=" + SMALLER_START_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByEndPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where endPlan equals to DEFAULT_END_PLAN
        defaultSvcPlanShouldBeFound("endPlan.equals=" + DEFAULT_END_PLAN);

        // Get all the svcPlanList where endPlan equals to UPDATED_END_PLAN
        defaultSvcPlanShouldNotBeFound("endPlan.equals=" + UPDATED_END_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByEndPlanIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where endPlan not equals to DEFAULT_END_PLAN
        defaultSvcPlanShouldNotBeFound("endPlan.notEquals=" + DEFAULT_END_PLAN);

        // Get all the svcPlanList where endPlan not equals to UPDATED_END_PLAN
        defaultSvcPlanShouldBeFound("endPlan.notEquals=" + UPDATED_END_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByEndPlanIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where endPlan in DEFAULT_END_PLAN or UPDATED_END_PLAN
        defaultSvcPlanShouldBeFound("endPlan.in=" + DEFAULT_END_PLAN + "," + UPDATED_END_PLAN);

        // Get all the svcPlanList where endPlan equals to UPDATED_END_PLAN
        defaultSvcPlanShouldNotBeFound("endPlan.in=" + UPDATED_END_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByEndPlanIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where endPlan is not null
        defaultSvcPlanShouldBeFound("endPlan.specified=true");

        // Get all the svcPlanList where endPlan is null
        defaultSvcPlanShouldNotBeFound("endPlan.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByEndPlanIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where endPlan is greater than or equal to DEFAULT_END_PLAN
        defaultSvcPlanShouldBeFound("endPlan.greaterThanOrEqual=" + DEFAULT_END_PLAN);

        // Get all the svcPlanList where endPlan is greater than or equal to UPDATED_END_PLAN
        defaultSvcPlanShouldNotBeFound("endPlan.greaterThanOrEqual=" + UPDATED_END_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByEndPlanIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where endPlan is less than or equal to DEFAULT_END_PLAN
        defaultSvcPlanShouldBeFound("endPlan.lessThanOrEqual=" + DEFAULT_END_PLAN);

        // Get all the svcPlanList where endPlan is less than or equal to SMALLER_END_PLAN
        defaultSvcPlanShouldNotBeFound("endPlan.lessThanOrEqual=" + SMALLER_END_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByEndPlanIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where endPlan is less than DEFAULT_END_PLAN
        defaultSvcPlanShouldNotBeFound("endPlan.lessThan=" + DEFAULT_END_PLAN);

        // Get all the svcPlanList where endPlan is less than UPDATED_END_PLAN
        defaultSvcPlanShouldBeFound("endPlan.lessThan=" + UPDATED_END_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByEndPlanIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where endPlan is greater than DEFAULT_END_PLAN
        defaultSvcPlanShouldNotBeFound("endPlan.greaterThan=" + DEFAULT_END_PLAN);

        // Get all the svcPlanList where endPlan is greater than SMALLER_END_PLAN
        defaultSvcPlanShouldBeFound("endPlan.greaterThan=" + SMALLER_END_PLAN);
    }

    @Test
    @Transactional
    void getAllSvcPlansByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where createDate equals to DEFAULT_CREATE_DATE
        defaultSvcPlanShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the svcPlanList where createDate equals to UPDATED_CREATE_DATE
        defaultSvcPlanShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSvcPlanShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the svcPlanList where createDate not equals to UPDATED_CREATE_DATE
        defaultSvcPlanShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSvcPlanShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the svcPlanList where createDate equals to UPDATED_CREATE_DATE
        defaultSvcPlanShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where createDate is not null
        defaultSvcPlanShouldBeFound("createDate.specified=true");

        // Get all the svcPlanList where createDate is null
        defaultSvcPlanShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSvcPlanShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the svcPlanList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSvcPlanShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSvcPlanShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the svcPlanList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSvcPlanShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where createDate is less than DEFAULT_CREATE_DATE
        defaultSvcPlanShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the svcPlanList where createDate is less than UPDATED_CREATE_DATE
        defaultSvcPlanShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSvcPlanShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the svcPlanList where createDate is greater than SMALLER_CREATE_DATE
        defaultSvcPlanShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByContractIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where contractId equals to DEFAULT_CONTRACT_ID
        defaultSvcPlanShouldBeFound("contractId.equals=" + DEFAULT_CONTRACT_ID);

        // Get all the svcPlanList where contractId equals to UPDATED_CONTRACT_ID
        defaultSvcPlanShouldNotBeFound("contractId.equals=" + UPDATED_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByContractIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where contractId not equals to DEFAULT_CONTRACT_ID
        defaultSvcPlanShouldNotBeFound("contractId.notEquals=" + DEFAULT_CONTRACT_ID);

        // Get all the svcPlanList where contractId not equals to UPDATED_CONTRACT_ID
        defaultSvcPlanShouldBeFound("contractId.notEquals=" + UPDATED_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByContractIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where contractId in DEFAULT_CONTRACT_ID or UPDATED_CONTRACT_ID
        defaultSvcPlanShouldBeFound("contractId.in=" + DEFAULT_CONTRACT_ID + "," + UPDATED_CONTRACT_ID);

        // Get all the svcPlanList where contractId equals to UPDATED_CONTRACT_ID
        defaultSvcPlanShouldNotBeFound("contractId.in=" + UPDATED_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByContractIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where contractId is not null
        defaultSvcPlanShouldBeFound("contractId.specified=true");

        // Get all the svcPlanList where contractId is null
        defaultSvcPlanShouldNotBeFound("contractId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByContractIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where contractId is greater than or equal to DEFAULT_CONTRACT_ID
        defaultSvcPlanShouldBeFound("contractId.greaterThanOrEqual=" + DEFAULT_CONTRACT_ID);

        // Get all the svcPlanList where contractId is greater than or equal to UPDATED_CONTRACT_ID
        defaultSvcPlanShouldNotBeFound("contractId.greaterThanOrEqual=" + UPDATED_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByContractIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where contractId is less than or equal to DEFAULT_CONTRACT_ID
        defaultSvcPlanShouldBeFound("contractId.lessThanOrEqual=" + DEFAULT_CONTRACT_ID);

        // Get all the svcPlanList where contractId is less than or equal to SMALLER_CONTRACT_ID
        defaultSvcPlanShouldNotBeFound("contractId.lessThanOrEqual=" + SMALLER_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByContractIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where contractId is less than DEFAULT_CONTRACT_ID
        defaultSvcPlanShouldNotBeFound("contractId.lessThan=" + DEFAULT_CONTRACT_ID);

        // Get all the svcPlanList where contractId is less than UPDATED_CONTRACT_ID
        defaultSvcPlanShouldBeFound("contractId.lessThan=" + UPDATED_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByContractIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where contractId is greater than DEFAULT_CONTRACT_ID
        defaultSvcPlanShouldNotBeFound("contractId.greaterThan=" + DEFAULT_CONTRACT_ID);

        // Get all the svcPlanList where contractId is greater than SMALLER_CONTRACT_ID
        defaultSvcPlanShouldBeFound("contractId.greaterThan=" + SMALLER_CONTRACT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where note equals to DEFAULT_NOTE
        defaultSvcPlanShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the svcPlanList where note equals to UPDATED_NOTE
        defaultSvcPlanShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where note not equals to DEFAULT_NOTE
        defaultSvcPlanShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the svcPlanList where note not equals to UPDATED_NOTE
        defaultSvcPlanShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultSvcPlanShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the svcPlanList where note equals to UPDATED_NOTE
        defaultSvcPlanShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where note is not null
        defaultSvcPlanShouldBeFound("note.specified=true");

        // Get all the svcPlanList where note is null
        defaultSvcPlanShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlansByNoteContainsSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where note contains DEFAULT_NOTE
        defaultSvcPlanShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the svcPlanList where note contains UPDATED_NOTE
        defaultSvcPlanShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlansByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        // Get all the svcPlanList where note does not contain DEFAULT_NOTE
        defaultSvcPlanShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the svcPlanList where note does not contain UPDATED_NOTE
        defaultSvcPlanShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlansBySvcPlanUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);
        SvcPlanUnit svcPlanUnit = SvcPlanUnitResourceIT.createEntity(em);
        em.persist(svcPlanUnit);
        em.flush();
        svcPlan.addSvcPlanUnit(svcPlanUnit);
        svcPlanRepository.saveAndFlush(svcPlan);
        Long svcPlanUnitId = svcPlanUnit.getId();

        // Get all the svcPlanList where svcPlanUnit equals to svcPlanUnitId
        defaultSvcPlanShouldBeFound("svcPlanUnitId.equals=" + svcPlanUnitId);

        // Get all the svcPlanList where svcPlanUnit equals to (svcPlanUnitId + 1)
        defaultSvcPlanShouldNotBeFound("svcPlanUnitId.equals=" + (svcPlanUnitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcPlanShouldBeFound(String filter) throws Exception {
        restSvcPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].serviceManagerId").value(hasItem(DEFAULT_SERVICE_MANAGER_ID.intValue())))
            .andExpect(jsonPath("$.[*].defaultSuppervisorId").value(hasItem(DEFAULT_DEFAULT_SUPPERVISOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].startPlan").value(hasItem(DEFAULT_START_PLAN.toString())))
            .andExpect(jsonPath("$.[*].endPlan").value(hasItem(DEFAULT_END_PLAN.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractId").value(hasItem(DEFAULT_CONTRACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restSvcPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcPlanShouldNotBeFound(String filter) throws Exception {
        restSvcPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcPlan() throws Exception {
        // Get the svcPlan
        restSvcPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcPlan() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();

        // Update the svcPlan
        SvcPlan updatedSvcPlan = svcPlanRepository.findById(svcPlan.getId()).get();
        // Disconnect from session so that the updates on updatedSvcPlan are not directly saved in db
        em.detach(updatedSvcPlan);
        updatedSvcPlan
            .name(UPDATED_NAME)
            .serviceManagerId(UPDATED_SERVICE_MANAGER_ID)
            .defaultSuppervisorId(UPDATED_DEFAULT_SUPPERVISOR_ID)
            .status(UPDATED_STATUS)
            .startPlan(UPDATED_START_PLAN)
            .endPlan(UPDATED_END_PLAN)
            .createDate(UPDATED_CREATE_DATE)
            .contractId(UPDATED_CONTRACT_ID)
            .note(UPDATED_NOTE);
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(updatedSvcPlan);

        restSvcPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
        SvcPlan testSvcPlan = svcPlanList.get(svcPlanList.size() - 1);
        assertThat(testSvcPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcPlan.getServiceManagerId()).isEqualTo(UPDATED_SERVICE_MANAGER_ID);
        assertThat(testSvcPlan.getDefaultSuppervisorId()).isEqualTo(UPDATED_DEFAULT_SUPPERVISOR_ID);
        assertThat(testSvcPlan.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcPlan.getStartPlan()).isEqualTo(UPDATED_START_PLAN);
        assertThat(testSvcPlan.getEndPlan()).isEqualTo(UPDATED_END_PLAN);
        assertThat(testSvcPlan.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcPlan.getContractId()).isEqualTo(UPDATED_CONTRACT_ID);
        assertThat(testSvcPlan.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingSvcPlan() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();
        svcPlan.setId(count.incrementAndGet());

        // Create the SvcPlan
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcPlan() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();
        svcPlan.setId(count.incrementAndGet());

        // Create the SvcPlan
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcPlan() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();
        svcPlan.setId(count.incrementAndGet());

        // Create the SvcPlan
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcPlanWithPatch() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();

        // Update the svcPlan using partial update
        SvcPlan partialUpdatedSvcPlan = new SvcPlan();
        partialUpdatedSvcPlan.setId(svcPlan.getId());

        partialUpdatedSvcPlan
            .defaultSuppervisorId(UPDATED_DEFAULT_SUPPERVISOR_ID)
            .status(UPDATED_STATUS)
            .startPlan(UPDATED_START_PLAN)
            .createDate(UPDATED_CREATE_DATE)
            .contractId(UPDATED_CONTRACT_ID);

        restSvcPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcPlan))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
        SvcPlan testSvcPlan = svcPlanList.get(svcPlanList.size() - 1);
        assertThat(testSvcPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcPlan.getServiceManagerId()).isEqualTo(DEFAULT_SERVICE_MANAGER_ID);
        assertThat(testSvcPlan.getDefaultSuppervisorId()).isEqualTo(UPDATED_DEFAULT_SUPPERVISOR_ID);
        assertThat(testSvcPlan.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcPlan.getStartPlan()).isEqualTo(UPDATED_START_PLAN);
        assertThat(testSvcPlan.getEndPlan()).isEqualTo(DEFAULT_END_PLAN);
        assertThat(testSvcPlan.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcPlan.getContractId()).isEqualTo(UPDATED_CONTRACT_ID);
        assertThat(testSvcPlan.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateSvcPlanWithPatch() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();

        // Update the svcPlan using partial update
        SvcPlan partialUpdatedSvcPlan = new SvcPlan();
        partialUpdatedSvcPlan.setId(svcPlan.getId());

        partialUpdatedSvcPlan
            .name(UPDATED_NAME)
            .serviceManagerId(UPDATED_SERVICE_MANAGER_ID)
            .defaultSuppervisorId(UPDATED_DEFAULT_SUPPERVISOR_ID)
            .status(UPDATED_STATUS)
            .startPlan(UPDATED_START_PLAN)
            .endPlan(UPDATED_END_PLAN)
            .createDate(UPDATED_CREATE_DATE)
            .contractId(UPDATED_CONTRACT_ID)
            .note(UPDATED_NOTE);

        restSvcPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcPlan))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
        SvcPlan testSvcPlan = svcPlanList.get(svcPlanList.size() - 1);
        assertThat(testSvcPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcPlan.getServiceManagerId()).isEqualTo(UPDATED_SERVICE_MANAGER_ID);
        assertThat(testSvcPlan.getDefaultSuppervisorId()).isEqualTo(UPDATED_DEFAULT_SUPPERVISOR_ID);
        assertThat(testSvcPlan.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcPlan.getStartPlan()).isEqualTo(UPDATED_START_PLAN);
        assertThat(testSvcPlan.getEndPlan()).isEqualTo(UPDATED_END_PLAN);
        assertThat(testSvcPlan.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcPlan.getContractId()).isEqualTo(UPDATED_CONTRACT_ID);
        assertThat(testSvcPlan.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingSvcPlan() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();
        svcPlan.setId(count.incrementAndGet());

        // Create the SvcPlan
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcPlan() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();
        svcPlan.setId(count.incrementAndGet());

        // Create the SvcPlan
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcPlan() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanRepository.findAll().size();
        svcPlan.setId(count.incrementAndGet());

        // Create the SvcPlan
        SvcPlanDTO svcPlanDTO = svcPlanMapper.toDto(svcPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcPlan in the database
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcPlan() throws Exception {
        // Initialize the database
        svcPlanRepository.saveAndFlush(svcPlan);

        int databaseSizeBeforeDelete = svcPlanRepository.findAll().size();

        // Delete the svcPlan
        restSvcPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcPlan> svcPlanList = svcPlanRepository.findAll();
        assertThat(svcPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
