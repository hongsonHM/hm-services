package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcPlanPart;
import com.overnetcontact.dvvs.repository.SvcPlanPartRepository;
import com.overnetcontact.dvvs.service.criteria.SvcPlanPartCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanPartDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanPartMapper;
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
 * Integration tests for the {@link SvcPlanPartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcPlanPartResourceIT {

    private static final Long DEFAULT_PLAN_UNIT_ID = 1L;
    private static final Long UPDATED_PLAN_UNIT_ID = 2L;
    private static final Long SMALLER_PLAN_UNIT_ID = 1L - 1L;

    private static final Long DEFAULT_SPEND_TASK_ID = 1L;
    private static final Long UPDATED_SPEND_TASK_ID = 2L;
    private static final Long SMALLER_SPEND_TASK_ID = 1L - 1L;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_START_AT = "AAAAAAAAAA";
    private static final String UPDATED_START_AT = "BBBBBBBBBB";

    private static final String DEFAULT_END_AT = "AAAAAAAAAA";
    private static final String UPDATED_END_AT = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_PERIODIC = "AAAAAAAAAA";
    private static final String UPDATED_PERIODIC = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_WORK_ON_DAYS = "AAAAAAAAAA";
    private static final String UPDATED_WORK_ON_DAYS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-plan-parts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcPlanPartRepository svcPlanPartRepository;

    @Autowired
    private SvcPlanPartMapper svcPlanPartMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcPlanPartMockMvc;

    private SvcPlanPart svcPlanPart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcPlanPart createEntity(EntityManager em) {
        SvcPlanPart svcPlanPart = new SvcPlanPart()
            .planUnitID(DEFAULT_PLAN_UNIT_ID)
            .spendTaskID(DEFAULT_SPEND_TASK_ID)
            .location(DEFAULT_LOCATION)
            .startAt(DEFAULT_START_AT)
            .endAt(DEFAULT_END_AT)
            .frequency(DEFAULT_FREQUENCY)
            .periodic(DEFAULT_PERIODIC)
            .note(DEFAULT_NOTE)
            .workOnDays(DEFAULT_WORK_ON_DAYS);
        return svcPlanPart;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcPlanPart createUpdatedEntity(EntityManager em) {
        SvcPlanPart svcPlanPart = new SvcPlanPart()
            .planUnitID(UPDATED_PLAN_UNIT_ID)
            .spendTaskID(UPDATED_SPEND_TASK_ID)
            .location(UPDATED_LOCATION)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .frequency(UPDATED_FREQUENCY)
            .periodic(UPDATED_PERIODIC)
            .note(UPDATED_NOTE)
            .workOnDays(UPDATED_WORK_ON_DAYS);
        return svcPlanPart;
    }

    @BeforeEach
    public void initTest() {
        svcPlanPart = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcPlanPart() throws Exception {
        int databaseSizeBeforeCreate = svcPlanPartRepository.findAll().size();
        // Create the SvcPlanPart
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);
        restSvcPlanPartMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeCreate + 1);
        SvcPlanPart testSvcPlanPart = svcPlanPartList.get(svcPlanPartList.size() - 1);
        assertThat(testSvcPlanPart.getPlanUnitID()).isEqualTo(DEFAULT_PLAN_UNIT_ID);
        assertThat(testSvcPlanPart.getSpendTaskID()).isEqualTo(DEFAULT_SPEND_TASK_ID);
        assertThat(testSvcPlanPart.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSvcPlanPart.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testSvcPlanPart.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testSvcPlanPart.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testSvcPlanPart.getPeriodic()).isEqualTo(DEFAULT_PERIODIC);
        assertThat(testSvcPlanPart.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSvcPlanPart.getWorkOnDays()).isEqualTo(DEFAULT_WORK_ON_DAYS);
    }

    @Test
    @Transactional
    void createSvcPlanPartWithExistingId() throws Exception {
        // Create the SvcPlanPart with an existing ID
        svcPlanPart.setId(1L);
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        int databaseSizeBeforeCreate = svcPlanPartRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcPlanPartMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlanUnitIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcPlanPartRepository.findAll().size();
        // set the field null
        svcPlanPart.setPlanUnitID(null);

        // Create the SvcPlanPart, which fails.
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        restSvcPlanPartMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpendTaskIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcPlanPartRepository.findAll().size();
        // set the field null
        svcPlanPart.setSpendTaskID(null);

        // Create the SvcPlanPart, which fails.
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        restSvcPlanPartMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcPlanParts() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList
        restSvcPlanPartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcPlanPart.getId().intValue())))
            .andExpect(jsonPath("$.[*].planUnitID").value(hasItem(DEFAULT_PLAN_UNIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].spendTaskID").value(hasItem(DEFAULT_SPEND_TASK_ID.intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT)))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].periodic").value(hasItem(DEFAULT_PERIODIC)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].workOnDays").value(hasItem(DEFAULT_WORK_ON_DAYS)));
    }

    @Test
    @Transactional
    void getSvcPlanPart() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get the svcPlanPart
        restSvcPlanPartMockMvc
            .perform(get(ENTITY_API_URL_ID, svcPlanPart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcPlanPart.getId().intValue()))
            .andExpect(jsonPath("$.planUnitID").value(DEFAULT_PLAN_UNIT_ID.intValue()))
            .andExpect(jsonPath("$.spendTaskID").value(DEFAULT_SPEND_TASK_ID.intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT))
            .andExpect(jsonPath("$.endAt").value(DEFAULT_END_AT))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY))
            .andExpect(jsonPath("$.periodic").value(DEFAULT_PERIODIC))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.workOnDays").value(DEFAULT_WORK_ON_DAYS));
    }

    @Test
    @Transactional
    void getSvcPlanPartsByIdFiltering() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        Long id = svcPlanPart.getId();

        defaultSvcPlanPartShouldBeFound("id.equals=" + id);
        defaultSvcPlanPartShouldNotBeFound("id.notEquals=" + id);

        defaultSvcPlanPartShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcPlanPartShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcPlanPartShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcPlanPartShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPlanUnitIDIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where planUnitID equals to DEFAULT_PLAN_UNIT_ID
        defaultSvcPlanPartShouldBeFound("planUnitID.equals=" + DEFAULT_PLAN_UNIT_ID);

        // Get all the svcPlanPartList where planUnitID equals to UPDATED_PLAN_UNIT_ID
        defaultSvcPlanPartShouldNotBeFound("planUnitID.equals=" + UPDATED_PLAN_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPlanUnitIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where planUnitID not equals to DEFAULT_PLAN_UNIT_ID
        defaultSvcPlanPartShouldNotBeFound("planUnitID.notEquals=" + DEFAULT_PLAN_UNIT_ID);

        // Get all the svcPlanPartList where planUnitID not equals to UPDATED_PLAN_UNIT_ID
        defaultSvcPlanPartShouldBeFound("planUnitID.notEquals=" + UPDATED_PLAN_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPlanUnitIDIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where planUnitID in DEFAULT_PLAN_UNIT_ID or UPDATED_PLAN_UNIT_ID
        defaultSvcPlanPartShouldBeFound("planUnitID.in=" + DEFAULT_PLAN_UNIT_ID + "," + UPDATED_PLAN_UNIT_ID);

        // Get all the svcPlanPartList where planUnitID equals to UPDATED_PLAN_UNIT_ID
        defaultSvcPlanPartShouldNotBeFound("planUnitID.in=" + UPDATED_PLAN_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPlanUnitIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where planUnitID is not null
        defaultSvcPlanPartShouldBeFound("planUnitID.specified=true");

        // Get all the svcPlanPartList where planUnitID is null
        defaultSvcPlanPartShouldNotBeFound("planUnitID.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPlanUnitIDIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where planUnitID is greater than or equal to DEFAULT_PLAN_UNIT_ID
        defaultSvcPlanPartShouldBeFound("planUnitID.greaterThanOrEqual=" + DEFAULT_PLAN_UNIT_ID);

        // Get all the svcPlanPartList where planUnitID is greater than or equal to UPDATED_PLAN_UNIT_ID
        defaultSvcPlanPartShouldNotBeFound("planUnitID.greaterThanOrEqual=" + UPDATED_PLAN_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPlanUnitIDIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where planUnitID is less than or equal to DEFAULT_PLAN_UNIT_ID
        defaultSvcPlanPartShouldBeFound("planUnitID.lessThanOrEqual=" + DEFAULT_PLAN_UNIT_ID);

        // Get all the svcPlanPartList where planUnitID is less than or equal to SMALLER_PLAN_UNIT_ID
        defaultSvcPlanPartShouldNotBeFound("planUnitID.lessThanOrEqual=" + SMALLER_PLAN_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPlanUnitIDIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where planUnitID is less than DEFAULT_PLAN_UNIT_ID
        defaultSvcPlanPartShouldNotBeFound("planUnitID.lessThan=" + DEFAULT_PLAN_UNIT_ID);

        // Get all the svcPlanPartList where planUnitID is less than UPDATED_PLAN_UNIT_ID
        defaultSvcPlanPartShouldBeFound("planUnitID.lessThan=" + UPDATED_PLAN_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPlanUnitIDIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where planUnitID is greater than DEFAULT_PLAN_UNIT_ID
        defaultSvcPlanPartShouldNotBeFound("planUnitID.greaterThan=" + DEFAULT_PLAN_UNIT_ID);

        // Get all the svcPlanPartList where planUnitID is greater than SMALLER_PLAN_UNIT_ID
        defaultSvcPlanPartShouldBeFound("planUnitID.greaterThan=" + SMALLER_PLAN_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsBySpendTaskIDIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where spendTaskID equals to DEFAULT_SPEND_TASK_ID
        defaultSvcPlanPartShouldBeFound("spendTaskID.equals=" + DEFAULT_SPEND_TASK_ID);

        // Get all the svcPlanPartList where spendTaskID equals to UPDATED_SPEND_TASK_ID
        defaultSvcPlanPartShouldNotBeFound("spendTaskID.equals=" + UPDATED_SPEND_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsBySpendTaskIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where spendTaskID not equals to DEFAULT_SPEND_TASK_ID
        defaultSvcPlanPartShouldNotBeFound("spendTaskID.notEquals=" + DEFAULT_SPEND_TASK_ID);

        // Get all the svcPlanPartList where spendTaskID not equals to UPDATED_SPEND_TASK_ID
        defaultSvcPlanPartShouldBeFound("spendTaskID.notEquals=" + UPDATED_SPEND_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsBySpendTaskIDIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where spendTaskID in DEFAULT_SPEND_TASK_ID or UPDATED_SPEND_TASK_ID
        defaultSvcPlanPartShouldBeFound("spendTaskID.in=" + DEFAULT_SPEND_TASK_ID + "," + UPDATED_SPEND_TASK_ID);

        // Get all the svcPlanPartList where spendTaskID equals to UPDATED_SPEND_TASK_ID
        defaultSvcPlanPartShouldNotBeFound("spendTaskID.in=" + UPDATED_SPEND_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsBySpendTaskIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where spendTaskID is not null
        defaultSvcPlanPartShouldBeFound("spendTaskID.specified=true");

        // Get all the svcPlanPartList where spendTaskID is null
        defaultSvcPlanPartShouldNotBeFound("spendTaskID.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsBySpendTaskIDIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where spendTaskID is greater than or equal to DEFAULT_SPEND_TASK_ID
        defaultSvcPlanPartShouldBeFound("spendTaskID.greaterThanOrEqual=" + DEFAULT_SPEND_TASK_ID);

        // Get all the svcPlanPartList where spendTaskID is greater than or equal to UPDATED_SPEND_TASK_ID
        defaultSvcPlanPartShouldNotBeFound("spendTaskID.greaterThanOrEqual=" + UPDATED_SPEND_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsBySpendTaskIDIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where spendTaskID is less than or equal to DEFAULT_SPEND_TASK_ID
        defaultSvcPlanPartShouldBeFound("spendTaskID.lessThanOrEqual=" + DEFAULT_SPEND_TASK_ID);

        // Get all the svcPlanPartList where spendTaskID is less than or equal to SMALLER_SPEND_TASK_ID
        defaultSvcPlanPartShouldNotBeFound("spendTaskID.lessThanOrEqual=" + SMALLER_SPEND_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsBySpendTaskIDIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where spendTaskID is less than DEFAULT_SPEND_TASK_ID
        defaultSvcPlanPartShouldNotBeFound("spendTaskID.lessThan=" + DEFAULT_SPEND_TASK_ID);

        // Get all the svcPlanPartList where spendTaskID is less than UPDATED_SPEND_TASK_ID
        defaultSvcPlanPartShouldBeFound("spendTaskID.lessThan=" + UPDATED_SPEND_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsBySpendTaskIDIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where spendTaskID is greater than DEFAULT_SPEND_TASK_ID
        defaultSvcPlanPartShouldNotBeFound("spendTaskID.greaterThan=" + DEFAULT_SPEND_TASK_ID);

        // Get all the svcPlanPartList where spendTaskID is greater than SMALLER_SPEND_TASK_ID
        defaultSvcPlanPartShouldBeFound("spendTaskID.greaterThan=" + SMALLER_SPEND_TASK_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where location equals to DEFAULT_LOCATION
        defaultSvcPlanPartShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the svcPlanPartList where location equals to UPDATED_LOCATION
        defaultSvcPlanPartShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where location not equals to DEFAULT_LOCATION
        defaultSvcPlanPartShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the svcPlanPartList where location not equals to UPDATED_LOCATION
        defaultSvcPlanPartShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultSvcPlanPartShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the svcPlanPartList where location equals to UPDATED_LOCATION
        defaultSvcPlanPartShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where location is not null
        defaultSvcPlanPartShouldBeFound("location.specified=true");

        // Get all the svcPlanPartList where location is null
        defaultSvcPlanPartShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByLocationContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where location contains DEFAULT_LOCATION
        defaultSvcPlanPartShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the svcPlanPartList where location contains UPDATED_LOCATION
        defaultSvcPlanPartShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where location does not contain DEFAULT_LOCATION
        defaultSvcPlanPartShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the svcPlanPartList where location does not contain UPDATED_LOCATION
        defaultSvcPlanPartShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByStartAtIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where startAt equals to DEFAULT_START_AT
        defaultSvcPlanPartShouldBeFound("startAt.equals=" + DEFAULT_START_AT);

        // Get all the svcPlanPartList where startAt equals to UPDATED_START_AT
        defaultSvcPlanPartShouldNotBeFound("startAt.equals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByStartAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where startAt not equals to DEFAULT_START_AT
        defaultSvcPlanPartShouldNotBeFound("startAt.notEquals=" + DEFAULT_START_AT);

        // Get all the svcPlanPartList where startAt not equals to UPDATED_START_AT
        defaultSvcPlanPartShouldBeFound("startAt.notEquals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByStartAtIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where startAt in DEFAULT_START_AT or UPDATED_START_AT
        defaultSvcPlanPartShouldBeFound("startAt.in=" + DEFAULT_START_AT + "," + UPDATED_START_AT);

        // Get all the svcPlanPartList where startAt equals to UPDATED_START_AT
        defaultSvcPlanPartShouldNotBeFound("startAt.in=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByStartAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where startAt is not null
        defaultSvcPlanPartShouldBeFound("startAt.specified=true");

        // Get all the svcPlanPartList where startAt is null
        defaultSvcPlanPartShouldNotBeFound("startAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByStartAtContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where startAt contains DEFAULT_START_AT
        defaultSvcPlanPartShouldBeFound("startAt.contains=" + DEFAULT_START_AT);

        // Get all the svcPlanPartList where startAt contains UPDATED_START_AT
        defaultSvcPlanPartShouldNotBeFound("startAt.contains=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByStartAtNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where startAt does not contain DEFAULT_START_AT
        defaultSvcPlanPartShouldNotBeFound("startAt.doesNotContain=" + DEFAULT_START_AT);

        // Get all the svcPlanPartList where startAt does not contain UPDATED_START_AT
        defaultSvcPlanPartShouldBeFound("startAt.doesNotContain=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByEndAtIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where endAt equals to DEFAULT_END_AT
        defaultSvcPlanPartShouldBeFound("endAt.equals=" + DEFAULT_END_AT);

        // Get all the svcPlanPartList where endAt equals to UPDATED_END_AT
        defaultSvcPlanPartShouldNotBeFound("endAt.equals=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByEndAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where endAt not equals to DEFAULT_END_AT
        defaultSvcPlanPartShouldNotBeFound("endAt.notEquals=" + DEFAULT_END_AT);

        // Get all the svcPlanPartList where endAt not equals to UPDATED_END_AT
        defaultSvcPlanPartShouldBeFound("endAt.notEquals=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByEndAtIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where endAt in DEFAULT_END_AT or UPDATED_END_AT
        defaultSvcPlanPartShouldBeFound("endAt.in=" + DEFAULT_END_AT + "," + UPDATED_END_AT);

        // Get all the svcPlanPartList where endAt equals to UPDATED_END_AT
        defaultSvcPlanPartShouldNotBeFound("endAt.in=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByEndAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where endAt is not null
        defaultSvcPlanPartShouldBeFound("endAt.specified=true");

        // Get all the svcPlanPartList where endAt is null
        defaultSvcPlanPartShouldNotBeFound("endAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByEndAtContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where endAt contains DEFAULT_END_AT
        defaultSvcPlanPartShouldBeFound("endAt.contains=" + DEFAULT_END_AT);

        // Get all the svcPlanPartList where endAt contains UPDATED_END_AT
        defaultSvcPlanPartShouldNotBeFound("endAt.contains=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByEndAtNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where endAt does not contain DEFAULT_END_AT
        defaultSvcPlanPartShouldNotBeFound("endAt.doesNotContain=" + DEFAULT_END_AT);

        // Get all the svcPlanPartList where endAt does not contain UPDATED_END_AT
        defaultSvcPlanPartShouldBeFound("endAt.doesNotContain=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where frequency equals to DEFAULT_FREQUENCY
        defaultSvcPlanPartShouldBeFound("frequency.equals=" + DEFAULT_FREQUENCY);

        // Get all the svcPlanPartList where frequency equals to UPDATED_FREQUENCY
        defaultSvcPlanPartShouldNotBeFound("frequency.equals=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByFrequencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where frequency not equals to DEFAULT_FREQUENCY
        defaultSvcPlanPartShouldNotBeFound("frequency.notEquals=" + DEFAULT_FREQUENCY);

        // Get all the svcPlanPartList where frequency not equals to UPDATED_FREQUENCY
        defaultSvcPlanPartShouldBeFound("frequency.notEquals=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where frequency in DEFAULT_FREQUENCY or UPDATED_FREQUENCY
        defaultSvcPlanPartShouldBeFound("frequency.in=" + DEFAULT_FREQUENCY + "," + UPDATED_FREQUENCY);

        // Get all the svcPlanPartList where frequency equals to UPDATED_FREQUENCY
        defaultSvcPlanPartShouldNotBeFound("frequency.in=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where frequency is not null
        defaultSvcPlanPartShouldBeFound("frequency.specified=true");

        // Get all the svcPlanPartList where frequency is null
        defaultSvcPlanPartShouldNotBeFound("frequency.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByFrequencyContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where frequency contains DEFAULT_FREQUENCY
        defaultSvcPlanPartShouldBeFound("frequency.contains=" + DEFAULT_FREQUENCY);

        // Get all the svcPlanPartList where frequency contains UPDATED_FREQUENCY
        defaultSvcPlanPartShouldNotBeFound("frequency.contains=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByFrequencyNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where frequency does not contain DEFAULT_FREQUENCY
        defaultSvcPlanPartShouldNotBeFound("frequency.doesNotContain=" + DEFAULT_FREQUENCY);

        // Get all the svcPlanPartList where frequency does not contain UPDATED_FREQUENCY
        defaultSvcPlanPartShouldBeFound("frequency.doesNotContain=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPeriodicIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where periodic equals to DEFAULT_PERIODIC
        defaultSvcPlanPartShouldBeFound("periodic.equals=" + DEFAULT_PERIODIC);

        // Get all the svcPlanPartList where periodic equals to UPDATED_PERIODIC
        defaultSvcPlanPartShouldNotBeFound("periodic.equals=" + UPDATED_PERIODIC);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPeriodicIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where periodic not equals to DEFAULT_PERIODIC
        defaultSvcPlanPartShouldNotBeFound("periodic.notEquals=" + DEFAULT_PERIODIC);

        // Get all the svcPlanPartList where periodic not equals to UPDATED_PERIODIC
        defaultSvcPlanPartShouldBeFound("periodic.notEquals=" + UPDATED_PERIODIC);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPeriodicIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where periodic in DEFAULT_PERIODIC or UPDATED_PERIODIC
        defaultSvcPlanPartShouldBeFound("periodic.in=" + DEFAULT_PERIODIC + "," + UPDATED_PERIODIC);

        // Get all the svcPlanPartList where periodic equals to UPDATED_PERIODIC
        defaultSvcPlanPartShouldNotBeFound("periodic.in=" + UPDATED_PERIODIC);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPeriodicIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where periodic is not null
        defaultSvcPlanPartShouldBeFound("periodic.specified=true");

        // Get all the svcPlanPartList where periodic is null
        defaultSvcPlanPartShouldNotBeFound("periodic.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPeriodicContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where periodic contains DEFAULT_PERIODIC
        defaultSvcPlanPartShouldBeFound("periodic.contains=" + DEFAULT_PERIODIC);

        // Get all the svcPlanPartList where periodic contains UPDATED_PERIODIC
        defaultSvcPlanPartShouldNotBeFound("periodic.contains=" + UPDATED_PERIODIC);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByPeriodicNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where periodic does not contain DEFAULT_PERIODIC
        defaultSvcPlanPartShouldNotBeFound("periodic.doesNotContain=" + DEFAULT_PERIODIC);

        // Get all the svcPlanPartList where periodic does not contain UPDATED_PERIODIC
        defaultSvcPlanPartShouldBeFound("periodic.doesNotContain=" + UPDATED_PERIODIC);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where note equals to DEFAULT_NOTE
        defaultSvcPlanPartShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the svcPlanPartList where note equals to UPDATED_NOTE
        defaultSvcPlanPartShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where note not equals to DEFAULT_NOTE
        defaultSvcPlanPartShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the svcPlanPartList where note not equals to UPDATED_NOTE
        defaultSvcPlanPartShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultSvcPlanPartShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the svcPlanPartList where note equals to UPDATED_NOTE
        defaultSvcPlanPartShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where note is not null
        defaultSvcPlanPartShouldBeFound("note.specified=true");

        // Get all the svcPlanPartList where note is null
        defaultSvcPlanPartShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByNoteContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where note contains DEFAULT_NOTE
        defaultSvcPlanPartShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the svcPlanPartList where note contains UPDATED_NOTE
        defaultSvcPlanPartShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where note does not contain DEFAULT_NOTE
        defaultSvcPlanPartShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the svcPlanPartList where note does not contain UPDATED_NOTE
        defaultSvcPlanPartShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByWorkOnDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where workOnDays equals to DEFAULT_WORK_ON_DAYS
        defaultSvcPlanPartShouldBeFound("workOnDays.equals=" + DEFAULT_WORK_ON_DAYS);

        // Get all the svcPlanPartList where workOnDays equals to UPDATED_WORK_ON_DAYS
        defaultSvcPlanPartShouldNotBeFound("workOnDays.equals=" + UPDATED_WORK_ON_DAYS);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByWorkOnDaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where workOnDays not equals to DEFAULT_WORK_ON_DAYS
        defaultSvcPlanPartShouldNotBeFound("workOnDays.notEquals=" + DEFAULT_WORK_ON_DAYS);

        // Get all the svcPlanPartList where workOnDays not equals to UPDATED_WORK_ON_DAYS
        defaultSvcPlanPartShouldBeFound("workOnDays.notEquals=" + UPDATED_WORK_ON_DAYS);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByWorkOnDaysIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where workOnDays in DEFAULT_WORK_ON_DAYS or UPDATED_WORK_ON_DAYS
        defaultSvcPlanPartShouldBeFound("workOnDays.in=" + DEFAULT_WORK_ON_DAYS + "," + UPDATED_WORK_ON_DAYS);

        // Get all the svcPlanPartList where workOnDays equals to UPDATED_WORK_ON_DAYS
        defaultSvcPlanPartShouldNotBeFound("workOnDays.in=" + UPDATED_WORK_ON_DAYS);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByWorkOnDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where workOnDays is not null
        defaultSvcPlanPartShouldBeFound("workOnDays.specified=true");

        // Get all the svcPlanPartList where workOnDays is null
        defaultSvcPlanPartShouldNotBeFound("workOnDays.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByWorkOnDaysContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where workOnDays contains DEFAULT_WORK_ON_DAYS
        defaultSvcPlanPartShouldBeFound("workOnDays.contains=" + DEFAULT_WORK_ON_DAYS);

        // Get all the svcPlanPartList where workOnDays contains UPDATED_WORK_ON_DAYS
        defaultSvcPlanPartShouldNotBeFound("workOnDays.contains=" + UPDATED_WORK_ON_DAYS);
    }

    @Test
    @Transactional
    void getAllSvcPlanPartsByWorkOnDaysNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        // Get all the svcPlanPartList where workOnDays does not contain DEFAULT_WORK_ON_DAYS
        defaultSvcPlanPartShouldNotBeFound("workOnDays.doesNotContain=" + DEFAULT_WORK_ON_DAYS);

        // Get all the svcPlanPartList where workOnDays does not contain UPDATED_WORK_ON_DAYS
        defaultSvcPlanPartShouldBeFound("workOnDays.doesNotContain=" + UPDATED_WORK_ON_DAYS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcPlanPartShouldBeFound(String filter) throws Exception {
        restSvcPlanPartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcPlanPart.getId().intValue())))
            .andExpect(jsonPath("$.[*].planUnitID").value(hasItem(DEFAULT_PLAN_UNIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].spendTaskID").value(hasItem(DEFAULT_SPEND_TASK_ID.intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT)))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].periodic").value(hasItem(DEFAULT_PERIODIC)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].workOnDays").value(hasItem(DEFAULT_WORK_ON_DAYS)));

        // Check, that the count call also returns 1
        restSvcPlanPartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcPlanPartShouldNotBeFound(String filter) throws Exception {
        restSvcPlanPartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcPlanPartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcPlanPart() throws Exception {
        // Get the svcPlanPart
        restSvcPlanPartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcPlanPart() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();

        // Update the svcPlanPart
        SvcPlanPart updatedSvcPlanPart = svcPlanPartRepository.findById(svcPlanPart.getId()).get();
        // Disconnect from session so that the updates on updatedSvcPlanPart are not directly saved in db
        em.detach(updatedSvcPlanPart);
        updatedSvcPlanPart
            .planUnitID(UPDATED_PLAN_UNIT_ID)
            .spendTaskID(UPDATED_SPEND_TASK_ID)
            .location(UPDATED_LOCATION)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .frequency(UPDATED_FREQUENCY)
            .periodic(UPDATED_PERIODIC)
            .note(UPDATED_NOTE)
            .workOnDays(UPDATED_WORK_ON_DAYS);
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(updatedSvcPlanPart);

        restSvcPlanPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcPlanPartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanPart testSvcPlanPart = svcPlanPartList.get(svcPlanPartList.size() - 1);
        assertThat(testSvcPlanPart.getPlanUnitID()).isEqualTo(UPDATED_PLAN_UNIT_ID);
        assertThat(testSvcPlanPart.getSpendTaskID()).isEqualTo(UPDATED_SPEND_TASK_ID);
        assertThat(testSvcPlanPart.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSvcPlanPart.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testSvcPlanPart.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testSvcPlanPart.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testSvcPlanPart.getPeriodic()).isEqualTo(UPDATED_PERIODIC);
        assertThat(testSvcPlanPart.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSvcPlanPart.getWorkOnDays()).isEqualTo(UPDATED_WORK_ON_DAYS);
    }

    @Test
    @Transactional
    void putNonExistingSvcPlanPart() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();
        svcPlanPart.setId(count.incrementAndGet());

        // Create the SvcPlanPart
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcPlanPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcPlanPartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcPlanPart() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();
        svcPlanPart.setId(count.incrementAndGet());

        // Create the SvcPlanPart
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcPlanPart() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();
        svcPlanPart.setId(count.incrementAndGet());

        // Create the SvcPlanPart
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanPartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcPlanPartWithPatch() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();

        // Update the svcPlanPart using partial update
        SvcPlanPart partialUpdatedSvcPlanPart = new SvcPlanPart();
        partialUpdatedSvcPlanPart.setId(svcPlanPart.getId());

        partialUpdatedSvcPlanPart
            .planUnitID(UPDATED_PLAN_UNIT_ID)
            .spendTaskID(UPDATED_SPEND_TASK_ID)
            .endAt(UPDATED_END_AT)
            .periodic(UPDATED_PERIODIC)
            .workOnDays(UPDATED_WORK_ON_DAYS);

        restSvcPlanPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcPlanPart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcPlanPart))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanPart testSvcPlanPart = svcPlanPartList.get(svcPlanPartList.size() - 1);
        assertThat(testSvcPlanPart.getPlanUnitID()).isEqualTo(UPDATED_PLAN_UNIT_ID);
        assertThat(testSvcPlanPart.getSpendTaskID()).isEqualTo(UPDATED_SPEND_TASK_ID);
        assertThat(testSvcPlanPart.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSvcPlanPart.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testSvcPlanPart.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testSvcPlanPart.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testSvcPlanPart.getPeriodic()).isEqualTo(UPDATED_PERIODIC);
        assertThat(testSvcPlanPart.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSvcPlanPart.getWorkOnDays()).isEqualTo(UPDATED_WORK_ON_DAYS);
    }

    @Test
    @Transactional
    void fullUpdateSvcPlanPartWithPatch() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();

        // Update the svcPlanPart using partial update
        SvcPlanPart partialUpdatedSvcPlanPart = new SvcPlanPart();
        partialUpdatedSvcPlanPart.setId(svcPlanPart.getId());

        partialUpdatedSvcPlanPart
            .planUnitID(UPDATED_PLAN_UNIT_ID)
            .spendTaskID(UPDATED_SPEND_TASK_ID)
            .location(UPDATED_LOCATION)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .frequency(UPDATED_FREQUENCY)
            .periodic(UPDATED_PERIODIC)
            .note(UPDATED_NOTE)
            .workOnDays(UPDATED_WORK_ON_DAYS);

        restSvcPlanPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcPlanPart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcPlanPart))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanPart testSvcPlanPart = svcPlanPartList.get(svcPlanPartList.size() - 1);
        assertThat(testSvcPlanPart.getPlanUnitID()).isEqualTo(UPDATED_PLAN_UNIT_ID);
        assertThat(testSvcPlanPart.getSpendTaskID()).isEqualTo(UPDATED_SPEND_TASK_ID);
        assertThat(testSvcPlanPart.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSvcPlanPart.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testSvcPlanPart.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testSvcPlanPart.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testSvcPlanPart.getPeriodic()).isEqualTo(UPDATED_PERIODIC);
        assertThat(testSvcPlanPart.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSvcPlanPart.getWorkOnDays()).isEqualTo(UPDATED_WORK_ON_DAYS);
    }

    @Test
    @Transactional
    void patchNonExistingSvcPlanPart() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();
        svcPlanPart.setId(count.incrementAndGet());

        // Create the SvcPlanPart
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcPlanPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcPlanPartDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcPlanPart() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();
        svcPlanPart.setId(count.incrementAndGet());

        // Create the SvcPlanPart
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcPlanPart() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanPartRepository.findAll().size();
        svcPlanPart.setId(count.incrementAndGet());

        // Create the SvcPlanPart
        SvcPlanPartDTO svcPlanPartDTO = svcPlanPartMapper.toDto(svcPlanPart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanPartMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcPlanPartDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcPlanPart in the database
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcPlanPart() throws Exception {
        // Initialize the database
        svcPlanPartRepository.saveAndFlush(svcPlanPart);

        int databaseSizeBeforeDelete = svcPlanPartRepository.findAll().size();

        // Delete the svcPlanPart
        restSvcPlanPartMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcPlanPart.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcPlanPart> svcPlanPartList = svcPlanPartRepository.findAll();
        assertThat(svcPlanPartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
