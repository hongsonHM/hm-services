package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcLabor;
import com.overnetcontact.dvvs.domain.SvcPlan;
import com.overnetcontact.dvvs.domain.SvcPlanTask;
import com.overnetcontact.dvvs.domain.SvcPlanUnit;
import com.overnetcontact.dvvs.repository.SvcPlanUnitRepository;
import com.overnetcontact.dvvs.service.criteria.SvcPlanUnitCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanUnitDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanUnitMapper;
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
 * Integration tests for the {@link SvcPlanUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcPlanUnitResourceIT {

    private static final LocalDate DEFAULT_START_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_AT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_AT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_AT = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Integer DEFAULT_AMOUNT_OF_WORK = 1;
    private static final Integer UPDATED_AMOUNT_OF_WORK = 2;
    private static final Integer SMALLER_AMOUNT_OF_WORK = 1 - 1;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Long DEFAULT_SUPPERVISOR_ID = 1L;
    private static final Long UPDATED_SUPPERVISOR_ID = 2L;
    private static final Long SMALLER_SUPPERVISOR_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/svc-plan-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcPlanUnitRepository svcPlanUnitRepository;

    @Autowired
    private SvcPlanUnitMapper svcPlanUnitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcPlanUnitMockMvc;

    private SvcPlanUnit svcPlanUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcPlanUnit createEntity(EntityManager em) {
        SvcPlanUnit svcPlanUnit = new SvcPlanUnit()
            .startAt(DEFAULT_START_AT)
            .endAt(DEFAULT_END_AT)
            .createAt(DEFAULT_CREATE_AT)
            .status(DEFAULT_STATUS)
            .amountOfWork(DEFAULT_AMOUNT_OF_WORK)
            .quantity(DEFAULT_QUANTITY)
            .frequency(DEFAULT_FREQUENCY)
            .note(DEFAULT_NOTE)
            .suppervisorId(DEFAULT_SUPPERVISOR_ID);
        return svcPlanUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcPlanUnit createUpdatedEntity(EntityManager em) {
        SvcPlanUnit svcPlanUnit = new SvcPlanUnit()
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .createAt(UPDATED_CREATE_AT)
            .status(UPDATED_STATUS)
            .amountOfWork(UPDATED_AMOUNT_OF_WORK)
            .quantity(UPDATED_QUANTITY)
            .frequency(UPDATED_FREQUENCY)
            .note(UPDATED_NOTE)
            .suppervisorId(UPDATED_SUPPERVISOR_ID);
        return svcPlanUnit;
    }

    @BeforeEach
    public void initTest() {
        svcPlanUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcPlanUnit() throws Exception {
        int databaseSizeBeforeCreate = svcPlanUnitRepository.findAll().size();
        // Create the SvcPlanUnit
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(svcPlanUnit);
        restSvcPlanUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeCreate + 1);
        SvcPlanUnit testSvcPlanUnit = svcPlanUnitList.get(svcPlanUnitList.size() - 1);
        assertThat(testSvcPlanUnit.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testSvcPlanUnit.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testSvcPlanUnit.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testSvcPlanUnit.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSvcPlanUnit.getAmountOfWork()).isEqualTo(DEFAULT_AMOUNT_OF_WORK);
        assertThat(testSvcPlanUnit.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSvcPlanUnit.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testSvcPlanUnit.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSvcPlanUnit.getSuppervisorId()).isEqualTo(DEFAULT_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void createSvcPlanUnitWithExistingId() throws Exception {
        // Create the SvcPlanUnit with an existing ID
        svcPlanUnit.setId(1L);
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(svcPlanUnit);

        int databaseSizeBeforeCreate = svcPlanUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcPlanUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnits() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList
        restSvcPlanUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcPlanUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].amountOfWork").value(hasItem(DEFAULT_AMOUNT_OF_WORK)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].suppervisorId").value(hasItem(DEFAULT_SUPPERVISOR_ID.intValue())));
    }

    @Test
    @Transactional
    void getSvcPlanUnit() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get the svcPlanUnit
        restSvcPlanUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, svcPlanUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcPlanUnit.getId().intValue()))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.endAt").value(DEFAULT_END_AT.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.amountOfWork").value(DEFAULT_AMOUNT_OF_WORK))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.suppervisorId").value(DEFAULT_SUPPERVISOR_ID.intValue()));
    }

    @Test
    @Transactional
    void getSvcPlanUnitsByIdFiltering() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        Long id = svcPlanUnit.getId();

        defaultSvcPlanUnitShouldBeFound("id.equals=" + id);
        defaultSvcPlanUnitShouldNotBeFound("id.notEquals=" + id);

        defaultSvcPlanUnitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcPlanUnitShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcPlanUnitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcPlanUnitShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStartAtIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where startAt equals to DEFAULT_START_AT
        defaultSvcPlanUnitShouldBeFound("startAt.equals=" + DEFAULT_START_AT);

        // Get all the svcPlanUnitList where startAt equals to UPDATED_START_AT
        defaultSvcPlanUnitShouldNotBeFound("startAt.equals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStartAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where startAt not equals to DEFAULT_START_AT
        defaultSvcPlanUnitShouldNotBeFound("startAt.notEquals=" + DEFAULT_START_AT);

        // Get all the svcPlanUnitList where startAt not equals to UPDATED_START_AT
        defaultSvcPlanUnitShouldBeFound("startAt.notEquals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStartAtIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where startAt in DEFAULT_START_AT or UPDATED_START_AT
        defaultSvcPlanUnitShouldBeFound("startAt.in=" + DEFAULT_START_AT + "," + UPDATED_START_AT);

        // Get all the svcPlanUnitList where startAt equals to UPDATED_START_AT
        defaultSvcPlanUnitShouldNotBeFound("startAt.in=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStartAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where startAt is not null
        defaultSvcPlanUnitShouldBeFound("startAt.specified=true");

        // Get all the svcPlanUnitList where startAt is null
        defaultSvcPlanUnitShouldNotBeFound("startAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStartAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where startAt is greater than or equal to DEFAULT_START_AT
        defaultSvcPlanUnitShouldBeFound("startAt.greaterThanOrEqual=" + DEFAULT_START_AT);

        // Get all the svcPlanUnitList where startAt is greater than or equal to UPDATED_START_AT
        defaultSvcPlanUnitShouldNotBeFound("startAt.greaterThanOrEqual=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStartAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where startAt is less than or equal to DEFAULT_START_AT
        defaultSvcPlanUnitShouldBeFound("startAt.lessThanOrEqual=" + DEFAULT_START_AT);

        // Get all the svcPlanUnitList where startAt is less than or equal to SMALLER_START_AT
        defaultSvcPlanUnitShouldNotBeFound("startAt.lessThanOrEqual=" + SMALLER_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStartAtIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where startAt is less than DEFAULT_START_AT
        defaultSvcPlanUnitShouldNotBeFound("startAt.lessThan=" + DEFAULT_START_AT);

        // Get all the svcPlanUnitList where startAt is less than UPDATED_START_AT
        defaultSvcPlanUnitShouldBeFound("startAt.lessThan=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStartAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where startAt is greater than DEFAULT_START_AT
        defaultSvcPlanUnitShouldNotBeFound("startAt.greaterThan=" + DEFAULT_START_AT);

        // Get all the svcPlanUnitList where startAt is greater than SMALLER_START_AT
        defaultSvcPlanUnitShouldBeFound("startAt.greaterThan=" + SMALLER_START_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByEndAtIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where endAt equals to DEFAULT_END_AT
        defaultSvcPlanUnitShouldBeFound("endAt.equals=" + DEFAULT_END_AT);

        // Get all the svcPlanUnitList where endAt equals to UPDATED_END_AT
        defaultSvcPlanUnitShouldNotBeFound("endAt.equals=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByEndAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where endAt not equals to DEFAULT_END_AT
        defaultSvcPlanUnitShouldNotBeFound("endAt.notEquals=" + DEFAULT_END_AT);

        // Get all the svcPlanUnitList where endAt not equals to UPDATED_END_AT
        defaultSvcPlanUnitShouldBeFound("endAt.notEquals=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByEndAtIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where endAt in DEFAULT_END_AT or UPDATED_END_AT
        defaultSvcPlanUnitShouldBeFound("endAt.in=" + DEFAULT_END_AT + "," + UPDATED_END_AT);

        // Get all the svcPlanUnitList where endAt equals to UPDATED_END_AT
        defaultSvcPlanUnitShouldNotBeFound("endAt.in=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByEndAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where endAt is not null
        defaultSvcPlanUnitShouldBeFound("endAt.specified=true");

        // Get all the svcPlanUnitList where endAt is null
        defaultSvcPlanUnitShouldNotBeFound("endAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByEndAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where endAt is greater than or equal to DEFAULT_END_AT
        defaultSvcPlanUnitShouldBeFound("endAt.greaterThanOrEqual=" + DEFAULT_END_AT);

        // Get all the svcPlanUnitList where endAt is greater than or equal to UPDATED_END_AT
        defaultSvcPlanUnitShouldNotBeFound("endAt.greaterThanOrEqual=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByEndAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where endAt is less than or equal to DEFAULT_END_AT
        defaultSvcPlanUnitShouldBeFound("endAt.lessThanOrEqual=" + DEFAULT_END_AT);

        // Get all the svcPlanUnitList where endAt is less than or equal to SMALLER_END_AT
        defaultSvcPlanUnitShouldNotBeFound("endAt.lessThanOrEqual=" + SMALLER_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByEndAtIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where endAt is less than DEFAULT_END_AT
        defaultSvcPlanUnitShouldNotBeFound("endAt.lessThan=" + DEFAULT_END_AT);

        // Get all the svcPlanUnitList where endAt is less than UPDATED_END_AT
        defaultSvcPlanUnitShouldBeFound("endAt.lessThan=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByEndAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where endAt is greater than DEFAULT_END_AT
        defaultSvcPlanUnitShouldNotBeFound("endAt.greaterThan=" + DEFAULT_END_AT);

        // Get all the svcPlanUnitList where endAt is greater than SMALLER_END_AT
        defaultSvcPlanUnitShouldBeFound("endAt.greaterThan=" + SMALLER_END_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where createAt equals to DEFAULT_CREATE_AT
        defaultSvcPlanUnitShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the svcPlanUnitList where createAt equals to UPDATED_CREATE_AT
        defaultSvcPlanUnitShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByCreateAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where createAt not equals to DEFAULT_CREATE_AT
        defaultSvcPlanUnitShouldNotBeFound("createAt.notEquals=" + DEFAULT_CREATE_AT);

        // Get all the svcPlanUnitList where createAt not equals to UPDATED_CREATE_AT
        defaultSvcPlanUnitShouldBeFound("createAt.notEquals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultSvcPlanUnitShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the svcPlanUnitList where createAt equals to UPDATED_CREATE_AT
        defaultSvcPlanUnitShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where createAt is not null
        defaultSvcPlanUnitShouldBeFound("createAt.specified=true");

        // Get all the svcPlanUnitList where createAt is null
        defaultSvcPlanUnitShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where createAt is greater than or equal to DEFAULT_CREATE_AT
        defaultSvcPlanUnitShouldBeFound("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the svcPlanUnitList where createAt is greater than or equal to UPDATED_CREATE_AT
        defaultSvcPlanUnitShouldNotBeFound("createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where createAt is less than or equal to DEFAULT_CREATE_AT
        defaultSvcPlanUnitShouldBeFound("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the svcPlanUnitList where createAt is less than or equal to SMALLER_CREATE_AT
        defaultSvcPlanUnitShouldNotBeFound("createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where createAt is less than DEFAULT_CREATE_AT
        defaultSvcPlanUnitShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the svcPlanUnitList where createAt is less than UPDATED_CREATE_AT
        defaultSvcPlanUnitShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where createAt is greater than DEFAULT_CREATE_AT
        defaultSvcPlanUnitShouldNotBeFound("createAt.greaterThan=" + DEFAULT_CREATE_AT);

        // Get all the svcPlanUnitList where createAt is greater than SMALLER_CREATE_AT
        defaultSvcPlanUnitShouldBeFound("createAt.greaterThan=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where status equals to DEFAULT_STATUS
        defaultSvcPlanUnitShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the svcPlanUnitList where status equals to UPDATED_STATUS
        defaultSvcPlanUnitShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where status not equals to DEFAULT_STATUS
        defaultSvcPlanUnitShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the svcPlanUnitList where status not equals to UPDATED_STATUS
        defaultSvcPlanUnitShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSvcPlanUnitShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the svcPlanUnitList where status equals to UPDATED_STATUS
        defaultSvcPlanUnitShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where status is not null
        defaultSvcPlanUnitShouldBeFound("status.specified=true");

        // Get all the svcPlanUnitList where status is null
        defaultSvcPlanUnitShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByAmountOfWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where amountOfWork equals to DEFAULT_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldBeFound("amountOfWork.equals=" + DEFAULT_AMOUNT_OF_WORK);

        // Get all the svcPlanUnitList where amountOfWork equals to UPDATED_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldNotBeFound("amountOfWork.equals=" + UPDATED_AMOUNT_OF_WORK);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByAmountOfWorkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where amountOfWork not equals to DEFAULT_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldNotBeFound("amountOfWork.notEquals=" + DEFAULT_AMOUNT_OF_WORK);

        // Get all the svcPlanUnitList where amountOfWork not equals to UPDATED_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldBeFound("amountOfWork.notEquals=" + UPDATED_AMOUNT_OF_WORK);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByAmountOfWorkIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where amountOfWork in DEFAULT_AMOUNT_OF_WORK or UPDATED_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldBeFound("amountOfWork.in=" + DEFAULT_AMOUNT_OF_WORK + "," + UPDATED_AMOUNT_OF_WORK);

        // Get all the svcPlanUnitList where amountOfWork equals to UPDATED_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldNotBeFound("amountOfWork.in=" + UPDATED_AMOUNT_OF_WORK);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByAmountOfWorkIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where amountOfWork is not null
        defaultSvcPlanUnitShouldBeFound("amountOfWork.specified=true");

        // Get all the svcPlanUnitList where amountOfWork is null
        defaultSvcPlanUnitShouldNotBeFound("amountOfWork.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByAmountOfWorkIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where amountOfWork is greater than or equal to DEFAULT_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldBeFound("amountOfWork.greaterThanOrEqual=" + DEFAULT_AMOUNT_OF_WORK);

        // Get all the svcPlanUnitList where amountOfWork is greater than or equal to UPDATED_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldNotBeFound("amountOfWork.greaterThanOrEqual=" + UPDATED_AMOUNT_OF_WORK);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByAmountOfWorkIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where amountOfWork is less than or equal to DEFAULT_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldBeFound("amountOfWork.lessThanOrEqual=" + DEFAULT_AMOUNT_OF_WORK);

        // Get all the svcPlanUnitList where amountOfWork is less than or equal to SMALLER_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldNotBeFound("amountOfWork.lessThanOrEqual=" + SMALLER_AMOUNT_OF_WORK);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByAmountOfWorkIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where amountOfWork is less than DEFAULT_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldNotBeFound("amountOfWork.lessThan=" + DEFAULT_AMOUNT_OF_WORK);

        // Get all the svcPlanUnitList where amountOfWork is less than UPDATED_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldBeFound("amountOfWork.lessThan=" + UPDATED_AMOUNT_OF_WORK);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByAmountOfWorkIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where amountOfWork is greater than DEFAULT_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldNotBeFound("amountOfWork.greaterThan=" + DEFAULT_AMOUNT_OF_WORK);

        // Get all the svcPlanUnitList where amountOfWork is greater than SMALLER_AMOUNT_OF_WORK
        defaultSvcPlanUnitShouldBeFound("amountOfWork.greaterThan=" + SMALLER_AMOUNT_OF_WORK);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where quantity equals to DEFAULT_QUANTITY
        defaultSvcPlanUnitShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the svcPlanUnitList where quantity equals to UPDATED_QUANTITY
        defaultSvcPlanUnitShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where quantity not equals to DEFAULT_QUANTITY
        defaultSvcPlanUnitShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the svcPlanUnitList where quantity not equals to UPDATED_QUANTITY
        defaultSvcPlanUnitShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultSvcPlanUnitShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the svcPlanUnitList where quantity equals to UPDATED_QUANTITY
        defaultSvcPlanUnitShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where quantity is not null
        defaultSvcPlanUnitShouldBeFound("quantity.specified=true");

        // Get all the svcPlanUnitList where quantity is null
        defaultSvcPlanUnitShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultSvcPlanUnitShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the svcPlanUnitList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultSvcPlanUnitShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultSvcPlanUnitShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the svcPlanUnitList where quantity is less than or equal to SMALLER_QUANTITY
        defaultSvcPlanUnitShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where quantity is less than DEFAULT_QUANTITY
        defaultSvcPlanUnitShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the svcPlanUnitList where quantity is less than UPDATED_QUANTITY
        defaultSvcPlanUnitShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where quantity is greater than DEFAULT_QUANTITY
        defaultSvcPlanUnitShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the svcPlanUnitList where quantity is greater than SMALLER_QUANTITY
        defaultSvcPlanUnitShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where frequency equals to DEFAULT_FREQUENCY
        defaultSvcPlanUnitShouldBeFound("frequency.equals=" + DEFAULT_FREQUENCY);

        // Get all the svcPlanUnitList where frequency equals to UPDATED_FREQUENCY
        defaultSvcPlanUnitShouldNotBeFound("frequency.equals=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByFrequencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where frequency not equals to DEFAULT_FREQUENCY
        defaultSvcPlanUnitShouldNotBeFound("frequency.notEquals=" + DEFAULT_FREQUENCY);

        // Get all the svcPlanUnitList where frequency not equals to UPDATED_FREQUENCY
        defaultSvcPlanUnitShouldBeFound("frequency.notEquals=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where frequency in DEFAULT_FREQUENCY or UPDATED_FREQUENCY
        defaultSvcPlanUnitShouldBeFound("frequency.in=" + DEFAULT_FREQUENCY + "," + UPDATED_FREQUENCY);

        // Get all the svcPlanUnitList where frequency equals to UPDATED_FREQUENCY
        defaultSvcPlanUnitShouldNotBeFound("frequency.in=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where frequency is not null
        defaultSvcPlanUnitShouldBeFound("frequency.specified=true");

        // Get all the svcPlanUnitList where frequency is null
        defaultSvcPlanUnitShouldNotBeFound("frequency.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByFrequencyContainsSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where frequency contains DEFAULT_FREQUENCY
        defaultSvcPlanUnitShouldBeFound("frequency.contains=" + DEFAULT_FREQUENCY);

        // Get all the svcPlanUnitList where frequency contains UPDATED_FREQUENCY
        defaultSvcPlanUnitShouldNotBeFound("frequency.contains=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByFrequencyNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where frequency does not contain DEFAULT_FREQUENCY
        defaultSvcPlanUnitShouldNotBeFound("frequency.doesNotContain=" + DEFAULT_FREQUENCY);

        // Get all the svcPlanUnitList where frequency does not contain UPDATED_FREQUENCY
        defaultSvcPlanUnitShouldBeFound("frequency.doesNotContain=" + UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where note equals to DEFAULT_NOTE
        defaultSvcPlanUnitShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the svcPlanUnitList where note equals to UPDATED_NOTE
        defaultSvcPlanUnitShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where note not equals to DEFAULT_NOTE
        defaultSvcPlanUnitShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the svcPlanUnitList where note not equals to UPDATED_NOTE
        defaultSvcPlanUnitShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultSvcPlanUnitShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the svcPlanUnitList where note equals to UPDATED_NOTE
        defaultSvcPlanUnitShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where note is not null
        defaultSvcPlanUnitShouldBeFound("note.specified=true");

        // Get all the svcPlanUnitList where note is null
        defaultSvcPlanUnitShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByNoteContainsSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where note contains DEFAULT_NOTE
        defaultSvcPlanUnitShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the svcPlanUnitList where note contains UPDATED_NOTE
        defaultSvcPlanUnitShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where note does not contain DEFAULT_NOTE
        defaultSvcPlanUnitShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the svcPlanUnitList where note does not contain UPDATED_NOTE
        defaultSvcPlanUnitShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySuppervisorIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where suppervisorId equals to DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldBeFound("suppervisorId.equals=" + DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanUnitList where suppervisorId equals to UPDATED_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldNotBeFound("suppervisorId.equals=" + UPDATED_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySuppervisorIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where suppervisorId not equals to DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldNotBeFound("suppervisorId.notEquals=" + DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanUnitList where suppervisorId not equals to UPDATED_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldBeFound("suppervisorId.notEquals=" + UPDATED_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySuppervisorIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where suppervisorId in DEFAULT_SUPPERVISOR_ID or UPDATED_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldBeFound("suppervisorId.in=" + DEFAULT_SUPPERVISOR_ID + "," + UPDATED_SUPPERVISOR_ID);

        // Get all the svcPlanUnitList where suppervisorId equals to UPDATED_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldNotBeFound("suppervisorId.in=" + UPDATED_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySuppervisorIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where suppervisorId is not null
        defaultSvcPlanUnitShouldBeFound("suppervisorId.specified=true");

        // Get all the svcPlanUnitList where suppervisorId is null
        defaultSvcPlanUnitShouldNotBeFound("suppervisorId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySuppervisorIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where suppervisorId is greater than or equal to DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldBeFound("suppervisorId.greaterThanOrEqual=" + DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanUnitList where suppervisorId is greater than or equal to UPDATED_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldNotBeFound("suppervisorId.greaterThanOrEqual=" + UPDATED_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySuppervisorIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where suppervisorId is less than or equal to DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldBeFound("suppervisorId.lessThanOrEqual=" + DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanUnitList where suppervisorId is less than or equal to SMALLER_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldNotBeFound("suppervisorId.lessThanOrEqual=" + SMALLER_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySuppervisorIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where suppervisorId is less than DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldNotBeFound("suppervisorId.lessThan=" + DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanUnitList where suppervisorId is less than UPDATED_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldBeFound("suppervisorId.lessThan=" + UPDATED_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySuppervisorIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        // Get all the svcPlanUnitList where suppervisorId is greater than DEFAULT_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldNotBeFound("suppervisorId.greaterThan=" + DEFAULT_SUPPERVISOR_ID);

        // Get all the svcPlanUnitList where suppervisorId is greater than SMALLER_SUPPERVISOR_ID
        defaultSvcPlanUnitShouldBeFound("suppervisorId.greaterThan=" + SMALLER_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySvcLaborIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);
        SvcLabor svcLabor = SvcLaborResourceIT.createEntity(em);
        em.persist(svcLabor);
        em.flush();
        svcPlanUnit.addSvcLabor(svcLabor);
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);
        Long svcLaborId = svcLabor.getId();

        // Get all the svcPlanUnitList where svcLabor equals to svcLaborId
        defaultSvcPlanUnitShouldBeFound("svcLaborId.equals=" + svcLaborId);

        // Get all the svcPlanUnitList where svcLabor equals to (svcLaborId + 1)
        defaultSvcPlanUnitShouldNotBeFound("svcLaborId.equals=" + (svcLaborId + 1));
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySvcPlanTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);
        SvcPlanTask svcPlanTask = SvcPlanTaskResourceIT.createEntity(em);
        em.persist(svcPlanTask);
        em.flush();
        svcPlanUnit.addSvcPlanTask(svcPlanTask);
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);
        Long svcPlanTaskId = svcPlanTask.getId();

        // Get all the svcPlanUnitList where svcPlanTask equals to svcPlanTaskId
        defaultSvcPlanUnitShouldBeFound("svcPlanTaskId.equals=" + svcPlanTaskId);

        // Get all the svcPlanUnitList where svcPlanTask equals to (svcPlanTaskId + 1)
        defaultSvcPlanUnitShouldNotBeFound("svcPlanTaskId.equals=" + (svcPlanTaskId + 1));
    }

    @Test
    @Transactional
    void getAllSvcPlanUnitsBySvcPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);
        SvcPlan svcPlan = SvcPlanResourceIT.createEntity(em);
        em.persist(svcPlan);
        em.flush();
        svcPlanUnit.setSvcPlan(svcPlan);
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);
        Long svcPlanId = svcPlan.getId();

        // Get all the svcPlanUnitList where svcPlan equals to svcPlanId
        defaultSvcPlanUnitShouldBeFound("svcPlanId.equals=" + svcPlanId);

        // Get all the svcPlanUnitList where svcPlan equals to (svcPlanId + 1)
        defaultSvcPlanUnitShouldNotBeFound("svcPlanId.equals=" + (svcPlanId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcPlanUnitShouldBeFound(String filter) throws Exception {
        restSvcPlanUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcPlanUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].amountOfWork").value(hasItem(DEFAULT_AMOUNT_OF_WORK)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].suppervisorId").value(hasItem(DEFAULT_SUPPERVISOR_ID.intValue())));

        // Check, that the count call also returns 1
        restSvcPlanUnitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcPlanUnitShouldNotBeFound(String filter) throws Exception {
        restSvcPlanUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcPlanUnitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcPlanUnit() throws Exception {
        // Get the svcPlanUnit
        restSvcPlanUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcPlanUnit() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();

        // Update the svcPlanUnit
        SvcPlanUnit updatedSvcPlanUnit = svcPlanUnitRepository.findById(svcPlanUnit.getId()).get();
        // Disconnect from session so that the updates on updatedSvcPlanUnit are not directly saved in db
        em.detach(updatedSvcPlanUnit);
        updatedSvcPlanUnit
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .createAt(UPDATED_CREATE_AT)
            .status(UPDATED_STATUS)
            .amountOfWork(UPDATED_AMOUNT_OF_WORK)
            .quantity(UPDATED_QUANTITY)
            .frequency(UPDATED_FREQUENCY)
            .note(UPDATED_NOTE)
            .suppervisorId(UPDATED_SUPPERVISOR_ID);
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(updatedSvcPlanUnit);

        restSvcPlanUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcPlanUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanUnit testSvcPlanUnit = svcPlanUnitList.get(svcPlanUnitList.size() - 1);
        assertThat(testSvcPlanUnit.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testSvcPlanUnit.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testSvcPlanUnit.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testSvcPlanUnit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcPlanUnit.getAmountOfWork()).isEqualTo(UPDATED_AMOUNT_OF_WORK);
        assertThat(testSvcPlanUnit.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSvcPlanUnit.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testSvcPlanUnit.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSvcPlanUnit.getSuppervisorId()).isEqualTo(UPDATED_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingSvcPlanUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();
        svcPlanUnit.setId(count.incrementAndGet());

        // Create the SvcPlanUnit
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(svcPlanUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcPlanUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcPlanUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcPlanUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();
        svcPlanUnit.setId(count.incrementAndGet());

        // Create the SvcPlanUnit
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(svcPlanUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcPlanUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();
        svcPlanUnit.setId(count.incrementAndGet());

        // Create the SvcPlanUnit
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(svcPlanUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcPlanUnitWithPatch() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();

        // Update the svcPlanUnit using partial update
        SvcPlanUnit partialUpdatedSvcPlanUnit = new SvcPlanUnit();
        partialUpdatedSvcPlanUnit.setId(svcPlanUnit.getId());

        partialUpdatedSvcPlanUnit.status(UPDATED_STATUS).frequency(UPDATED_FREQUENCY).suppervisorId(UPDATED_SUPPERVISOR_ID);

        restSvcPlanUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcPlanUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcPlanUnit))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanUnit testSvcPlanUnit = svcPlanUnitList.get(svcPlanUnitList.size() - 1);
        assertThat(testSvcPlanUnit.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testSvcPlanUnit.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testSvcPlanUnit.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testSvcPlanUnit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcPlanUnit.getAmountOfWork()).isEqualTo(DEFAULT_AMOUNT_OF_WORK);
        assertThat(testSvcPlanUnit.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSvcPlanUnit.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testSvcPlanUnit.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSvcPlanUnit.getSuppervisorId()).isEqualTo(UPDATED_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void fullUpdateSvcPlanUnitWithPatch() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();

        // Update the svcPlanUnit using partial update
        SvcPlanUnit partialUpdatedSvcPlanUnit = new SvcPlanUnit();
        partialUpdatedSvcPlanUnit.setId(svcPlanUnit.getId());

        partialUpdatedSvcPlanUnit
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .createAt(UPDATED_CREATE_AT)
            .status(UPDATED_STATUS)
            .amountOfWork(UPDATED_AMOUNT_OF_WORK)
            .quantity(UPDATED_QUANTITY)
            .frequency(UPDATED_FREQUENCY)
            .note(UPDATED_NOTE)
            .suppervisorId(UPDATED_SUPPERVISOR_ID);

        restSvcPlanUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcPlanUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcPlanUnit))
            )
            .andExpect(status().isOk());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcPlanUnit testSvcPlanUnit = svcPlanUnitList.get(svcPlanUnitList.size() - 1);
        assertThat(testSvcPlanUnit.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testSvcPlanUnit.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testSvcPlanUnit.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testSvcPlanUnit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcPlanUnit.getAmountOfWork()).isEqualTo(UPDATED_AMOUNT_OF_WORK);
        assertThat(testSvcPlanUnit.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSvcPlanUnit.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testSvcPlanUnit.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSvcPlanUnit.getSuppervisorId()).isEqualTo(UPDATED_SUPPERVISOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSvcPlanUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();
        svcPlanUnit.setId(count.incrementAndGet());

        // Create the SvcPlanUnit
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(svcPlanUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcPlanUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcPlanUnitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcPlanUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();
        svcPlanUnit.setId(count.incrementAndGet());

        // Create the SvcPlanUnit
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(svcPlanUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcPlanUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcPlanUnitRepository.findAll().size();
        svcPlanUnit.setId(count.incrementAndGet());

        // Create the SvcPlanUnit
        SvcPlanUnitDTO svcPlanUnitDTO = svcPlanUnitMapper.toDto(svcPlanUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcPlanUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcPlanUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcPlanUnit in the database
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcPlanUnit() throws Exception {
        // Initialize the database
        svcPlanUnitRepository.saveAndFlush(svcPlanUnit);

        int databaseSizeBeforeDelete = svcPlanUnitRepository.findAll().size();

        // Delete the svcPlanUnit
        restSvcPlanUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcPlanUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcPlanUnit> svcPlanUnitList = svcPlanUnitRepository.findAll();
        assertThat(svcPlanUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
