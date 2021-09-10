package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcSchedulePlanRecord;
import com.overnetcontact.dvvs.domain.SvcScheduleUnitRecord;
import com.overnetcontact.dvvs.repository.SvcScheduleUnitRecordRepository;
import com.overnetcontact.dvvs.service.criteria.SvcScheduleUnitRecordCriteria;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitRecordDTO;
import com.overnetcontact.dvvs.service.mapper.SvcScheduleUnitRecordMapper;
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
 * Integration tests for the {@link SvcScheduleUnitRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcScheduleUnitRecordResourceIT {

    private static final Integer DEFAULT_SVC_SCHEDULE_UNIT_ID = 1;
    private static final Integer UPDATED_SVC_SCHEDULE_UNIT_ID = 2;
    private static final Integer SMALLER_SVC_SCHEDULE_UNIT_ID = 1 - 1;

    private static final Integer DEFAULT_IS_PUBLISHED = 1;
    private static final Integer UPDATED_IS_PUBLISHED = 2;
    private static final Integer SMALLER_IS_PUBLISHED = 1 - 1;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_APPROVED = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED = "BBBBBBBBBB";

    private static final Integer DEFAULT_APPROVED_ID = 1;
    private static final Integer UPDATED_APPROVED_ID = 2;
    private static final Integer SMALLER_APPROVED_ID = 1 - 1;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATE_BY_ID = 1;
    private static final Integer UPDATED_CREATE_BY_ID = 2;
    private static final Integer SMALLER_CREATE_BY_ID = 1 - 1;

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Integer DEFAULT_LAST_MODIFIED_BY_ID = 1;
    private static final Integer UPDATED_LAST_MODIFIED_BY_ID = 2;
    private static final Integer SMALLER_LAST_MODIFIED_BY_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/svc-schedule-unit-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcScheduleUnitRecordRepository svcScheduleUnitRecordRepository;

    @Autowired
    private SvcScheduleUnitRecordMapper svcScheduleUnitRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcScheduleUnitRecordMockMvc;

    private SvcScheduleUnitRecord svcScheduleUnitRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcScheduleUnitRecord createEntity(EntityManager em) {
        SvcScheduleUnitRecord svcScheduleUnitRecord = new SvcScheduleUnitRecord()
            .svcScheduleUnitId(DEFAULT_SVC_SCHEDULE_UNIT_ID)
            .isPublished(DEFAULT_IS_PUBLISHED)
            .status(DEFAULT_STATUS)
            .approved(DEFAULT_APPROVED)
            .approvedId(DEFAULT_APPROVED_ID)
            .comment(DEFAULT_COMMENT)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createBy(DEFAULT_CREATE_BY)
            .createById(DEFAULT_CREATE_BY_ID)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedById(DEFAULT_LAST_MODIFIED_BY_ID);
        return svcScheduleUnitRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcScheduleUnitRecord createUpdatedEntity(EntityManager em) {
        SvcScheduleUnitRecord svcScheduleUnitRecord = new SvcScheduleUnitRecord()
            .svcScheduleUnitId(UPDATED_SVC_SCHEDULE_UNIT_ID)
            .isPublished(UPDATED_IS_PUBLISHED)
            .status(UPDATED_STATUS)
            .approved(UPDATED_APPROVED)
            .approvedId(UPDATED_APPROVED_ID)
            .comment(UPDATED_COMMENT)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createBy(UPDATED_CREATE_BY)
            .createById(UPDATED_CREATE_BY_ID)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedById(UPDATED_LAST_MODIFIED_BY_ID);
        return svcScheduleUnitRecord;
    }

    @BeforeEach
    public void initTest() {
        svcScheduleUnitRecord = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcScheduleUnitRecord() throws Exception {
        int databaseSizeBeforeCreate = svcScheduleUnitRecordRepository.findAll().size();
        // Create the SvcScheduleUnitRecord
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);
        restSvcScheduleUnitRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeCreate + 1);
        SvcScheduleUnitRecord testSvcScheduleUnitRecord = svcScheduleUnitRecordList.get(svcScheduleUnitRecordList.size() - 1);
        assertThat(testSvcScheduleUnitRecord.getSvcScheduleUnitId()).isEqualTo(DEFAULT_SVC_SCHEDULE_UNIT_ID);
        assertThat(testSvcScheduleUnitRecord.getIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
        assertThat(testSvcScheduleUnitRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSvcScheduleUnitRecord.getApproved()).isEqualTo(DEFAULT_APPROVED);
        assertThat(testSvcScheduleUnitRecord.getApprovedId()).isEqualTo(DEFAULT_APPROVED_ID);
        assertThat(testSvcScheduleUnitRecord.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testSvcScheduleUnitRecord.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSvcScheduleUnitRecord.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testSvcScheduleUnitRecord.getCreateById()).isEqualTo(DEFAULT_CREATE_BY_ID);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedById()).isEqualTo(DEFAULT_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void createSvcScheduleUnitRecordWithExistingId() throws Exception {
        // Create the SvcScheduleUnitRecord with an existing ID
        svcScheduleUnitRecord.setId(1L);
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        int databaseSizeBeforeCreate = svcScheduleUnitRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcScheduleUnitRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSvcScheduleUnitIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRecordRepository.findAll().size();
        // set the field null
        svcScheduleUnitRecord.setSvcScheduleUnitId(null);

        // Create the SvcScheduleUnitRecord, which fails.
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        restSvcScheduleUnitRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPublishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRecordRepository.findAll().size();
        // set the field null
        svcScheduleUnitRecord.setIsPublished(null);

        // Create the SvcScheduleUnitRecord, which fails.
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        restSvcScheduleUnitRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRecordRepository.findAll().size();
        // set the field null
        svcScheduleUnitRecord.setStatus(null);

        // Create the SvcScheduleUnitRecord, which fails.
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        restSvcScheduleUnitRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecords() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList
        restSvcScheduleUnitRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcScheduleUnitRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].svcScheduleUnitId").value(hasItem(DEFAULT_SVC_SCHEDULE_UNIT_ID)))
            .andExpect(jsonPath("$.[*].isPublished").value(hasItem(DEFAULT_IS_PUBLISHED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED)))
            .andExpect(jsonPath("$.[*].approvedId").value(hasItem(DEFAULT_APPROVED_ID)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createById").value(hasItem(DEFAULT_CREATE_BY_ID)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedById").value(hasItem(DEFAULT_LAST_MODIFIED_BY_ID)));
    }

    @Test
    @Transactional
    void getSvcScheduleUnitRecord() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get the svcScheduleUnitRecord
        restSvcScheduleUnitRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, svcScheduleUnitRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcScheduleUnitRecord.getId().intValue()))
            .andExpect(jsonPath("$.svcScheduleUnitId").value(DEFAULT_SVC_SCHEDULE_UNIT_ID))
            .andExpect(jsonPath("$.isPublished").value(DEFAULT_IS_PUBLISHED))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.approved").value(DEFAULT_APPROVED))
            .andExpect(jsonPath("$.approvedId").value(DEFAULT_APPROVED_ID))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createById").value(DEFAULT_CREATE_BY_ID))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedById").value(DEFAULT_LAST_MODIFIED_BY_ID));
    }

    @Test
    @Transactional
    void getSvcScheduleUnitRecordsByIdFiltering() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        Long id = svcScheduleUnitRecord.getId();

        defaultSvcScheduleUnitRecordShouldBeFound("id.equals=" + id);
        defaultSvcScheduleUnitRecordShouldNotBeFound("id.notEquals=" + id);

        defaultSvcScheduleUnitRecordShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcScheduleUnitRecordShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcScheduleUnitRecordShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcScheduleUnitRecordShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcScheduleUnitIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId equals to DEFAULT_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldBeFound("svcScheduleUnitId.equals=" + DEFAULT_SVC_SCHEDULE_UNIT_ID);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId equals to UPDATED_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcScheduleUnitId.equals=" + UPDATED_SVC_SCHEDULE_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcScheduleUnitIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId not equals to DEFAULT_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcScheduleUnitId.notEquals=" + DEFAULT_SVC_SCHEDULE_UNIT_ID);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId not equals to UPDATED_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldBeFound("svcScheduleUnitId.notEquals=" + UPDATED_SVC_SCHEDULE_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcScheduleUnitIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId in DEFAULT_SVC_SCHEDULE_UNIT_ID or UPDATED_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldBeFound(
            "svcScheduleUnitId.in=" + DEFAULT_SVC_SCHEDULE_UNIT_ID + "," + UPDATED_SVC_SCHEDULE_UNIT_ID
        );

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId equals to UPDATED_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcScheduleUnitId.in=" + UPDATED_SVC_SCHEDULE_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcScheduleUnitIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is not null
        defaultSvcScheduleUnitRecordShouldBeFound("svcScheduleUnitId.specified=true");

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcScheduleUnitId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcScheduleUnitIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is greater than or equal to DEFAULT_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldBeFound("svcScheduleUnitId.greaterThanOrEqual=" + DEFAULT_SVC_SCHEDULE_UNIT_ID);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is greater than or equal to UPDATED_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcScheduleUnitId.greaterThanOrEqual=" + UPDATED_SVC_SCHEDULE_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcScheduleUnitIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is less than or equal to DEFAULT_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldBeFound("svcScheduleUnitId.lessThanOrEqual=" + DEFAULT_SVC_SCHEDULE_UNIT_ID);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is less than or equal to SMALLER_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcScheduleUnitId.lessThanOrEqual=" + SMALLER_SVC_SCHEDULE_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcScheduleUnitIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is less than DEFAULT_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcScheduleUnitId.lessThan=" + DEFAULT_SVC_SCHEDULE_UNIT_ID);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is less than UPDATED_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldBeFound("svcScheduleUnitId.lessThan=" + UPDATED_SVC_SCHEDULE_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcScheduleUnitIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is greater than DEFAULT_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcScheduleUnitId.greaterThan=" + DEFAULT_SVC_SCHEDULE_UNIT_ID);

        // Get all the svcScheduleUnitRecordList where svcScheduleUnitId is greater than SMALLER_SVC_SCHEDULE_UNIT_ID
        defaultSvcScheduleUnitRecordShouldBeFound("svcScheduleUnitId.greaterThan=" + SMALLER_SVC_SCHEDULE_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByIsPublishedIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where isPublished equals to DEFAULT_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldBeFound("isPublished.equals=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcScheduleUnitRecordList where isPublished equals to UPDATED_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldNotBeFound("isPublished.equals=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByIsPublishedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where isPublished not equals to DEFAULT_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldNotBeFound("isPublished.notEquals=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcScheduleUnitRecordList where isPublished not equals to UPDATED_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldBeFound("isPublished.notEquals=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByIsPublishedIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where isPublished in DEFAULT_IS_PUBLISHED or UPDATED_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldBeFound("isPublished.in=" + DEFAULT_IS_PUBLISHED + "," + UPDATED_IS_PUBLISHED);

        // Get all the svcScheduleUnitRecordList where isPublished equals to UPDATED_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldNotBeFound("isPublished.in=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByIsPublishedIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where isPublished is not null
        defaultSvcScheduleUnitRecordShouldBeFound("isPublished.specified=true");

        // Get all the svcScheduleUnitRecordList where isPublished is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("isPublished.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByIsPublishedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where isPublished is greater than or equal to DEFAULT_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldBeFound("isPublished.greaterThanOrEqual=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcScheduleUnitRecordList where isPublished is greater than or equal to UPDATED_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldNotBeFound("isPublished.greaterThanOrEqual=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByIsPublishedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where isPublished is less than or equal to DEFAULT_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldBeFound("isPublished.lessThanOrEqual=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcScheduleUnitRecordList where isPublished is less than or equal to SMALLER_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldNotBeFound("isPublished.lessThanOrEqual=" + SMALLER_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByIsPublishedIsLessThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where isPublished is less than DEFAULT_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldNotBeFound("isPublished.lessThan=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcScheduleUnitRecordList where isPublished is less than UPDATED_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldBeFound("isPublished.lessThan=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByIsPublishedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where isPublished is greater than DEFAULT_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldNotBeFound("isPublished.greaterThan=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcScheduleUnitRecordList where isPublished is greater than SMALLER_IS_PUBLISHED
        defaultSvcScheduleUnitRecordShouldBeFound("isPublished.greaterThan=" + SMALLER_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where status equals to DEFAULT_STATUS
        defaultSvcScheduleUnitRecordShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the svcScheduleUnitRecordList where status equals to UPDATED_STATUS
        defaultSvcScheduleUnitRecordShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where status not equals to DEFAULT_STATUS
        defaultSvcScheduleUnitRecordShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the svcScheduleUnitRecordList where status not equals to UPDATED_STATUS
        defaultSvcScheduleUnitRecordShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSvcScheduleUnitRecordShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the svcScheduleUnitRecordList where status equals to UPDATED_STATUS
        defaultSvcScheduleUnitRecordShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where status is not null
        defaultSvcScheduleUnitRecordShouldBeFound("status.specified=true");

        // Get all the svcScheduleUnitRecordList where status is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByStatusContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where status contains DEFAULT_STATUS
        defaultSvcScheduleUnitRecordShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the svcScheduleUnitRecordList where status contains UPDATED_STATUS
        defaultSvcScheduleUnitRecordShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where status does not contain DEFAULT_STATUS
        defaultSvcScheduleUnitRecordShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the svcScheduleUnitRecordList where status does not contain UPDATED_STATUS
        defaultSvcScheduleUnitRecordShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approved equals to DEFAULT_APPROVED
        defaultSvcScheduleUnitRecordShouldBeFound("approved.equals=" + DEFAULT_APPROVED);

        // Get all the svcScheduleUnitRecordList where approved equals to UPDATED_APPROVED
        defaultSvcScheduleUnitRecordShouldNotBeFound("approved.equals=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approved not equals to DEFAULT_APPROVED
        defaultSvcScheduleUnitRecordShouldNotBeFound("approved.notEquals=" + DEFAULT_APPROVED);

        // Get all the svcScheduleUnitRecordList where approved not equals to UPDATED_APPROVED
        defaultSvcScheduleUnitRecordShouldBeFound("approved.notEquals=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approved in DEFAULT_APPROVED or UPDATED_APPROVED
        defaultSvcScheduleUnitRecordShouldBeFound("approved.in=" + DEFAULT_APPROVED + "," + UPDATED_APPROVED);

        // Get all the svcScheduleUnitRecordList where approved equals to UPDATED_APPROVED
        defaultSvcScheduleUnitRecordShouldNotBeFound("approved.in=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approved is not null
        defaultSvcScheduleUnitRecordShouldBeFound("approved.specified=true");

        // Get all the svcScheduleUnitRecordList where approved is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("approved.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approved contains DEFAULT_APPROVED
        defaultSvcScheduleUnitRecordShouldBeFound("approved.contains=" + DEFAULT_APPROVED);

        // Get all the svcScheduleUnitRecordList where approved contains UPDATED_APPROVED
        defaultSvcScheduleUnitRecordShouldNotBeFound("approved.contains=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedNotContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approved does not contain DEFAULT_APPROVED
        defaultSvcScheduleUnitRecordShouldNotBeFound("approved.doesNotContain=" + DEFAULT_APPROVED);

        // Get all the svcScheduleUnitRecordList where approved does not contain UPDATED_APPROVED
        defaultSvcScheduleUnitRecordShouldBeFound("approved.doesNotContain=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approvedId equals to DEFAULT_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldBeFound("approvedId.equals=" + DEFAULT_APPROVED_ID);

        // Get all the svcScheduleUnitRecordList where approvedId equals to UPDATED_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("approvedId.equals=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approvedId not equals to DEFAULT_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("approvedId.notEquals=" + DEFAULT_APPROVED_ID);

        // Get all the svcScheduleUnitRecordList where approvedId not equals to UPDATED_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldBeFound("approvedId.notEquals=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approvedId in DEFAULT_APPROVED_ID or UPDATED_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldBeFound("approvedId.in=" + DEFAULT_APPROVED_ID + "," + UPDATED_APPROVED_ID);

        // Get all the svcScheduleUnitRecordList where approvedId equals to UPDATED_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("approvedId.in=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approvedId is not null
        defaultSvcScheduleUnitRecordShouldBeFound("approvedId.specified=true");

        // Get all the svcScheduleUnitRecordList where approvedId is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("approvedId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approvedId is greater than or equal to DEFAULT_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldBeFound("approvedId.greaterThanOrEqual=" + DEFAULT_APPROVED_ID);

        // Get all the svcScheduleUnitRecordList where approvedId is greater than or equal to UPDATED_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("approvedId.greaterThanOrEqual=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approvedId is less than or equal to DEFAULT_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldBeFound("approvedId.lessThanOrEqual=" + DEFAULT_APPROVED_ID);

        // Get all the svcScheduleUnitRecordList where approvedId is less than or equal to SMALLER_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("approvedId.lessThanOrEqual=" + SMALLER_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approvedId is less than DEFAULT_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("approvedId.lessThan=" + DEFAULT_APPROVED_ID);

        // Get all the svcScheduleUnitRecordList where approvedId is less than UPDATED_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldBeFound("approvedId.lessThan=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByApprovedIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where approvedId is greater than DEFAULT_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("approvedId.greaterThan=" + DEFAULT_APPROVED_ID);

        // Get all the svcScheduleUnitRecordList where approvedId is greater than SMALLER_APPROVED_ID
        defaultSvcScheduleUnitRecordShouldBeFound("approvedId.greaterThan=" + SMALLER_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where comment equals to DEFAULT_COMMENT
        defaultSvcScheduleUnitRecordShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the svcScheduleUnitRecordList where comment equals to UPDATED_COMMENT
        defaultSvcScheduleUnitRecordShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCommentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where comment not equals to DEFAULT_COMMENT
        defaultSvcScheduleUnitRecordShouldNotBeFound("comment.notEquals=" + DEFAULT_COMMENT);

        // Get all the svcScheduleUnitRecordList where comment not equals to UPDATED_COMMENT
        defaultSvcScheduleUnitRecordShouldBeFound("comment.notEquals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultSvcScheduleUnitRecordShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the svcScheduleUnitRecordList where comment equals to UPDATED_COMMENT
        defaultSvcScheduleUnitRecordShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where comment is not null
        defaultSvcScheduleUnitRecordShouldBeFound("comment.specified=true");

        // Get all the svcScheduleUnitRecordList where comment is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCommentContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where comment contains DEFAULT_COMMENT
        defaultSvcScheduleUnitRecordShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the svcScheduleUnitRecordList where comment contains UPDATED_COMMENT
        defaultSvcScheduleUnitRecordShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where comment does not contain DEFAULT_COMMENT
        defaultSvcScheduleUnitRecordShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the svcScheduleUnitRecordList where comment does not contain UPDATED_COMMENT
        defaultSvcScheduleUnitRecordShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createDate equals to DEFAULT_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the svcScheduleUnitRecordList where createDate equals to UPDATED_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the svcScheduleUnitRecordList where createDate not equals to UPDATED_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the svcScheduleUnitRecordList where createDate equals to UPDATED_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createDate is not null
        defaultSvcScheduleUnitRecordShouldBeFound("createDate.specified=true");

        // Get all the svcScheduleUnitRecordList where createDate is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the svcScheduleUnitRecordList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the svcScheduleUnitRecordList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createDate is less than DEFAULT_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the svcScheduleUnitRecordList where createDate is less than UPDATED_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the svcScheduleUnitRecordList where createDate is greater than SMALLER_CREATE_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is not null
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedDate.specified=true");

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcScheduleUnitRecordList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createBy equals to DEFAULT_CREATE_BY
        defaultSvcScheduleUnitRecordShouldBeFound("createBy.equals=" + DEFAULT_CREATE_BY);

        // Get all the svcScheduleUnitRecordList where createBy equals to UPDATED_CREATE_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("createBy.equals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createBy not equals to DEFAULT_CREATE_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("createBy.notEquals=" + DEFAULT_CREATE_BY);

        // Get all the svcScheduleUnitRecordList where createBy not equals to UPDATED_CREATE_BY
        defaultSvcScheduleUnitRecordShouldBeFound("createBy.notEquals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createBy in DEFAULT_CREATE_BY or UPDATED_CREATE_BY
        defaultSvcScheduleUnitRecordShouldBeFound("createBy.in=" + DEFAULT_CREATE_BY + "," + UPDATED_CREATE_BY);

        // Get all the svcScheduleUnitRecordList where createBy equals to UPDATED_CREATE_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("createBy.in=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createBy is not null
        defaultSvcScheduleUnitRecordShouldBeFound("createBy.specified=true");

        // Get all the svcScheduleUnitRecordList where createBy is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("createBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createBy contains DEFAULT_CREATE_BY
        defaultSvcScheduleUnitRecordShouldBeFound("createBy.contains=" + DEFAULT_CREATE_BY);

        // Get all the svcScheduleUnitRecordList where createBy contains UPDATED_CREATE_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("createBy.contains=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByNotContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createBy does not contain DEFAULT_CREATE_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("createBy.doesNotContain=" + DEFAULT_CREATE_BY);

        // Get all the svcScheduleUnitRecordList where createBy does not contain UPDATED_CREATE_BY
        defaultSvcScheduleUnitRecordShouldBeFound("createBy.doesNotContain=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createById equals to DEFAULT_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("createById.equals=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcScheduleUnitRecordList where createById equals to UPDATED_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("createById.equals=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createById not equals to DEFAULT_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("createById.notEquals=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcScheduleUnitRecordList where createById not equals to UPDATED_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("createById.notEquals=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createById in DEFAULT_CREATE_BY_ID or UPDATED_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("createById.in=" + DEFAULT_CREATE_BY_ID + "," + UPDATED_CREATE_BY_ID);

        // Get all the svcScheduleUnitRecordList where createById equals to UPDATED_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("createById.in=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createById is not null
        defaultSvcScheduleUnitRecordShouldBeFound("createById.specified=true");

        // Get all the svcScheduleUnitRecordList where createById is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("createById.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createById is greater than or equal to DEFAULT_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("createById.greaterThanOrEqual=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcScheduleUnitRecordList where createById is greater than or equal to UPDATED_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("createById.greaterThanOrEqual=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createById is less than or equal to DEFAULT_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("createById.lessThanOrEqual=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcScheduleUnitRecordList where createById is less than or equal to SMALLER_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("createById.lessThanOrEqual=" + SMALLER_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createById is less than DEFAULT_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("createById.lessThan=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcScheduleUnitRecordList where createById is less than UPDATED_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("createById.lessThan=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByCreateByIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where createById is greater than DEFAULT_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("createById.greaterThan=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcScheduleUnitRecordList where createById is greater than SMALLER_CREATE_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("createById.greaterThan=" + SMALLER_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy is not null
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedBy.specified=true");

        // Get all the svcScheduleUnitRecordList where lastModifiedBy is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the svcScheduleUnitRecordList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedById equals to DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedById.equals=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcScheduleUnitRecordList where lastModifiedById equals to UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedById.equals=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedById not equals to DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedById.notEquals=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcScheduleUnitRecordList where lastModifiedById not equals to UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedById.notEquals=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedById in DEFAULT_LAST_MODIFIED_BY_ID or UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedById.in=" + DEFAULT_LAST_MODIFIED_BY_ID + "," + UPDATED_LAST_MODIFIED_BY_ID);

        // Get all the svcScheduleUnitRecordList where lastModifiedById equals to UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedById.in=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is not null
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedById.specified=true");

        // Get all the svcScheduleUnitRecordList where lastModifiedById is null
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedById.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is greater than or equal to DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedById.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is greater than or equal to UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedById.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is less than or equal to DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedById.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is less than or equal to SMALLER_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedById.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is less than DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedById.lessThan=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is less than UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedById.lessThan=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsByLastModifiedByIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is greater than DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldNotBeFound("lastModifiedById.greaterThan=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcScheduleUnitRecordList where lastModifiedById is greater than SMALLER_LAST_MODIFIED_BY_ID
        defaultSvcScheduleUnitRecordShouldBeFound("lastModifiedById.greaterThan=" + SMALLER_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnitRecordsBySvcSchedulePlanRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);
        SvcSchedulePlanRecord svcSchedulePlanRecord = SvcSchedulePlanRecordResourceIT.createEntity(em);
        em.persist(svcSchedulePlanRecord);
        em.flush();
        svcScheduleUnitRecord.addSvcSchedulePlanRecord(svcSchedulePlanRecord);
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);
        Long svcSchedulePlanRecordId = svcSchedulePlanRecord.getId();

        // Get all the svcScheduleUnitRecordList where svcSchedulePlanRecord equals to svcSchedulePlanRecordId
        defaultSvcScheduleUnitRecordShouldBeFound("svcSchedulePlanRecordId.equals=" + svcSchedulePlanRecordId);

        // Get all the svcScheduleUnitRecordList where svcSchedulePlanRecord equals to (svcSchedulePlanRecordId + 1)
        defaultSvcScheduleUnitRecordShouldNotBeFound("svcSchedulePlanRecordId.equals=" + (svcSchedulePlanRecordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcScheduleUnitRecordShouldBeFound(String filter) throws Exception {
        restSvcScheduleUnitRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcScheduleUnitRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].svcScheduleUnitId").value(hasItem(DEFAULT_SVC_SCHEDULE_UNIT_ID)))
            .andExpect(jsonPath("$.[*].isPublished").value(hasItem(DEFAULT_IS_PUBLISHED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED)))
            .andExpect(jsonPath("$.[*].approvedId").value(hasItem(DEFAULT_APPROVED_ID)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createById").value(hasItem(DEFAULT_CREATE_BY_ID)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedById").value(hasItem(DEFAULT_LAST_MODIFIED_BY_ID)));

        // Check, that the count call also returns 1
        restSvcScheduleUnitRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcScheduleUnitRecordShouldNotBeFound(String filter) throws Exception {
        restSvcScheduleUnitRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcScheduleUnitRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcScheduleUnitRecord() throws Exception {
        // Get the svcScheduleUnitRecord
        restSvcScheduleUnitRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcScheduleUnitRecord() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();

        // Update the svcScheduleUnitRecord
        SvcScheduleUnitRecord updatedSvcScheduleUnitRecord = svcScheduleUnitRecordRepository.findById(svcScheduleUnitRecord.getId()).get();
        // Disconnect from session so that the updates on updatedSvcScheduleUnitRecord are not directly saved in db
        em.detach(updatedSvcScheduleUnitRecord);
        updatedSvcScheduleUnitRecord
            .svcScheduleUnitId(UPDATED_SVC_SCHEDULE_UNIT_ID)
            .isPublished(UPDATED_IS_PUBLISHED)
            .status(UPDATED_STATUS)
            .approved(UPDATED_APPROVED)
            .approvedId(UPDATED_APPROVED_ID)
            .comment(UPDATED_COMMENT)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createBy(UPDATED_CREATE_BY)
            .createById(UPDATED_CREATE_BY_ID)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedById(UPDATED_LAST_MODIFIED_BY_ID);
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(updatedSvcScheduleUnitRecord);

        restSvcScheduleUnitRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcScheduleUnitRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
        SvcScheduleUnitRecord testSvcScheduleUnitRecord = svcScheduleUnitRecordList.get(svcScheduleUnitRecordList.size() - 1);
        assertThat(testSvcScheduleUnitRecord.getSvcScheduleUnitId()).isEqualTo(UPDATED_SVC_SCHEDULE_UNIT_ID);
        assertThat(testSvcScheduleUnitRecord.getIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testSvcScheduleUnitRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcScheduleUnitRecord.getApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testSvcScheduleUnitRecord.getApprovedId()).isEqualTo(UPDATED_APPROVED_ID);
        assertThat(testSvcScheduleUnitRecord.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testSvcScheduleUnitRecord.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSvcScheduleUnitRecord.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcScheduleUnitRecord.getCreateById()).isEqualTo(UPDATED_CREATE_BY_ID);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedById()).isEqualTo(UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void putNonExistingSvcScheduleUnitRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();
        svcScheduleUnitRecord.setId(count.incrementAndGet());

        // Create the SvcScheduleUnitRecord
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcScheduleUnitRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcScheduleUnitRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcScheduleUnitRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();
        svcScheduleUnitRecord.setId(count.incrementAndGet());

        // Create the SvcScheduleUnitRecord
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcScheduleUnitRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcScheduleUnitRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();
        svcScheduleUnitRecord.setId(count.incrementAndGet());

        // Create the SvcScheduleUnitRecord
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcScheduleUnitRecordMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcScheduleUnitRecordWithPatch() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();

        // Update the svcScheduleUnitRecord using partial update
        SvcScheduleUnitRecord partialUpdatedSvcScheduleUnitRecord = new SvcScheduleUnitRecord();
        partialUpdatedSvcScheduleUnitRecord.setId(svcScheduleUnitRecord.getId());

        partialUpdatedSvcScheduleUnitRecord
            .svcScheduleUnitId(UPDATED_SVC_SCHEDULE_UNIT_ID)
            .approved(UPDATED_APPROVED)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createBy(UPDATED_CREATE_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSvcScheduleUnitRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcScheduleUnitRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcScheduleUnitRecord))
            )
            .andExpect(status().isOk());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
        SvcScheduleUnitRecord testSvcScheduleUnitRecord = svcScheduleUnitRecordList.get(svcScheduleUnitRecordList.size() - 1);
        assertThat(testSvcScheduleUnitRecord.getSvcScheduleUnitId()).isEqualTo(UPDATED_SVC_SCHEDULE_UNIT_ID);
        assertThat(testSvcScheduleUnitRecord.getIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
        assertThat(testSvcScheduleUnitRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSvcScheduleUnitRecord.getApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testSvcScheduleUnitRecord.getApprovedId()).isEqualTo(DEFAULT_APPROVED_ID);
        assertThat(testSvcScheduleUnitRecord.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testSvcScheduleUnitRecord.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSvcScheduleUnitRecord.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcScheduleUnitRecord.getCreateById()).isEqualTo(DEFAULT_CREATE_BY_ID);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedById()).isEqualTo(DEFAULT_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void fullUpdateSvcScheduleUnitRecordWithPatch() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();

        // Update the svcScheduleUnitRecord using partial update
        SvcScheduleUnitRecord partialUpdatedSvcScheduleUnitRecord = new SvcScheduleUnitRecord();
        partialUpdatedSvcScheduleUnitRecord.setId(svcScheduleUnitRecord.getId());

        partialUpdatedSvcScheduleUnitRecord
            .svcScheduleUnitId(UPDATED_SVC_SCHEDULE_UNIT_ID)
            .isPublished(UPDATED_IS_PUBLISHED)
            .status(UPDATED_STATUS)
            .approved(UPDATED_APPROVED)
            .approvedId(UPDATED_APPROVED_ID)
            .comment(UPDATED_COMMENT)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createBy(UPDATED_CREATE_BY)
            .createById(UPDATED_CREATE_BY_ID)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedById(UPDATED_LAST_MODIFIED_BY_ID);

        restSvcScheduleUnitRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcScheduleUnitRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcScheduleUnitRecord))
            )
            .andExpect(status().isOk());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
        SvcScheduleUnitRecord testSvcScheduleUnitRecord = svcScheduleUnitRecordList.get(svcScheduleUnitRecordList.size() - 1);
        assertThat(testSvcScheduleUnitRecord.getSvcScheduleUnitId()).isEqualTo(UPDATED_SVC_SCHEDULE_UNIT_ID);
        assertThat(testSvcScheduleUnitRecord.getIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testSvcScheduleUnitRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcScheduleUnitRecord.getApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testSvcScheduleUnitRecord.getApprovedId()).isEqualTo(UPDATED_APPROVED_ID);
        assertThat(testSvcScheduleUnitRecord.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testSvcScheduleUnitRecord.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSvcScheduleUnitRecord.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcScheduleUnitRecord.getCreateById()).isEqualTo(UPDATED_CREATE_BY_ID);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSvcScheduleUnitRecord.getLastModifiedById()).isEqualTo(UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSvcScheduleUnitRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();
        svcScheduleUnitRecord.setId(count.incrementAndGet());

        // Create the SvcScheduleUnitRecord
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcScheduleUnitRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcScheduleUnitRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcScheduleUnitRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();
        svcScheduleUnitRecord.setId(count.incrementAndGet());

        // Create the SvcScheduleUnitRecord
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcScheduleUnitRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcScheduleUnitRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRecordRepository.findAll().size();
        svcScheduleUnitRecord.setId(count.incrementAndGet());

        // Create the SvcScheduleUnitRecord
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO = svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcScheduleUnitRecordMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcScheduleUnitRecord in the database
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcScheduleUnitRecord() throws Exception {
        // Initialize the database
        svcScheduleUnitRecordRepository.saveAndFlush(svcScheduleUnitRecord);

        int databaseSizeBeforeDelete = svcScheduleUnitRecordRepository.findAll().size();

        // Delete the svcScheduleUnitRecord
        restSvcScheduleUnitRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcScheduleUnitRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcScheduleUnitRecord> svcScheduleUnitRecordList = svcScheduleUnitRecordRepository.findAll();
        assertThat(svcScheduleUnitRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
