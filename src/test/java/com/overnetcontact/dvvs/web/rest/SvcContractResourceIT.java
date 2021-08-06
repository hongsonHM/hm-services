package com.overnetcontact.dvvs.web.rest;

import static com.overnetcontact.dvvs.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
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

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CONTRACT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONTRACT_VALUE = new BigDecimal(2);

    private static final Integer DEFAULT_HUMAN_RESOURCES = 1;
    private static final Integer UPDATED_HUMAN_RESOURCES = 2;

    private static final Integer DEFAULT_HUMAN_RESOURCES_WEEKEND = 1;
    private static final Integer UPDATED_HUMAN_RESOURCES_WEEKEND = 2;

    private static final SvcContractStatus DEFAULT_STATUS = SvcContractStatus.SUCCESS;
    private static final SvcContractStatus UPDATED_STATUS = SvcContractStatus.PENDING;

    private static final Long DEFAULT_SUBJECT_COUNT = 1L;
    private static final Long UPDATED_SUBJECT_COUNT = 2L;

    private static final BigDecimal DEFAULT_VALUE_PER_PERSON = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE_PER_PERSON = new BigDecimal(2);

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

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
