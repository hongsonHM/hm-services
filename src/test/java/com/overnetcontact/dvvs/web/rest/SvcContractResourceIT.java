package com.overnetcontact.dvvs.web.rest;

import static com.overnetcontact.dvvs.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.domain.SvcClient;
import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.domain.SvcTarget;
import com.overnetcontact.dvvs.domain.SvcUnit;
import com.overnetcontact.dvvs.domain.User;
import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
import com.overnetcontact.dvvs.service.criteria.SvcContractCriteria;
import com.overnetcontact.dvvs.service.dto.SvcContractDTO;
import com.overnetcontact.dvvs.service.mapper.SvcContractMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SvcContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcContractResourceIT {

    private static final Long DEFAULT_ORDER_NUMBER = 1L;
    private static final Long UPDATED_ORDER_NUMBER = 2L;
    private static final Long SMALLER_ORDER_NUMBER = 1L - 1L;

    private static final String DEFAULT_DOCUMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_APPENDICES_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_APPENDICES_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_ID = "AAAAAAAAAA";
    private static final String UPDATED_FILE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_EFFECTIVE_TIME_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_TIME_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EFFECTIVE_TIME_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_TIME_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DURATION_MONTH = 1;
    private static final Integer UPDATED_DURATION_MONTH = 2;
    private static final Integer SMALLER_DURATION_MONTH = 1 - 1;

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CONTRACT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONTRACT_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CONTRACT_VALUE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_HUMAN_RESOURCES = 1;
    private static final Integer UPDATED_HUMAN_RESOURCES = 2;
    private static final Integer SMALLER_HUMAN_RESOURCES = 1 - 1;

    private static final Integer DEFAULT_HUMAN_RESOURCES_WEEKEND = 1;
    private static final Integer UPDATED_HUMAN_RESOURCES_WEEKEND = 2;
    private static final Integer SMALLER_HUMAN_RESOURCES_WEEKEND = 1 - 1;

    private static final SvcContractStatus DEFAULT_STATUS = SvcContractStatus.SUCCESS;
    private static final SvcContractStatus UPDATED_STATUS = SvcContractStatus.PENDING;

    private static final Long DEFAULT_SUBJECT_COUNT = 1L;
    private static final Long UPDATED_SUBJECT_COUNT = 2L;
    private static final Long SMALLER_SUBJECT_COUNT = 1L - 1L;

    private static final BigDecimal DEFAULT_VALUE_PER_PERSON = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE_PER_PERSON = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE_PER_PERSON = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final Integer SMALLER_YEAR = 1 - 1;

    private static final String ENTITY_API_URL = "/api/svc-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcContractRepository svcContractRepository;

    @Autowired
    private SvcContractMapper svcContractMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcContractMockMvc;

    private SvcContract svcContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcContract createEntity(EntityManager em) {
        SvcContract svcContract = new SvcContract()
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .documentId(DEFAULT_DOCUMENT_ID)
            .appendicesNumber(DEFAULT_APPENDICES_NUMBER)
            .fileId(DEFAULT_FILE_ID)
            .content(DEFAULT_CONTENT)
            .effectiveTimeFrom(DEFAULT_EFFECTIVE_TIME_FROM)
            .effectiveTimeTo(DEFAULT_EFFECTIVE_TIME_TO)
            .durationMonth(DEFAULT_DURATION_MONTH)
            .value(DEFAULT_VALUE)
            .contractValue(DEFAULT_CONTRACT_VALUE)
            .humanResources(DEFAULT_HUMAN_RESOURCES)
            .humanResourcesWeekend(DEFAULT_HUMAN_RESOURCES_WEEKEND)
            .status(DEFAULT_STATUS)
            .subjectCount(DEFAULT_SUBJECT_COUNT)
            .valuePerPerson(DEFAULT_VALUE_PER_PERSON)
            .year(DEFAULT_YEAR);
        return svcContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcContract createUpdatedEntity(EntityManager em) {
        SvcContract svcContract = new SvcContract()
            .orderNumber(UPDATED_ORDER_NUMBER)
            .documentId(UPDATED_DOCUMENT_ID)
            .appendicesNumber(UPDATED_APPENDICES_NUMBER)
            .fileId(UPDATED_FILE_ID)
            .content(UPDATED_CONTENT)
            .effectiveTimeFrom(UPDATED_EFFECTIVE_TIME_FROM)
            .effectiveTimeTo(UPDATED_EFFECTIVE_TIME_TO)
            .durationMonth(UPDATED_DURATION_MONTH)
            .value(UPDATED_VALUE)
            .contractValue(UPDATED_CONTRACT_VALUE)
            .humanResources(UPDATED_HUMAN_RESOURCES)
            .humanResourcesWeekend(UPDATED_HUMAN_RESOURCES_WEEKEND)
            .status(UPDATED_STATUS)
            .subjectCount(UPDATED_SUBJECT_COUNT)
            .valuePerPerson(UPDATED_VALUE_PER_PERSON)
            .year(UPDATED_YEAR);
        return svcContract;
    }

    @BeforeEach
    public void initTest() {
        svcContract = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcContract() throws Exception {
        int databaseSizeBeforeCreate = svcContractRepository.findAll().size();
        // Create the SvcContract
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);
        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeCreate + 1);
        SvcContract testSvcContract = svcContractList.get(svcContractList.size() - 1);
        assertThat(testSvcContract.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testSvcContract.getDocumentId()).isEqualTo(DEFAULT_DOCUMENT_ID);
        assertThat(testSvcContract.getAppendicesNumber()).isEqualTo(DEFAULT_APPENDICES_NUMBER);
        assertThat(testSvcContract.getFileId()).isEqualTo(DEFAULT_FILE_ID);
        assertThat(testSvcContract.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSvcContract.getEffectiveTimeFrom()).isEqualTo(DEFAULT_EFFECTIVE_TIME_FROM);
        assertThat(testSvcContract.getEffectiveTimeTo()).isEqualTo(DEFAULT_EFFECTIVE_TIME_TO);
        assertThat(testSvcContract.getDurationMonth()).isEqualTo(DEFAULT_DURATION_MONTH);
        assertThat(testSvcContract.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testSvcContract.getContractValue()).isEqualByComparingTo(DEFAULT_CONTRACT_VALUE);
        assertThat(testSvcContract.getHumanResources()).isEqualTo(DEFAULT_HUMAN_RESOURCES);
        assertThat(testSvcContract.getHumanResourcesWeekend()).isEqualTo(DEFAULT_HUMAN_RESOURCES_WEEKEND);
        assertThat(testSvcContract.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSvcContract.getSubjectCount()).isEqualTo(DEFAULT_SUBJECT_COUNT);
        assertThat(testSvcContract.getValuePerPerson()).isEqualByComparingTo(DEFAULT_VALUE_PER_PERSON);
        assertThat(testSvcContract.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void createSvcContractWithExistingId() throws Exception {
        // Create the SvcContract with an existing ID
        svcContract.setId(1L);
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        int databaseSizeBeforeCreate = svcContractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setOrderNumber(null);

        // Create the SvcContract, which fails.
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEffectiveTimeFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setEffectiveTimeFrom(null);

        // Create the SvcContract, which fails.
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEffectiveTimeToIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setEffectiveTimeTo(null);

        // Create the SvcContract, which fails.
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setDurationMonth(null);

        // Create the SvcContract, which fails.
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setValue(null);

        // Create the SvcContract, which fails.
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setContractValue(null);

        // Create the SvcContract, which fails.
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setStatus(null);

        // Create the SvcContract, which fails.
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        restSvcContractMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcContracts() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList
        restSvcContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID)))
            .andExpect(jsonPath("$.[*].appendicesNumber").value(hasItem(DEFAULT_APPENDICES_NUMBER)))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].effectiveTimeFrom").value(hasItem(DEFAULT_EFFECTIVE_TIME_FROM.toString())))
            .andExpect(jsonPath("$.[*].effectiveTimeTo").value(hasItem(DEFAULT_EFFECTIVE_TIME_TO.toString())))
            .andExpect(jsonPath("$.[*].durationMonth").value(hasItem(DEFAULT_DURATION_MONTH)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].contractValue").value(hasItem(sameNumber(DEFAULT_CONTRACT_VALUE))))
            .andExpect(jsonPath("$.[*].humanResources").value(hasItem(DEFAULT_HUMAN_RESOURCES)))
            .andExpect(jsonPath("$.[*].humanResourcesWeekend").value(hasItem(DEFAULT_HUMAN_RESOURCES_WEEKEND)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].subjectCount").value(hasItem(DEFAULT_SUBJECT_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].valuePerPerson").value(hasItem(sameNumber(DEFAULT_VALUE_PER_PERSON))))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getSvcContract() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get the svcContract
        restSvcContractMockMvc
            .perform(get(ENTITY_API_URL_ID, svcContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcContract.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID))
            .andExpect(jsonPath("$.appendicesNumber").value(DEFAULT_APPENDICES_NUMBER))
            .andExpect(jsonPath("$.fileId").value(DEFAULT_FILE_ID))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.effectiveTimeFrom").value(DEFAULT_EFFECTIVE_TIME_FROM.toString()))
            .andExpect(jsonPath("$.effectiveTimeTo").value(DEFAULT_EFFECTIVE_TIME_TO.toString()))
            .andExpect(jsonPath("$.durationMonth").value(DEFAULT_DURATION_MONTH))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.contractValue").value(sameNumber(DEFAULT_CONTRACT_VALUE)))
            .andExpect(jsonPath("$.humanResources").value(DEFAULT_HUMAN_RESOURCES))
            .andExpect(jsonPath("$.humanResourcesWeekend").value(DEFAULT_HUMAN_RESOURCES_WEEKEND))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.subjectCount").value(DEFAULT_SUBJECT_COUNT.intValue()))
            .andExpect(jsonPath("$.valuePerPerson").value(sameNumber(DEFAULT_VALUE_PER_PERSON)))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getSvcContractsByIdFiltering() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        Long id = svcContract.getId();

        defaultSvcContractShouldBeFound("id.equals=" + id);
        defaultSvcContractShouldNotBeFound("id.notEquals=" + id);

        defaultSvcContractShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcContractShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcContractShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcContractShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcContractsByOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where orderNumber equals to DEFAULT_ORDER_NUMBER
        defaultSvcContractShouldBeFound("orderNumber.equals=" + DEFAULT_ORDER_NUMBER);

        // Get all the svcContractList where orderNumber equals to UPDATED_ORDER_NUMBER
        defaultSvcContractShouldNotBeFound("orderNumber.equals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByOrderNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where orderNumber not equals to DEFAULT_ORDER_NUMBER
        defaultSvcContractShouldNotBeFound("orderNumber.notEquals=" + DEFAULT_ORDER_NUMBER);

        // Get all the svcContractList where orderNumber not equals to UPDATED_ORDER_NUMBER
        defaultSvcContractShouldBeFound("orderNumber.notEquals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where orderNumber in DEFAULT_ORDER_NUMBER or UPDATED_ORDER_NUMBER
        defaultSvcContractShouldBeFound("orderNumber.in=" + DEFAULT_ORDER_NUMBER + "," + UPDATED_ORDER_NUMBER);

        // Get all the svcContractList where orderNumber equals to UPDATED_ORDER_NUMBER
        defaultSvcContractShouldNotBeFound("orderNumber.in=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where orderNumber is not null
        defaultSvcContractShouldBeFound("orderNumber.specified=true");

        // Get all the svcContractList where orderNumber is null
        defaultSvcContractShouldNotBeFound("orderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByOrderNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where orderNumber is greater than or equal to DEFAULT_ORDER_NUMBER
        defaultSvcContractShouldBeFound("orderNumber.greaterThanOrEqual=" + DEFAULT_ORDER_NUMBER);

        // Get all the svcContractList where orderNumber is greater than or equal to UPDATED_ORDER_NUMBER
        defaultSvcContractShouldNotBeFound("orderNumber.greaterThanOrEqual=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByOrderNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where orderNumber is less than or equal to DEFAULT_ORDER_NUMBER
        defaultSvcContractShouldBeFound("orderNumber.lessThanOrEqual=" + DEFAULT_ORDER_NUMBER);

        // Get all the svcContractList where orderNumber is less than or equal to SMALLER_ORDER_NUMBER
        defaultSvcContractShouldNotBeFound("orderNumber.lessThanOrEqual=" + SMALLER_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByOrderNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where orderNumber is less than DEFAULT_ORDER_NUMBER
        defaultSvcContractShouldNotBeFound("orderNumber.lessThan=" + DEFAULT_ORDER_NUMBER);

        // Get all the svcContractList where orderNumber is less than UPDATED_ORDER_NUMBER
        defaultSvcContractShouldBeFound("orderNumber.lessThan=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByOrderNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where orderNumber is greater than DEFAULT_ORDER_NUMBER
        defaultSvcContractShouldNotBeFound("orderNumber.greaterThan=" + DEFAULT_ORDER_NUMBER);

        // Get all the svcContractList where orderNumber is greater than SMALLER_ORDER_NUMBER
        defaultSvcContractShouldBeFound("orderNumber.greaterThan=" + SMALLER_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where documentId equals to DEFAULT_DOCUMENT_ID
        defaultSvcContractShouldBeFound("documentId.equals=" + DEFAULT_DOCUMENT_ID);

        // Get all the svcContractList where documentId equals to UPDATED_DOCUMENT_ID
        defaultSvcContractShouldNotBeFound("documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDocumentIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where documentId not equals to DEFAULT_DOCUMENT_ID
        defaultSvcContractShouldNotBeFound("documentId.notEquals=" + DEFAULT_DOCUMENT_ID);

        // Get all the svcContractList where documentId not equals to UPDATED_DOCUMENT_ID
        defaultSvcContractShouldBeFound("documentId.notEquals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where documentId in DEFAULT_DOCUMENT_ID or UPDATED_DOCUMENT_ID
        defaultSvcContractShouldBeFound("documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID);

        // Get all the svcContractList where documentId equals to UPDATED_DOCUMENT_ID
        defaultSvcContractShouldNotBeFound("documentId.in=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where documentId is not null
        defaultSvcContractShouldBeFound("documentId.specified=true");

        // Get all the svcContractList where documentId is null
        defaultSvcContractShouldNotBeFound("documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByDocumentIdContainsSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where documentId contains DEFAULT_DOCUMENT_ID
        defaultSvcContractShouldBeFound("documentId.contains=" + DEFAULT_DOCUMENT_ID);

        // Get all the svcContractList where documentId contains UPDATED_DOCUMENT_ID
        defaultSvcContractShouldNotBeFound("documentId.contains=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDocumentIdNotContainsSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where documentId does not contain DEFAULT_DOCUMENT_ID
        defaultSvcContractShouldNotBeFound("documentId.doesNotContain=" + DEFAULT_DOCUMENT_ID);

        // Get all the svcContractList where documentId does not contain UPDATED_DOCUMENT_ID
        defaultSvcContractShouldBeFound("documentId.doesNotContain=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByAppendicesNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where appendicesNumber equals to DEFAULT_APPENDICES_NUMBER
        defaultSvcContractShouldBeFound("appendicesNumber.equals=" + DEFAULT_APPENDICES_NUMBER);

        // Get all the svcContractList where appendicesNumber equals to UPDATED_APPENDICES_NUMBER
        defaultSvcContractShouldNotBeFound("appendicesNumber.equals=" + UPDATED_APPENDICES_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByAppendicesNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where appendicesNumber not equals to DEFAULT_APPENDICES_NUMBER
        defaultSvcContractShouldNotBeFound("appendicesNumber.notEquals=" + DEFAULT_APPENDICES_NUMBER);

        // Get all the svcContractList where appendicesNumber not equals to UPDATED_APPENDICES_NUMBER
        defaultSvcContractShouldBeFound("appendicesNumber.notEquals=" + UPDATED_APPENDICES_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByAppendicesNumberIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where appendicesNumber in DEFAULT_APPENDICES_NUMBER or UPDATED_APPENDICES_NUMBER
        defaultSvcContractShouldBeFound("appendicesNumber.in=" + DEFAULT_APPENDICES_NUMBER + "," + UPDATED_APPENDICES_NUMBER);

        // Get all the svcContractList where appendicesNumber equals to UPDATED_APPENDICES_NUMBER
        defaultSvcContractShouldNotBeFound("appendicesNumber.in=" + UPDATED_APPENDICES_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByAppendicesNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where appendicesNumber is not null
        defaultSvcContractShouldBeFound("appendicesNumber.specified=true");

        // Get all the svcContractList where appendicesNumber is null
        defaultSvcContractShouldNotBeFound("appendicesNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByAppendicesNumberContainsSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where appendicesNumber contains DEFAULT_APPENDICES_NUMBER
        defaultSvcContractShouldBeFound("appendicesNumber.contains=" + DEFAULT_APPENDICES_NUMBER);

        // Get all the svcContractList where appendicesNumber contains UPDATED_APPENDICES_NUMBER
        defaultSvcContractShouldNotBeFound("appendicesNumber.contains=" + UPDATED_APPENDICES_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByAppendicesNumberNotContainsSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where appendicesNumber does not contain DEFAULT_APPENDICES_NUMBER
        defaultSvcContractShouldNotBeFound("appendicesNumber.doesNotContain=" + DEFAULT_APPENDICES_NUMBER);

        // Get all the svcContractList where appendicesNumber does not contain UPDATED_APPENDICES_NUMBER
        defaultSvcContractShouldBeFound("appendicesNumber.doesNotContain=" + UPDATED_APPENDICES_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcContractsByFileIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where fileId equals to DEFAULT_FILE_ID
        defaultSvcContractShouldBeFound("fileId.equals=" + DEFAULT_FILE_ID);

        // Get all the svcContractList where fileId equals to UPDATED_FILE_ID
        defaultSvcContractShouldNotBeFound("fileId.equals=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByFileIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where fileId not equals to DEFAULT_FILE_ID
        defaultSvcContractShouldNotBeFound("fileId.notEquals=" + DEFAULT_FILE_ID);

        // Get all the svcContractList where fileId not equals to UPDATED_FILE_ID
        defaultSvcContractShouldBeFound("fileId.notEquals=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByFileIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where fileId in DEFAULT_FILE_ID or UPDATED_FILE_ID
        defaultSvcContractShouldBeFound("fileId.in=" + DEFAULT_FILE_ID + "," + UPDATED_FILE_ID);

        // Get all the svcContractList where fileId equals to UPDATED_FILE_ID
        defaultSvcContractShouldNotBeFound("fileId.in=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByFileIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where fileId is not null
        defaultSvcContractShouldBeFound("fileId.specified=true");

        // Get all the svcContractList where fileId is null
        defaultSvcContractShouldNotBeFound("fileId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByFileIdContainsSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where fileId contains DEFAULT_FILE_ID
        defaultSvcContractShouldBeFound("fileId.contains=" + DEFAULT_FILE_ID);

        // Get all the svcContractList where fileId contains UPDATED_FILE_ID
        defaultSvcContractShouldNotBeFound("fileId.contains=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByFileIdNotContainsSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where fileId does not contain DEFAULT_FILE_ID
        defaultSvcContractShouldNotBeFound("fileId.doesNotContain=" + DEFAULT_FILE_ID);

        // Get all the svcContractList where fileId does not contain UPDATED_FILE_ID
        defaultSvcContractShouldBeFound("fileId.doesNotContain=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where content equals to DEFAULT_CONTENT
        defaultSvcContractShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the svcContractList where content equals to UPDATED_CONTENT
        defaultSvcContractShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where content not equals to DEFAULT_CONTENT
        defaultSvcContractShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the svcContractList where content not equals to UPDATED_CONTENT
        defaultSvcContractShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultSvcContractShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the svcContractList where content equals to UPDATED_CONTENT
        defaultSvcContractShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where content is not null
        defaultSvcContractShouldBeFound("content.specified=true");

        // Get all the svcContractList where content is null
        defaultSvcContractShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByContentContainsSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where content contains DEFAULT_CONTENT
        defaultSvcContractShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the svcContractList where content contains UPDATED_CONTENT
        defaultSvcContractShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where content does not contain DEFAULT_CONTENT
        defaultSvcContractShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the svcContractList where content does not contain UPDATED_CONTENT
        defaultSvcContractShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSvcContractsByEffectiveTimeFromIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where effectiveTimeFrom equals to DEFAULT_EFFECTIVE_TIME_FROM
        defaultSvcContractShouldBeFound("effectiveTimeFrom.equals=" + DEFAULT_EFFECTIVE_TIME_FROM);

        // Get all the svcContractList where effectiveTimeFrom equals to UPDATED_EFFECTIVE_TIME_FROM
        defaultSvcContractShouldNotBeFound("effectiveTimeFrom.equals=" + UPDATED_EFFECTIVE_TIME_FROM);
    }

    @Test
    @Transactional
    void getAllSvcContractsByEffectiveTimeFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where effectiveTimeFrom not equals to DEFAULT_EFFECTIVE_TIME_FROM
        defaultSvcContractShouldNotBeFound("effectiveTimeFrom.notEquals=" + DEFAULT_EFFECTIVE_TIME_FROM);

        // Get all the svcContractList where effectiveTimeFrom not equals to UPDATED_EFFECTIVE_TIME_FROM
        defaultSvcContractShouldBeFound("effectiveTimeFrom.notEquals=" + UPDATED_EFFECTIVE_TIME_FROM);
    }

    @Test
    @Transactional
    void getAllSvcContractsByEffectiveTimeFromIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where effectiveTimeFrom in DEFAULT_EFFECTIVE_TIME_FROM or UPDATED_EFFECTIVE_TIME_FROM
        defaultSvcContractShouldBeFound("effectiveTimeFrom.in=" + DEFAULT_EFFECTIVE_TIME_FROM + "," + UPDATED_EFFECTIVE_TIME_FROM);

        // Get all the svcContractList where effectiveTimeFrom equals to UPDATED_EFFECTIVE_TIME_FROM
        defaultSvcContractShouldNotBeFound("effectiveTimeFrom.in=" + UPDATED_EFFECTIVE_TIME_FROM);
    }

    @Test
    @Transactional
    void getAllSvcContractsByEffectiveTimeFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where effectiveTimeFrom is not null
        defaultSvcContractShouldBeFound("effectiveTimeFrom.specified=true");

        // Get all the svcContractList where effectiveTimeFrom is null
        defaultSvcContractShouldNotBeFound("effectiveTimeFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByEffectiveTimeToIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where effectiveTimeTo equals to DEFAULT_EFFECTIVE_TIME_TO
        defaultSvcContractShouldBeFound("effectiveTimeTo.equals=" + DEFAULT_EFFECTIVE_TIME_TO);

        // Get all the svcContractList where effectiveTimeTo equals to UPDATED_EFFECTIVE_TIME_TO
        defaultSvcContractShouldNotBeFound("effectiveTimeTo.equals=" + UPDATED_EFFECTIVE_TIME_TO);
    }

    @Test
    @Transactional
    void getAllSvcContractsByEffectiveTimeToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where effectiveTimeTo not equals to DEFAULT_EFFECTIVE_TIME_TO
        defaultSvcContractShouldNotBeFound("effectiveTimeTo.notEquals=" + DEFAULT_EFFECTIVE_TIME_TO);

        // Get all the svcContractList where effectiveTimeTo not equals to UPDATED_EFFECTIVE_TIME_TO
        defaultSvcContractShouldBeFound("effectiveTimeTo.notEquals=" + UPDATED_EFFECTIVE_TIME_TO);
    }

    @Test
    @Transactional
    void getAllSvcContractsByEffectiveTimeToIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where effectiveTimeTo in DEFAULT_EFFECTIVE_TIME_TO or UPDATED_EFFECTIVE_TIME_TO
        defaultSvcContractShouldBeFound("effectiveTimeTo.in=" + DEFAULT_EFFECTIVE_TIME_TO + "," + UPDATED_EFFECTIVE_TIME_TO);

        // Get all the svcContractList where effectiveTimeTo equals to UPDATED_EFFECTIVE_TIME_TO
        defaultSvcContractShouldNotBeFound("effectiveTimeTo.in=" + UPDATED_EFFECTIVE_TIME_TO);
    }

    @Test
    @Transactional
    void getAllSvcContractsByEffectiveTimeToIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where effectiveTimeTo is not null
        defaultSvcContractShouldBeFound("effectiveTimeTo.specified=true");

        // Get all the svcContractList where effectiveTimeTo is null
        defaultSvcContractShouldNotBeFound("effectiveTimeTo.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByDurationMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where durationMonth equals to DEFAULT_DURATION_MONTH
        defaultSvcContractShouldBeFound("durationMonth.equals=" + DEFAULT_DURATION_MONTH);

        // Get all the svcContractList where durationMonth equals to UPDATED_DURATION_MONTH
        defaultSvcContractShouldNotBeFound("durationMonth.equals=" + UPDATED_DURATION_MONTH);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDurationMonthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where durationMonth not equals to DEFAULT_DURATION_MONTH
        defaultSvcContractShouldNotBeFound("durationMonth.notEquals=" + DEFAULT_DURATION_MONTH);

        // Get all the svcContractList where durationMonth not equals to UPDATED_DURATION_MONTH
        defaultSvcContractShouldBeFound("durationMonth.notEquals=" + UPDATED_DURATION_MONTH);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDurationMonthIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where durationMonth in DEFAULT_DURATION_MONTH or UPDATED_DURATION_MONTH
        defaultSvcContractShouldBeFound("durationMonth.in=" + DEFAULT_DURATION_MONTH + "," + UPDATED_DURATION_MONTH);

        // Get all the svcContractList where durationMonth equals to UPDATED_DURATION_MONTH
        defaultSvcContractShouldNotBeFound("durationMonth.in=" + UPDATED_DURATION_MONTH);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDurationMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where durationMonth is not null
        defaultSvcContractShouldBeFound("durationMonth.specified=true");

        // Get all the svcContractList where durationMonth is null
        defaultSvcContractShouldNotBeFound("durationMonth.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByDurationMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where durationMonth is greater than or equal to DEFAULT_DURATION_MONTH
        defaultSvcContractShouldBeFound("durationMonth.greaterThanOrEqual=" + DEFAULT_DURATION_MONTH);

        // Get all the svcContractList where durationMonth is greater than or equal to UPDATED_DURATION_MONTH
        defaultSvcContractShouldNotBeFound("durationMonth.greaterThanOrEqual=" + UPDATED_DURATION_MONTH);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDurationMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where durationMonth is less than or equal to DEFAULT_DURATION_MONTH
        defaultSvcContractShouldBeFound("durationMonth.lessThanOrEqual=" + DEFAULT_DURATION_MONTH);

        // Get all the svcContractList where durationMonth is less than or equal to SMALLER_DURATION_MONTH
        defaultSvcContractShouldNotBeFound("durationMonth.lessThanOrEqual=" + SMALLER_DURATION_MONTH);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDurationMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where durationMonth is less than DEFAULT_DURATION_MONTH
        defaultSvcContractShouldNotBeFound("durationMonth.lessThan=" + DEFAULT_DURATION_MONTH);

        // Get all the svcContractList where durationMonth is less than UPDATED_DURATION_MONTH
        defaultSvcContractShouldBeFound("durationMonth.lessThan=" + UPDATED_DURATION_MONTH);
    }

    @Test
    @Transactional
    void getAllSvcContractsByDurationMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where durationMonth is greater than DEFAULT_DURATION_MONTH
        defaultSvcContractShouldNotBeFound("durationMonth.greaterThan=" + DEFAULT_DURATION_MONTH);

        // Get all the svcContractList where durationMonth is greater than SMALLER_DURATION_MONTH
        defaultSvcContractShouldBeFound("durationMonth.greaterThan=" + SMALLER_DURATION_MONTH);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where value equals to DEFAULT_VALUE
        defaultSvcContractShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the svcContractList where value equals to UPDATED_VALUE
        defaultSvcContractShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where value not equals to DEFAULT_VALUE
        defaultSvcContractShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the svcContractList where value not equals to UPDATED_VALUE
        defaultSvcContractShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultSvcContractShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the svcContractList where value equals to UPDATED_VALUE
        defaultSvcContractShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where value is not null
        defaultSvcContractShouldBeFound("value.specified=true");

        // Get all the svcContractList where value is null
        defaultSvcContractShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where value is greater than or equal to DEFAULT_VALUE
        defaultSvcContractShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the svcContractList where value is greater than or equal to UPDATED_VALUE
        defaultSvcContractShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where value is less than or equal to DEFAULT_VALUE
        defaultSvcContractShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the svcContractList where value is less than or equal to SMALLER_VALUE
        defaultSvcContractShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where value is less than DEFAULT_VALUE
        defaultSvcContractShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the svcContractList where value is less than UPDATED_VALUE
        defaultSvcContractShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where value is greater than DEFAULT_VALUE
        defaultSvcContractShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the svcContractList where value is greater than SMALLER_VALUE
        defaultSvcContractShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContractValueIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where contractValue equals to DEFAULT_CONTRACT_VALUE
        defaultSvcContractShouldBeFound("contractValue.equals=" + DEFAULT_CONTRACT_VALUE);

        // Get all the svcContractList where contractValue equals to UPDATED_CONTRACT_VALUE
        defaultSvcContractShouldNotBeFound("contractValue.equals=" + UPDATED_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContractValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where contractValue not equals to DEFAULT_CONTRACT_VALUE
        defaultSvcContractShouldNotBeFound("contractValue.notEquals=" + DEFAULT_CONTRACT_VALUE);

        // Get all the svcContractList where contractValue not equals to UPDATED_CONTRACT_VALUE
        defaultSvcContractShouldBeFound("contractValue.notEquals=" + UPDATED_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContractValueIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where contractValue in DEFAULT_CONTRACT_VALUE or UPDATED_CONTRACT_VALUE
        defaultSvcContractShouldBeFound("contractValue.in=" + DEFAULT_CONTRACT_VALUE + "," + UPDATED_CONTRACT_VALUE);

        // Get all the svcContractList where contractValue equals to UPDATED_CONTRACT_VALUE
        defaultSvcContractShouldNotBeFound("contractValue.in=" + UPDATED_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContractValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where contractValue is not null
        defaultSvcContractShouldBeFound("contractValue.specified=true");

        // Get all the svcContractList where contractValue is null
        defaultSvcContractShouldNotBeFound("contractValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByContractValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where contractValue is greater than or equal to DEFAULT_CONTRACT_VALUE
        defaultSvcContractShouldBeFound("contractValue.greaterThanOrEqual=" + DEFAULT_CONTRACT_VALUE);

        // Get all the svcContractList where contractValue is greater than or equal to UPDATED_CONTRACT_VALUE
        defaultSvcContractShouldNotBeFound("contractValue.greaterThanOrEqual=" + UPDATED_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContractValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where contractValue is less than or equal to DEFAULT_CONTRACT_VALUE
        defaultSvcContractShouldBeFound("contractValue.lessThanOrEqual=" + DEFAULT_CONTRACT_VALUE);

        // Get all the svcContractList where contractValue is less than or equal to SMALLER_CONTRACT_VALUE
        defaultSvcContractShouldNotBeFound("contractValue.lessThanOrEqual=" + SMALLER_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContractValueIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where contractValue is less than DEFAULT_CONTRACT_VALUE
        defaultSvcContractShouldNotBeFound("contractValue.lessThan=" + DEFAULT_CONTRACT_VALUE);

        // Get all the svcContractList where contractValue is less than UPDATED_CONTRACT_VALUE
        defaultSvcContractShouldBeFound("contractValue.lessThan=" + UPDATED_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByContractValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where contractValue is greater than DEFAULT_CONTRACT_VALUE
        defaultSvcContractShouldNotBeFound("contractValue.greaterThan=" + DEFAULT_CONTRACT_VALUE);

        // Get all the svcContractList where contractValue is greater than SMALLER_CONTRACT_VALUE
        defaultSvcContractShouldBeFound("contractValue.greaterThan=" + SMALLER_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResources equals to DEFAULT_HUMAN_RESOURCES
        defaultSvcContractShouldBeFound("humanResources.equals=" + DEFAULT_HUMAN_RESOURCES);

        // Get all the svcContractList where humanResources equals to UPDATED_HUMAN_RESOURCES
        defaultSvcContractShouldNotBeFound("humanResources.equals=" + UPDATED_HUMAN_RESOURCES);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResources not equals to DEFAULT_HUMAN_RESOURCES
        defaultSvcContractShouldNotBeFound("humanResources.notEquals=" + DEFAULT_HUMAN_RESOURCES);

        // Get all the svcContractList where humanResources not equals to UPDATED_HUMAN_RESOURCES
        defaultSvcContractShouldBeFound("humanResources.notEquals=" + UPDATED_HUMAN_RESOURCES);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResources in DEFAULT_HUMAN_RESOURCES or UPDATED_HUMAN_RESOURCES
        defaultSvcContractShouldBeFound("humanResources.in=" + DEFAULT_HUMAN_RESOURCES + "," + UPDATED_HUMAN_RESOURCES);

        // Get all the svcContractList where humanResources equals to UPDATED_HUMAN_RESOURCES
        defaultSvcContractShouldNotBeFound("humanResources.in=" + UPDATED_HUMAN_RESOURCES);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResources is not null
        defaultSvcContractShouldBeFound("humanResources.specified=true");

        // Get all the svcContractList where humanResources is null
        defaultSvcContractShouldNotBeFound("humanResources.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResources is greater than or equal to DEFAULT_HUMAN_RESOURCES
        defaultSvcContractShouldBeFound("humanResources.greaterThanOrEqual=" + DEFAULT_HUMAN_RESOURCES);

        // Get all the svcContractList where humanResources is greater than or equal to UPDATED_HUMAN_RESOURCES
        defaultSvcContractShouldNotBeFound("humanResources.greaterThanOrEqual=" + UPDATED_HUMAN_RESOURCES);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResources is less than or equal to DEFAULT_HUMAN_RESOURCES
        defaultSvcContractShouldBeFound("humanResources.lessThanOrEqual=" + DEFAULT_HUMAN_RESOURCES);

        // Get all the svcContractList where humanResources is less than or equal to SMALLER_HUMAN_RESOURCES
        defaultSvcContractShouldNotBeFound("humanResources.lessThanOrEqual=" + SMALLER_HUMAN_RESOURCES);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResources is less than DEFAULT_HUMAN_RESOURCES
        defaultSvcContractShouldNotBeFound("humanResources.lessThan=" + DEFAULT_HUMAN_RESOURCES);

        // Get all the svcContractList where humanResources is less than UPDATED_HUMAN_RESOURCES
        defaultSvcContractShouldBeFound("humanResources.lessThan=" + UPDATED_HUMAN_RESOURCES);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResources is greater than DEFAULT_HUMAN_RESOURCES
        defaultSvcContractShouldNotBeFound("humanResources.greaterThan=" + DEFAULT_HUMAN_RESOURCES);

        // Get all the svcContractList where humanResources is greater than SMALLER_HUMAN_RESOURCES
        defaultSvcContractShouldBeFound("humanResources.greaterThan=" + SMALLER_HUMAN_RESOURCES);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesWeekendIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResourcesWeekend equals to DEFAULT_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldBeFound("humanResourcesWeekend.equals=" + DEFAULT_HUMAN_RESOURCES_WEEKEND);

        // Get all the svcContractList where humanResourcesWeekend equals to UPDATED_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldNotBeFound("humanResourcesWeekend.equals=" + UPDATED_HUMAN_RESOURCES_WEEKEND);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesWeekendIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResourcesWeekend not equals to DEFAULT_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldNotBeFound("humanResourcesWeekend.notEquals=" + DEFAULT_HUMAN_RESOURCES_WEEKEND);

        // Get all the svcContractList where humanResourcesWeekend not equals to UPDATED_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldBeFound("humanResourcesWeekend.notEquals=" + UPDATED_HUMAN_RESOURCES_WEEKEND);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesWeekendIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResourcesWeekend in DEFAULT_HUMAN_RESOURCES_WEEKEND or UPDATED_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldBeFound(
            "humanResourcesWeekend.in=" + DEFAULT_HUMAN_RESOURCES_WEEKEND + "," + UPDATED_HUMAN_RESOURCES_WEEKEND
        );

        // Get all the svcContractList where humanResourcesWeekend equals to UPDATED_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldNotBeFound("humanResourcesWeekend.in=" + UPDATED_HUMAN_RESOURCES_WEEKEND);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesWeekendIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResourcesWeekend is not null
        defaultSvcContractShouldBeFound("humanResourcesWeekend.specified=true");

        // Get all the svcContractList where humanResourcesWeekend is null
        defaultSvcContractShouldNotBeFound("humanResourcesWeekend.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesWeekendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResourcesWeekend is greater than or equal to DEFAULT_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldBeFound("humanResourcesWeekend.greaterThanOrEqual=" + DEFAULT_HUMAN_RESOURCES_WEEKEND);

        // Get all the svcContractList where humanResourcesWeekend is greater than or equal to UPDATED_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldNotBeFound("humanResourcesWeekend.greaterThanOrEqual=" + UPDATED_HUMAN_RESOURCES_WEEKEND);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesWeekendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResourcesWeekend is less than or equal to DEFAULT_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldBeFound("humanResourcesWeekend.lessThanOrEqual=" + DEFAULT_HUMAN_RESOURCES_WEEKEND);

        // Get all the svcContractList where humanResourcesWeekend is less than or equal to SMALLER_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldNotBeFound("humanResourcesWeekend.lessThanOrEqual=" + SMALLER_HUMAN_RESOURCES_WEEKEND);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesWeekendIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResourcesWeekend is less than DEFAULT_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldNotBeFound("humanResourcesWeekend.lessThan=" + DEFAULT_HUMAN_RESOURCES_WEEKEND);

        // Get all the svcContractList where humanResourcesWeekend is less than UPDATED_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldBeFound("humanResourcesWeekend.lessThan=" + UPDATED_HUMAN_RESOURCES_WEEKEND);
    }

    @Test
    @Transactional
    void getAllSvcContractsByHumanResourcesWeekendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where humanResourcesWeekend is greater than DEFAULT_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldNotBeFound("humanResourcesWeekend.greaterThan=" + DEFAULT_HUMAN_RESOURCES_WEEKEND);

        // Get all the svcContractList where humanResourcesWeekend is greater than SMALLER_HUMAN_RESOURCES_WEEKEND
        defaultSvcContractShouldBeFound("humanResourcesWeekend.greaterThan=" + SMALLER_HUMAN_RESOURCES_WEEKEND);
    }

    @Test
    @Transactional
    void getAllSvcContractsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where status equals to DEFAULT_STATUS
        defaultSvcContractShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the svcContractList where status equals to UPDATED_STATUS
        defaultSvcContractShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcContractsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where status not equals to DEFAULT_STATUS
        defaultSvcContractShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the svcContractList where status not equals to UPDATED_STATUS
        defaultSvcContractShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcContractsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSvcContractShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the svcContractList where status equals to UPDATED_STATUS
        defaultSvcContractShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcContractsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where status is not null
        defaultSvcContractShouldBeFound("status.specified=true");

        // Get all the svcContractList where status is null
        defaultSvcContractShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsBySubjectCountIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where subjectCount equals to DEFAULT_SUBJECT_COUNT
        defaultSvcContractShouldBeFound("subjectCount.equals=" + DEFAULT_SUBJECT_COUNT);

        // Get all the svcContractList where subjectCount equals to UPDATED_SUBJECT_COUNT
        defaultSvcContractShouldNotBeFound("subjectCount.equals=" + UPDATED_SUBJECT_COUNT);
    }

    @Test
    @Transactional
    void getAllSvcContractsBySubjectCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where subjectCount not equals to DEFAULT_SUBJECT_COUNT
        defaultSvcContractShouldNotBeFound("subjectCount.notEquals=" + DEFAULT_SUBJECT_COUNT);

        // Get all the svcContractList where subjectCount not equals to UPDATED_SUBJECT_COUNT
        defaultSvcContractShouldBeFound("subjectCount.notEquals=" + UPDATED_SUBJECT_COUNT);
    }

    @Test
    @Transactional
    void getAllSvcContractsBySubjectCountIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where subjectCount in DEFAULT_SUBJECT_COUNT or UPDATED_SUBJECT_COUNT
        defaultSvcContractShouldBeFound("subjectCount.in=" + DEFAULT_SUBJECT_COUNT + "," + UPDATED_SUBJECT_COUNT);

        // Get all the svcContractList where subjectCount equals to UPDATED_SUBJECT_COUNT
        defaultSvcContractShouldNotBeFound("subjectCount.in=" + UPDATED_SUBJECT_COUNT);
    }

    @Test
    @Transactional
    void getAllSvcContractsBySubjectCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where subjectCount is not null
        defaultSvcContractShouldBeFound("subjectCount.specified=true");

        // Get all the svcContractList where subjectCount is null
        defaultSvcContractShouldNotBeFound("subjectCount.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsBySubjectCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where subjectCount is greater than or equal to DEFAULT_SUBJECT_COUNT
        defaultSvcContractShouldBeFound("subjectCount.greaterThanOrEqual=" + DEFAULT_SUBJECT_COUNT);

        // Get all the svcContractList where subjectCount is greater than or equal to UPDATED_SUBJECT_COUNT
        defaultSvcContractShouldNotBeFound("subjectCount.greaterThanOrEqual=" + UPDATED_SUBJECT_COUNT);
    }

    @Test
    @Transactional
    void getAllSvcContractsBySubjectCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where subjectCount is less than or equal to DEFAULT_SUBJECT_COUNT
        defaultSvcContractShouldBeFound("subjectCount.lessThanOrEqual=" + DEFAULT_SUBJECT_COUNT);

        // Get all the svcContractList where subjectCount is less than or equal to SMALLER_SUBJECT_COUNT
        defaultSvcContractShouldNotBeFound("subjectCount.lessThanOrEqual=" + SMALLER_SUBJECT_COUNT);
    }

    @Test
    @Transactional
    void getAllSvcContractsBySubjectCountIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where subjectCount is less than DEFAULT_SUBJECT_COUNT
        defaultSvcContractShouldNotBeFound("subjectCount.lessThan=" + DEFAULT_SUBJECT_COUNT);

        // Get all the svcContractList where subjectCount is less than UPDATED_SUBJECT_COUNT
        defaultSvcContractShouldBeFound("subjectCount.lessThan=" + UPDATED_SUBJECT_COUNT);
    }

    @Test
    @Transactional
    void getAllSvcContractsBySubjectCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where subjectCount is greater than DEFAULT_SUBJECT_COUNT
        defaultSvcContractShouldNotBeFound("subjectCount.greaterThan=" + DEFAULT_SUBJECT_COUNT);

        // Get all the svcContractList where subjectCount is greater than SMALLER_SUBJECT_COUNT
        defaultSvcContractShouldBeFound("subjectCount.greaterThan=" + SMALLER_SUBJECT_COUNT);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValuePerPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where valuePerPerson equals to DEFAULT_VALUE_PER_PERSON
        defaultSvcContractShouldBeFound("valuePerPerson.equals=" + DEFAULT_VALUE_PER_PERSON);

        // Get all the svcContractList where valuePerPerson equals to UPDATED_VALUE_PER_PERSON
        defaultSvcContractShouldNotBeFound("valuePerPerson.equals=" + UPDATED_VALUE_PER_PERSON);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValuePerPersonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where valuePerPerson not equals to DEFAULT_VALUE_PER_PERSON
        defaultSvcContractShouldNotBeFound("valuePerPerson.notEquals=" + DEFAULT_VALUE_PER_PERSON);

        // Get all the svcContractList where valuePerPerson not equals to UPDATED_VALUE_PER_PERSON
        defaultSvcContractShouldBeFound("valuePerPerson.notEquals=" + UPDATED_VALUE_PER_PERSON);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValuePerPersonIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where valuePerPerson in DEFAULT_VALUE_PER_PERSON or UPDATED_VALUE_PER_PERSON
        defaultSvcContractShouldBeFound("valuePerPerson.in=" + DEFAULT_VALUE_PER_PERSON + "," + UPDATED_VALUE_PER_PERSON);

        // Get all the svcContractList where valuePerPerson equals to UPDATED_VALUE_PER_PERSON
        defaultSvcContractShouldNotBeFound("valuePerPerson.in=" + UPDATED_VALUE_PER_PERSON);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValuePerPersonIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where valuePerPerson is not null
        defaultSvcContractShouldBeFound("valuePerPerson.specified=true");

        // Get all the svcContractList where valuePerPerson is null
        defaultSvcContractShouldNotBeFound("valuePerPerson.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByValuePerPersonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where valuePerPerson is greater than or equal to DEFAULT_VALUE_PER_PERSON
        defaultSvcContractShouldBeFound("valuePerPerson.greaterThanOrEqual=" + DEFAULT_VALUE_PER_PERSON);

        // Get all the svcContractList where valuePerPerson is greater than or equal to UPDATED_VALUE_PER_PERSON
        defaultSvcContractShouldNotBeFound("valuePerPerson.greaterThanOrEqual=" + UPDATED_VALUE_PER_PERSON);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValuePerPersonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where valuePerPerson is less than or equal to DEFAULT_VALUE_PER_PERSON
        defaultSvcContractShouldBeFound("valuePerPerson.lessThanOrEqual=" + DEFAULT_VALUE_PER_PERSON);

        // Get all the svcContractList where valuePerPerson is less than or equal to SMALLER_VALUE_PER_PERSON
        defaultSvcContractShouldNotBeFound("valuePerPerson.lessThanOrEqual=" + SMALLER_VALUE_PER_PERSON);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValuePerPersonIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where valuePerPerson is less than DEFAULT_VALUE_PER_PERSON
        defaultSvcContractShouldNotBeFound("valuePerPerson.lessThan=" + DEFAULT_VALUE_PER_PERSON);

        // Get all the svcContractList where valuePerPerson is less than UPDATED_VALUE_PER_PERSON
        defaultSvcContractShouldBeFound("valuePerPerson.lessThan=" + UPDATED_VALUE_PER_PERSON);
    }

    @Test
    @Transactional
    void getAllSvcContractsByValuePerPersonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where valuePerPerson is greater than DEFAULT_VALUE_PER_PERSON
        defaultSvcContractShouldNotBeFound("valuePerPerson.greaterThan=" + DEFAULT_VALUE_PER_PERSON);

        // Get all the svcContractList where valuePerPerson is greater than SMALLER_VALUE_PER_PERSON
        defaultSvcContractShouldBeFound("valuePerPerson.greaterThan=" + SMALLER_VALUE_PER_PERSON);
    }

    @Test
    @Transactional
    void getAllSvcContractsByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where year equals to DEFAULT_YEAR
        defaultSvcContractShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the svcContractList where year equals to UPDATED_YEAR
        defaultSvcContractShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllSvcContractsByYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where year not equals to DEFAULT_YEAR
        defaultSvcContractShouldNotBeFound("year.notEquals=" + DEFAULT_YEAR);

        // Get all the svcContractList where year not equals to UPDATED_YEAR
        defaultSvcContractShouldBeFound("year.notEquals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllSvcContractsByYearIsInShouldWork() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultSvcContractShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the svcContractList where year equals to UPDATED_YEAR
        defaultSvcContractShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllSvcContractsByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where year is not null
        defaultSvcContractShouldBeFound("year.specified=true");

        // Get all the svcContractList where year is null
        defaultSvcContractShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcContractsByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where year is greater than or equal to DEFAULT_YEAR
        defaultSvcContractShouldBeFound("year.greaterThanOrEqual=" + DEFAULT_YEAR);

        // Get all the svcContractList where year is greater than or equal to UPDATED_YEAR
        defaultSvcContractShouldNotBeFound("year.greaterThanOrEqual=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllSvcContractsByYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where year is less than or equal to DEFAULT_YEAR
        defaultSvcContractShouldBeFound("year.lessThanOrEqual=" + DEFAULT_YEAR);

        // Get all the svcContractList where year is less than or equal to SMALLER_YEAR
        defaultSvcContractShouldNotBeFound("year.lessThanOrEqual=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllSvcContractsByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where year is less than DEFAULT_YEAR
        defaultSvcContractShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the svcContractList where year is less than UPDATED_YEAR
        defaultSvcContractShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllSvcContractsByYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        // Get all the svcContractList where year is greater than DEFAULT_YEAR
        defaultSvcContractShouldNotBeFound("year.greaterThan=" + DEFAULT_YEAR);

        // Get all the svcContractList where year is greater than SMALLER_YEAR
        defaultSvcContractShouldBeFound("year.greaterThan=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllSvcContractsByTargetsIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);
        SvcTarget targets = SvcTargetResourceIT.createEntity(em);
        em.persist(targets);
        em.flush();
        svcContract.addTargets(targets);
        svcContractRepository.saveAndFlush(svcContract);
        Long targetsId = targets.getId();

        // Get all the svcContractList where targets equals to targetsId
        defaultSvcContractShouldBeFound("targetsId.equals=" + targetsId);

        // Get all the svcContractList where targets equals to (targetsId + 1)
        defaultSvcContractShouldNotBeFound("targetsId.equals=" + (targetsId + 1));
    }

    @Test
    @Transactional
    void getAllSvcContractsByApprovedByIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);
        User approvedBy = UserResourceIT.createEntity(em);
        em.persist(approvedBy);
        em.flush();
        svcContract.setApprovedBy(approvedBy);
        svcContractRepository.saveAndFlush(svcContract);
        Long approvedById = approvedBy.getId();

        // Get all the svcContractList where approvedBy equals to approvedById
        defaultSvcContractShouldBeFound("approvedById.equals=" + approvedById);

        // Get all the svcContractList where approvedBy equals to (approvedById + 1)
        defaultSvcContractShouldNotBeFound("approvedById.equals=" + (approvedById + 1));
    }

    @Test
    @Transactional
    void getAllSvcContractsByOwnerByIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);
        User ownerBy = UserResourceIT.createEntity(em);
        em.persist(ownerBy);
        em.flush();
        svcContract.setOwnerBy(ownerBy);
        svcContractRepository.saveAndFlush(svcContract);
        Long ownerById = ownerBy.getId();

        // Get all the svcContractList where ownerBy equals to ownerById
        defaultSvcContractShouldBeFound("ownerById.equals=" + ownerById);

        // Get all the svcContractList where ownerBy equals to (ownerById + 1)
        defaultSvcContractShouldNotBeFound("ownerById.equals=" + (ownerById + 1));
    }

    @Test
    @Transactional
    void getAllSvcContractsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);
        SvcUnit unit = SvcUnitResourceIT.createEntity(em);
        em.persist(unit);
        em.flush();
        svcContract.setUnit(unit);
        svcContractRepository.saveAndFlush(svcContract);
        Long unitId = unit.getId();

        // Get all the svcContractList where unit equals to unitId
        defaultSvcContractShouldBeFound("unitId.equals=" + unitId);

        // Get all the svcContractList where unit equals to (unitId + 1)
        defaultSvcContractShouldNotBeFound("unitId.equals=" + (unitId + 1));
    }

    @Test
    @Transactional
    void getAllSvcContractsBySalerIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);
        OrgUser saler = OrgUserResourceIT.createEntity(em);
        em.persist(saler);
        em.flush();
        svcContract.setSaler(saler);
        svcContractRepository.saveAndFlush(svcContract);
        Long salerId = saler.getId();

        // Get all the svcContractList where saler equals to salerId
        defaultSvcContractShouldBeFound("salerId.equals=" + salerId);

        // Get all the svcContractList where saler equals to (salerId + 1)
        defaultSvcContractShouldNotBeFound("salerId.equals=" + (salerId + 1));
    }

    @Test
    @Transactional
    void getAllSvcContractsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);
        SvcClient client = SvcClientResourceIT.createEntity(em);
        em.persist(client);
        em.flush();
        svcContract.setClient(client);
        svcContractRepository.saveAndFlush(svcContract);
        Long clientId = client.getId();

        // Get all the svcContractList where client equals to clientId
        defaultSvcContractShouldBeFound("clientId.equals=" + clientId);

        // Get all the svcContractList where client equals to (clientId + 1)
        defaultSvcContractShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcContractShouldBeFound(String filter) throws Exception {
        restSvcContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID)))
            .andExpect(jsonPath("$.[*].appendicesNumber").value(hasItem(DEFAULT_APPENDICES_NUMBER)))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].effectiveTimeFrom").value(hasItem(DEFAULT_EFFECTIVE_TIME_FROM.toString())))
            .andExpect(jsonPath("$.[*].effectiveTimeTo").value(hasItem(DEFAULT_EFFECTIVE_TIME_TO.toString())))
            .andExpect(jsonPath("$.[*].durationMonth").value(hasItem(DEFAULT_DURATION_MONTH)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].contractValue").value(hasItem(sameNumber(DEFAULT_CONTRACT_VALUE))))
            .andExpect(jsonPath("$.[*].humanResources").value(hasItem(DEFAULT_HUMAN_RESOURCES)))
            .andExpect(jsonPath("$.[*].humanResourcesWeekend").value(hasItem(DEFAULT_HUMAN_RESOURCES_WEEKEND)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].subjectCount").value(hasItem(DEFAULT_SUBJECT_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].valuePerPerson").value(hasItem(sameNumber(DEFAULT_VALUE_PER_PERSON))))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));

        // Check, that the count call also returns 1
        restSvcContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcContractShouldNotBeFound(String filter) throws Exception {
        restSvcContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcContract() throws Exception {
        // Get the svcContract
        restSvcContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcContract() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();

        // Update the svcContract
        SvcContract updatedSvcContract = svcContractRepository.findById(svcContract.getId()).get();
        // Disconnect from session so that the updates on updatedSvcContract are not directly saved in db
        em.detach(updatedSvcContract);
        updatedSvcContract
            .orderNumber(UPDATED_ORDER_NUMBER)
            .documentId(UPDATED_DOCUMENT_ID)
            .appendicesNumber(UPDATED_APPENDICES_NUMBER)
            .fileId(UPDATED_FILE_ID)
            .content(UPDATED_CONTENT)
            .effectiveTimeFrom(UPDATED_EFFECTIVE_TIME_FROM)
            .effectiveTimeTo(UPDATED_EFFECTIVE_TIME_TO)
            .durationMonth(UPDATED_DURATION_MONTH)
            .value(UPDATED_VALUE)
            .contractValue(UPDATED_CONTRACT_VALUE)
            .humanResources(UPDATED_HUMAN_RESOURCES)
            .humanResourcesWeekend(UPDATED_HUMAN_RESOURCES_WEEKEND)
            .status(UPDATED_STATUS)
            .subjectCount(UPDATED_SUBJECT_COUNT)
            .valuePerPerson(UPDATED_VALUE_PER_PERSON)
            .year(UPDATED_YEAR);
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(updatedSvcContract);

        restSvcContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
        SvcContract testSvcContract = svcContractList.get(svcContractList.size() - 1);
        assertThat(testSvcContract.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testSvcContract.getDocumentId()).isEqualTo(UPDATED_DOCUMENT_ID);
        assertThat(testSvcContract.getAppendicesNumber()).isEqualTo(UPDATED_APPENDICES_NUMBER);
        assertThat(testSvcContract.getFileId()).isEqualTo(UPDATED_FILE_ID);
        assertThat(testSvcContract.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSvcContract.getEffectiveTimeFrom()).isEqualTo(UPDATED_EFFECTIVE_TIME_FROM);
        assertThat(testSvcContract.getEffectiveTimeTo()).isEqualTo(UPDATED_EFFECTIVE_TIME_TO);
        assertThat(testSvcContract.getDurationMonth()).isEqualTo(UPDATED_DURATION_MONTH);
        assertThat(testSvcContract.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSvcContract.getContractValue()).isEqualTo(UPDATED_CONTRACT_VALUE);
        assertThat(testSvcContract.getHumanResources()).isEqualTo(UPDATED_HUMAN_RESOURCES);
        assertThat(testSvcContract.getHumanResourcesWeekend()).isEqualTo(UPDATED_HUMAN_RESOURCES_WEEKEND);
        assertThat(testSvcContract.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcContract.getSubjectCount()).isEqualTo(UPDATED_SUBJECT_COUNT);
        assertThat(testSvcContract.getValuePerPerson()).isEqualTo(UPDATED_VALUE_PER_PERSON);
        assertThat(testSvcContract.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingSvcContract() throws Exception {
        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();
        svcContract.setId(count.incrementAndGet());

        // Create the SvcContract
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcContract() throws Exception {
        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();
        svcContract.setId(count.incrementAndGet());

        // Create the SvcContract
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcContract() throws Exception {
        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();
        svcContract.setId(count.incrementAndGet());

        // Create the SvcContract
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcContractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcContractWithPatch() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();

        // Update the svcContract using partial update
        SvcContract partialUpdatedSvcContract = new SvcContract();
        partialUpdatedSvcContract.setId(svcContract.getId());

        partialUpdatedSvcContract
            .documentId(UPDATED_DOCUMENT_ID)
            .appendicesNumber(UPDATED_APPENDICES_NUMBER)
            .fileId(UPDATED_FILE_ID)
            .effectiveTimeFrom(UPDATED_EFFECTIVE_TIME_FROM)
            .durationMonth(UPDATED_DURATION_MONTH)
            .status(UPDATED_STATUS)
            .subjectCount(UPDATED_SUBJECT_COUNT)
            .valuePerPerson(UPDATED_VALUE_PER_PERSON);

        restSvcContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcContract))
            )
            .andExpect(status().isOk());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
        SvcContract testSvcContract = svcContractList.get(svcContractList.size() - 1);
        assertThat(testSvcContract.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testSvcContract.getDocumentId()).isEqualTo(UPDATED_DOCUMENT_ID);
        assertThat(testSvcContract.getAppendicesNumber()).isEqualTo(UPDATED_APPENDICES_NUMBER);
        assertThat(testSvcContract.getFileId()).isEqualTo(UPDATED_FILE_ID);
        assertThat(testSvcContract.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSvcContract.getEffectiveTimeFrom()).isEqualTo(UPDATED_EFFECTIVE_TIME_FROM);
        assertThat(testSvcContract.getEffectiveTimeTo()).isEqualTo(DEFAULT_EFFECTIVE_TIME_TO);
        assertThat(testSvcContract.getDurationMonth()).isEqualTo(UPDATED_DURATION_MONTH);
        assertThat(testSvcContract.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testSvcContract.getContractValue()).isEqualByComparingTo(DEFAULT_CONTRACT_VALUE);
        assertThat(testSvcContract.getHumanResources()).isEqualTo(DEFAULT_HUMAN_RESOURCES);
        assertThat(testSvcContract.getHumanResourcesWeekend()).isEqualTo(DEFAULT_HUMAN_RESOURCES_WEEKEND);
        assertThat(testSvcContract.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcContract.getSubjectCount()).isEqualTo(UPDATED_SUBJECT_COUNT);
        assertThat(testSvcContract.getValuePerPerson()).isEqualByComparingTo(UPDATED_VALUE_PER_PERSON);
        assertThat(testSvcContract.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateSvcContractWithPatch() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();

        // Update the svcContract using partial update
        SvcContract partialUpdatedSvcContract = new SvcContract();
        partialUpdatedSvcContract.setId(svcContract.getId());

        partialUpdatedSvcContract
            .orderNumber(UPDATED_ORDER_NUMBER)
            .documentId(UPDATED_DOCUMENT_ID)
            .appendicesNumber(UPDATED_APPENDICES_NUMBER)
            .fileId(UPDATED_FILE_ID)
            .content(UPDATED_CONTENT)
            .effectiveTimeFrom(UPDATED_EFFECTIVE_TIME_FROM)
            .effectiveTimeTo(UPDATED_EFFECTIVE_TIME_TO)
            .durationMonth(UPDATED_DURATION_MONTH)
            .value(UPDATED_VALUE)
            .contractValue(UPDATED_CONTRACT_VALUE)
            .humanResources(UPDATED_HUMAN_RESOURCES)
            .humanResourcesWeekend(UPDATED_HUMAN_RESOURCES_WEEKEND)
            .status(UPDATED_STATUS)
            .subjectCount(UPDATED_SUBJECT_COUNT)
            .valuePerPerson(UPDATED_VALUE_PER_PERSON)
            .year(UPDATED_YEAR);

        restSvcContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcContract))
            )
            .andExpect(status().isOk());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
        SvcContract testSvcContract = svcContractList.get(svcContractList.size() - 1);
        assertThat(testSvcContract.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testSvcContract.getDocumentId()).isEqualTo(UPDATED_DOCUMENT_ID);
        assertThat(testSvcContract.getAppendicesNumber()).isEqualTo(UPDATED_APPENDICES_NUMBER);
        assertThat(testSvcContract.getFileId()).isEqualTo(UPDATED_FILE_ID);
        assertThat(testSvcContract.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSvcContract.getEffectiveTimeFrom()).isEqualTo(UPDATED_EFFECTIVE_TIME_FROM);
        assertThat(testSvcContract.getEffectiveTimeTo()).isEqualTo(UPDATED_EFFECTIVE_TIME_TO);
        assertThat(testSvcContract.getDurationMonth()).isEqualTo(UPDATED_DURATION_MONTH);
        assertThat(testSvcContract.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testSvcContract.getContractValue()).isEqualByComparingTo(UPDATED_CONTRACT_VALUE);
        assertThat(testSvcContract.getHumanResources()).isEqualTo(UPDATED_HUMAN_RESOURCES);
        assertThat(testSvcContract.getHumanResourcesWeekend()).isEqualTo(UPDATED_HUMAN_RESOURCES_WEEKEND);
        assertThat(testSvcContract.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcContract.getSubjectCount()).isEqualTo(UPDATED_SUBJECT_COUNT);
        assertThat(testSvcContract.getValuePerPerson()).isEqualByComparingTo(UPDATED_VALUE_PER_PERSON);
        assertThat(testSvcContract.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingSvcContract() throws Exception {
        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();
        svcContract.setId(count.incrementAndGet());

        // Create the SvcContract
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcContractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcContract() throws Exception {
        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();
        svcContract.setId(count.incrementAndGet());

        // Create the SvcContract
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcContract() throws Exception {
        int databaseSizeBeforeUpdate = svcContractRepository.findAll().size();
        svcContract.setId(count.incrementAndGet());

        // Create the SvcContract
        SvcContractDTO svcContractDTO = svcContractMapper.toDto(svcContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcContractMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcContractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcContract in the database
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcContract() throws Exception {
        // Initialize the database
        svcContractRepository.saveAndFlush(svcContract);

        int databaseSizeBeforeDelete = svcContractRepository.findAll().size();

        // Delete the svcContract
        restSvcContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcContract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcContract> svcContractList = svcContractRepository.findAll();
        assertThat(svcContractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
