package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcSchedulePlanRecord;
import com.overnetcontact.dvvs.domain.SvcScheduleUnitRecord;
import com.overnetcontact.dvvs.repository.SvcSchedulePlanRecordRepository;
import com.overnetcontact.dvvs.service.SvcSchedulePlanRecordService;
import com.overnetcontact.dvvs.service.criteria.SvcSchedulePlanRecordCriteria;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanRecordDTO;
import com.overnetcontact.dvvs.service.mapper.SvcSchedulePlanRecordMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SvcSchedulePlanRecordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SvcSchedulePlanRecordResourceIT {

    private static final Integer DEFAULT_SVC_SCHEDULE_PLAN_ID = 1;
    private static final Integer UPDATED_SVC_SCHEDULE_PLAN_ID = 2;
    private static final Integer SMALLER_SVC_SCHEDULE_PLAN_ID = 1 - 1;

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

    private static final String ENTITY_API_URL = "/api/svc-schedule-plan-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcSchedulePlanRecordRepository svcSchedulePlanRecordRepository;

    @Mock
    private SvcSchedulePlanRecordRepository svcSchedulePlanRecordRepositoryMock;

    @Autowired
    private SvcSchedulePlanRecordMapper svcSchedulePlanRecordMapper;

    @Mock
    private SvcSchedulePlanRecordService svcSchedulePlanRecordServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcSchedulePlanRecordMockMvc;

    private SvcSchedulePlanRecord svcSchedulePlanRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcSchedulePlanRecord createEntity(EntityManager em) {
        SvcSchedulePlanRecord svcSchedulePlanRecord = new SvcSchedulePlanRecord()
            .svcSchedulePlanId(DEFAULT_SVC_SCHEDULE_PLAN_ID)
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
        return svcSchedulePlanRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcSchedulePlanRecord createUpdatedEntity(EntityManager em) {
        SvcSchedulePlanRecord svcSchedulePlanRecord = new SvcSchedulePlanRecord()
            .svcSchedulePlanId(UPDATED_SVC_SCHEDULE_PLAN_ID)
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
        return svcSchedulePlanRecord;
    }

    @BeforeEach
    public void initTest() {
        svcSchedulePlanRecord = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcSchedulePlanRecord() throws Exception {
        int databaseSizeBeforeCreate = svcSchedulePlanRecordRepository.findAll().size();
        // Create the SvcSchedulePlanRecord
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);
        restSvcSchedulePlanRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeCreate + 1);
        SvcSchedulePlanRecord testSvcSchedulePlanRecord = svcSchedulePlanRecordList.get(svcSchedulePlanRecordList.size() - 1);
        assertThat(testSvcSchedulePlanRecord.getSvcSchedulePlanId()).isEqualTo(DEFAULT_SVC_SCHEDULE_PLAN_ID);
        assertThat(testSvcSchedulePlanRecord.getIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
        assertThat(testSvcSchedulePlanRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSvcSchedulePlanRecord.getApproved()).isEqualTo(DEFAULT_APPROVED);
        assertThat(testSvcSchedulePlanRecord.getApprovedId()).isEqualTo(DEFAULT_APPROVED_ID);
        assertThat(testSvcSchedulePlanRecord.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testSvcSchedulePlanRecord.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSvcSchedulePlanRecord.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testSvcSchedulePlanRecord.getCreateById()).isEqualTo(DEFAULT_CREATE_BY_ID);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedById()).isEqualTo(DEFAULT_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void createSvcSchedulePlanRecordWithExistingId() throws Exception {
        // Create the SvcSchedulePlanRecord with an existing ID
        svcSchedulePlanRecord.setId(1L);
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        int databaseSizeBeforeCreate = svcSchedulePlanRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcSchedulePlanRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSvcSchedulePlanIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcSchedulePlanRecordRepository.findAll().size();
        // set the field null
        svcSchedulePlanRecord.setSvcSchedulePlanId(null);

        // Create the SvcSchedulePlanRecord, which fails.
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        restSvcSchedulePlanRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPublishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcSchedulePlanRecordRepository.findAll().size();
        // set the field null
        svcSchedulePlanRecord.setIsPublished(null);

        // Create the SvcSchedulePlanRecord, which fails.
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        restSvcSchedulePlanRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcSchedulePlanRecordRepository.findAll().size();
        // set the field null
        svcSchedulePlanRecord.setStatus(null);

        // Create the SvcSchedulePlanRecord, which fails.
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        restSvcSchedulePlanRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecords() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList
        restSvcSchedulePlanRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcSchedulePlanRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].svcSchedulePlanId").value(hasItem(DEFAULT_SVC_SCHEDULE_PLAN_ID)))
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

    @SuppressWarnings({ "unchecked" })
    void getAllSvcSchedulePlanRecordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(svcSchedulePlanRecordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSvcSchedulePlanRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(svcSchedulePlanRecordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSvcSchedulePlanRecordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(svcSchedulePlanRecordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSvcSchedulePlanRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(svcSchedulePlanRecordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSvcSchedulePlanRecord() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get the svcSchedulePlanRecord
        restSvcSchedulePlanRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, svcSchedulePlanRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcSchedulePlanRecord.getId().intValue()))
            .andExpect(jsonPath("$.svcSchedulePlanId").value(DEFAULT_SVC_SCHEDULE_PLAN_ID))
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
    void getSvcSchedulePlanRecordsByIdFiltering() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        Long id = svcSchedulePlanRecord.getId();

        defaultSvcSchedulePlanRecordShouldBeFound("id.equals=" + id);
        defaultSvcSchedulePlanRecordShouldNotBeFound("id.notEquals=" + id);

        defaultSvcSchedulePlanRecordShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcSchedulePlanRecordShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcSchedulePlanRecordShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcSchedulePlanRecordShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcSchedulePlanIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId equals to DEFAULT_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldBeFound("svcSchedulePlanId.equals=" + DEFAULT_SVC_SCHEDULE_PLAN_ID);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId equals to UPDATED_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcSchedulePlanId.equals=" + UPDATED_SVC_SCHEDULE_PLAN_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcSchedulePlanIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId not equals to DEFAULT_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcSchedulePlanId.notEquals=" + DEFAULT_SVC_SCHEDULE_PLAN_ID);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId not equals to UPDATED_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldBeFound("svcSchedulePlanId.notEquals=" + UPDATED_SVC_SCHEDULE_PLAN_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcSchedulePlanIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId in DEFAULT_SVC_SCHEDULE_PLAN_ID or UPDATED_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldBeFound(
            "svcSchedulePlanId.in=" + DEFAULT_SVC_SCHEDULE_PLAN_ID + "," + UPDATED_SVC_SCHEDULE_PLAN_ID
        );

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId equals to UPDATED_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcSchedulePlanId.in=" + UPDATED_SVC_SCHEDULE_PLAN_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcSchedulePlanIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is not null
        defaultSvcSchedulePlanRecordShouldBeFound("svcSchedulePlanId.specified=true");

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcSchedulePlanId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcSchedulePlanIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is greater than or equal to DEFAULT_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldBeFound("svcSchedulePlanId.greaterThanOrEqual=" + DEFAULT_SVC_SCHEDULE_PLAN_ID);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is greater than or equal to UPDATED_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcSchedulePlanId.greaterThanOrEqual=" + UPDATED_SVC_SCHEDULE_PLAN_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcSchedulePlanIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is less than or equal to DEFAULT_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldBeFound("svcSchedulePlanId.lessThanOrEqual=" + DEFAULT_SVC_SCHEDULE_PLAN_ID);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is less than or equal to SMALLER_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcSchedulePlanId.lessThanOrEqual=" + SMALLER_SVC_SCHEDULE_PLAN_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcSchedulePlanIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is less than DEFAULT_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcSchedulePlanId.lessThan=" + DEFAULT_SVC_SCHEDULE_PLAN_ID);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is less than UPDATED_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldBeFound("svcSchedulePlanId.lessThan=" + UPDATED_SVC_SCHEDULE_PLAN_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcSchedulePlanIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is greater than DEFAULT_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcSchedulePlanId.greaterThan=" + DEFAULT_SVC_SCHEDULE_PLAN_ID);

        // Get all the svcSchedulePlanRecordList where svcSchedulePlanId is greater than SMALLER_SVC_SCHEDULE_PLAN_ID
        defaultSvcSchedulePlanRecordShouldBeFound("svcSchedulePlanId.greaterThan=" + SMALLER_SVC_SCHEDULE_PLAN_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByIsPublishedIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where isPublished equals to DEFAULT_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldBeFound("isPublished.equals=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcSchedulePlanRecordList where isPublished equals to UPDATED_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldNotBeFound("isPublished.equals=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByIsPublishedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where isPublished not equals to DEFAULT_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldNotBeFound("isPublished.notEquals=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcSchedulePlanRecordList where isPublished not equals to UPDATED_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldBeFound("isPublished.notEquals=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByIsPublishedIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where isPublished in DEFAULT_IS_PUBLISHED or UPDATED_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldBeFound("isPublished.in=" + DEFAULT_IS_PUBLISHED + "," + UPDATED_IS_PUBLISHED);

        // Get all the svcSchedulePlanRecordList where isPublished equals to UPDATED_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldNotBeFound("isPublished.in=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByIsPublishedIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where isPublished is not null
        defaultSvcSchedulePlanRecordShouldBeFound("isPublished.specified=true");

        // Get all the svcSchedulePlanRecordList where isPublished is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("isPublished.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByIsPublishedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where isPublished is greater than or equal to DEFAULT_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldBeFound("isPublished.greaterThanOrEqual=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcSchedulePlanRecordList where isPublished is greater than or equal to UPDATED_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldNotBeFound("isPublished.greaterThanOrEqual=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByIsPublishedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where isPublished is less than or equal to DEFAULT_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldBeFound("isPublished.lessThanOrEqual=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcSchedulePlanRecordList where isPublished is less than or equal to SMALLER_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldNotBeFound("isPublished.lessThanOrEqual=" + SMALLER_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByIsPublishedIsLessThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where isPublished is less than DEFAULT_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldNotBeFound("isPublished.lessThan=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcSchedulePlanRecordList where isPublished is less than UPDATED_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldBeFound("isPublished.lessThan=" + UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByIsPublishedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where isPublished is greater than DEFAULT_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldNotBeFound("isPublished.greaterThan=" + DEFAULT_IS_PUBLISHED);

        // Get all the svcSchedulePlanRecordList where isPublished is greater than SMALLER_IS_PUBLISHED
        defaultSvcSchedulePlanRecordShouldBeFound("isPublished.greaterThan=" + SMALLER_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where status equals to DEFAULT_STATUS
        defaultSvcSchedulePlanRecordShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the svcSchedulePlanRecordList where status equals to UPDATED_STATUS
        defaultSvcSchedulePlanRecordShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where status not equals to DEFAULT_STATUS
        defaultSvcSchedulePlanRecordShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the svcSchedulePlanRecordList where status not equals to UPDATED_STATUS
        defaultSvcSchedulePlanRecordShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSvcSchedulePlanRecordShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the svcSchedulePlanRecordList where status equals to UPDATED_STATUS
        defaultSvcSchedulePlanRecordShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where status is not null
        defaultSvcSchedulePlanRecordShouldBeFound("status.specified=true");

        // Get all the svcSchedulePlanRecordList where status is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByStatusContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where status contains DEFAULT_STATUS
        defaultSvcSchedulePlanRecordShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the svcSchedulePlanRecordList where status contains UPDATED_STATUS
        defaultSvcSchedulePlanRecordShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where status does not contain DEFAULT_STATUS
        defaultSvcSchedulePlanRecordShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the svcSchedulePlanRecordList where status does not contain UPDATED_STATUS
        defaultSvcSchedulePlanRecordShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approved equals to DEFAULT_APPROVED
        defaultSvcSchedulePlanRecordShouldBeFound("approved.equals=" + DEFAULT_APPROVED);

        // Get all the svcSchedulePlanRecordList where approved equals to UPDATED_APPROVED
        defaultSvcSchedulePlanRecordShouldNotBeFound("approved.equals=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approved not equals to DEFAULT_APPROVED
        defaultSvcSchedulePlanRecordShouldNotBeFound("approved.notEquals=" + DEFAULT_APPROVED);

        // Get all the svcSchedulePlanRecordList where approved not equals to UPDATED_APPROVED
        defaultSvcSchedulePlanRecordShouldBeFound("approved.notEquals=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approved in DEFAULT_APPROVED or UPDATED_APPROVED
        defaultSvcSchedulePlanRecordShouldBeFound("approved.in=" + DEFAULT_APPROVED + "," + UPDATED_APPROVED);

        // Get all the svcSchedulePlanRecordList where approved equals to UPDATED_APPROVED
        defaultSvcSchedulePlanRecordShouldNotBeFound("approved.in=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approved is not null
        defaultSvcSchedulePlanRecordShouldBeFound("approved.specified=true");

        // Get all the svcSchedulePlanRecordList where approved is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("approved.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approved contains DEFAULT_APPROVED
        defaultSvcSchedulePlanRecordShouldBeFound("approved.contains=" + DEFAULT_APPROVED);

        // Get all the svcSchedulePlanRecordList where approved contains UPDATED_APPROVED
        defaultSvcSchedulePlanRecordShouldNotBeFound("approved.contains=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedNotContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approved does not contain DEFAULT_APPROVED
        defaultSvcSchedulePlanRecordShouldNotBeFound("approved.doesNotContain=" + DEFAULT_APPROVED);

        // Get all the svcSchedulePlanRecordList where approved does not contain UPDATED_APPROVED
        defaultSvcSchedulePlanRecordShouldBeFound("approved.doesNotContain=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approvedId equals to DEFAULT_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldBeFound("approvedId.equals=" + DEFAULT_APPROVED_ID);

        // Get all the svcSchedulePlanRecordList where approvedId equals to UPDATED_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("approvedId.equals=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approvedId not equals to DEFAULT_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("approvedId.notEquals=" + DEFAULT_APPROVED_ID);

        // Get all the svcSchedulePlanRecordList where approvedId not equals to UPDATED_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldBeFound("approvedId.notEquals=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approvedId in DEFAULT_APPROVED_ID or UPDATED_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldBeFound("approvedId.in=" + DEFAULT_APPROVED_ID + "," + UPDATED_APPROVED_ID);

        // Get all the svcSchedulePlanRecordList where approvedId equals to UPDATED_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("approvedId.in=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approvedId is not null
        defaultSvcSchedulePlanRecordShouldBeFound("approvedId.specified=true");

        // Get all the svcSchedulePlanRecordList where approvedId is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("approvedId.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approvedId is greater than or equal to DEFAULT_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldBeFound("approvedId.greaterThanOrEqual=" + DEFAULT_APPROVED_ID);

        // Get all the svcSchedulePlanRecordList where approvedId is greater than or equal to UPDATED_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("approvedId.greaterThanOrEqual=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approvedId is less than or equal to DEFAULT_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldBeFound("approvedId.lessThanOrEqual=" + DEFAULT_APPROVED_ID);

        // Get all the svcSchedulePlanRecordList where approvedId is less than or equal to SMALLER_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("approvedId.lessThanOrEqual=" + SMALLER_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approvedId is less than DEFAULT_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("approvedId.lessThan=" + DEFAULT_APPROVED_ID);

        // Get all the svcSchedulePlanRecordList where approvedId is less than UPDATED_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldBeFound("approvedId.lessThan=" + UPDATED_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByApprovedIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where approvedId is greater than DEFAULT_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("approvedId.greaterThan=" + DEFAULT_APPROVED_ID);

        // Get all the svcSchedulePlanRecordList where approvedId is greater than SMALLER_APPROVED_ID
        defaultSvcSchedulePlanRecordShouldBeFound("approvedId.greaterThan=" + SMALLER_APPROVED_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where comment equals to DEFAULT_COMMENT
        defaultSvcSchedulePlanRecordShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the svcSchedulePlanRecordList where comment equals to UPDATED_COMMENT
        defaultSvcSchedulePlanRecordShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCommentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where comment not equals to DEFAULT_COMMENT
        defaultSvcSchedulePlanRecordShouldNotBeFound("comment.notEquals=" + DEFAULT_COMMENT);

        // Get all the svcSchedulePlanRecordList where comment not equals to UPDATED_COMMENT
        defaultSvcSchedulePlanRecordShouldBeFound("comment.notEquals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultSvcSchedulePlanRecordShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the svcSchedulePlanRecordList where comment equals to UPDATED_COMMENT
        defaultSvcSchedulePlanRecordShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where comment is not null
        defaultSvcSchedulePlanRecordShouldBeFound("comment.specified=true");

        // Get all the svcSchedulePlanRecordList where comment is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCommentContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where comment contains DEFAULT_COMMENT
        defaultSvcSchedulePlanRecordShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the svcSchedulePlanRecordList where comment contains UPDATED_COMMENT
        defaultSvcSchedulePlanRecordShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where comment does not contain DEFAULT_COMMENT
        defaultSvcSchedulePlanRecordShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the svcSchedulePlanRecordList where comment does not contain UPDATED_COMMENT
        defaultSvcSchedulePlanRecordShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createDate equals to DEFAULT_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the svcSchedulePlanRecordList where createDate equals to UPDATED_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the svcSchedulePlanRecordList where createDate not equals to UPDATED_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the svcSchedulePlanRecordList where createDate equals to UPDATED_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createDate is not null
        defaultSvcSchedulePlanRecordShouldBeFound("createDate.specified=true");

        // Get all the svcSchedulePlanRecordList where createDate is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the svcSchedulePlanRecordList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the svcSchedulePlanRecordList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createDate is less than DEFAULT_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the svcSchedulePlanRecordList where createDate is less than UPDATED_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the svcSchedulePlanRecordList where createDate is greater than SMALLER_CREATE_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is not null
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedDate.specified=true");

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the svcSchedulePlanRecordList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createBy equals to DEFAULT_CREATE_BY
        defaultSvcSchedulePlanRecordShouldBeFound("createBy.equals=" + DEFAULT_CREATE_BY);

        // Get all the svcSchedulePlanRecordList where createBy equals to UPDATED_CREATE_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("createBy.equals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createBy not equals to DEFAULT_CREATE_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("createBy.notEquals=" + DEFAULT_CREATE_BY);

        // Get all the svcSchedulePlanRecordList where createBy not equals to UPDATED_CREATE_BY
        defaultSvcSchedulePlanRecordShouldBeFound("createBy.notEquals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createBy in DEFAULT_CREATE_BY or UPDATED_CREATE_BY
        defaultSvcSchedulePlanRecordShouldBeFound("createBy.in=" + DEFAULT_CREATE_BY + "," + UPDATED_CREATE_BY);

        // Get all the svcSchedulePlanRecordList where createBy equals to UPDATED_CREATE_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("createBy.in=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createBy is not null
        defaultSvcSchedulePlanRecordShouldBeFound("createBy.specified=true");

        // Get all the svcSchedulePlanRecordList where createBy is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("createBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createBy contains DEFAULT_CREATE_BY
        defaultSvcSchedulePlanRecordShouldBeFound("createBy.contains=" + DEFAULT_CREATE_BY);

        // Get all the svcSchedulePlanRecordList where createBy contains UPDATED_CREATE_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("createBy.contains=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByNotContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createBy does not contain DEFAULT_CREATE_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("createBy.doesNotContain=" + DEFAULT_CREATE_BY);

        // Get all the svcSchedulePlanRecordList where createBy does not contain UPDATED_CREATE_BY
        defaultSvcSchedulePlanRecordShouldBeFound("createBy.doesNotContain=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createById equals to DEFAULT_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("createById.equals=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcSchedulePlanRecordList where createById equals to UPDATED_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("createById.equals=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createById not equals to DEFAULT_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("createById.notEquals=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcSchedulePlanRecordList where createById not equals to UPDATED_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("createById.notEquals=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createById in DEFAULT_CREATE_BY_ID or UPDATED_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("createById.in=" + DEFAULT_CREATE_BY_ID + "," + UPDATED_CREATE_BY_ID);

        // Get all the svcSchedulePlanRecordList where createById equals to UPDATED_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("createById.in=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createById is not null
        defaultSvcSchedulePlanRecordShouldBeFound("createById.specified=true");

        // Get all the svcSchedulePlanRecordList where createById is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("createById.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createById is greater than or equal to DEFAULT_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("createById.greaterThanOrEqual=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcSchedulePlanRecordList where createById is greater than or equal to UPDATED_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("createById.greaterThanOrEqual=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createById is less than or equal to DEFAULT_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("createById.lessThanOrEqual=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcSchedulePlanRecordList where createById is less than or equal to SMALLER_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("createById.lessThanOrEqual=" + SMALLER_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createById is less than DEFAULT_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("createById.lessThan=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcSchedulePlanRecordList where createById is less than UPDATED_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("createById.lessThan=" + UPDATED_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByCreateByIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where createById is greater than DEFAULT_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("createById.greaterThan=" + DEFAULT_CREATE_BY_ID);

        // Get all the svcSchedulePlanRecordList where createById is greater than SMALLER_CREATE_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("createById.greaterThan=" + SMALLER_CREATE_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy is not null
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedBy.specified=true");

        // Get all the svcSchedulePlanRecordList where lastModifiedBy is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the svcSchedulePlanRecordList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIdIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedById equals to DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedById.equals=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcSchedulePlanRecordList where lastModifiedById equals to UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedById.equals=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedById not equals to DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedById.notEquals=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcSchedulePlanRecordList where lastModifiedById not equals to UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedById.notEquals=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIdIsInShouldWork() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedById in DEFAULT_LAST_MODIFIED_BY_ID or UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedById.in=" + DEFAULT_LAST_MODIFIED_BY_ID + "," + UPDATED_LAST_MODIFIED_BY_ID);

        // Get all the svcSchedulePlanRecordList where lastModifiedById equals to UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedById.in=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is not null
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedById.specified=true");

        // Get all the svcSchedulePlanRecordList where lastModifiedById is null
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedById.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is greater than or equal to DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedById.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is greater than or equal to UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedById.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is less than or equal to DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedById.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is less than or equal to SMALLER_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedById.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIdIsLessThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is less than DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedById.lessThan=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is less than UPDATED_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedById.lessThan=" + UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsByLastModifiedByIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is greater than DEFAULT_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldNotBeFound("lastModifiedById.greaterThan=" + DEFAULT_LAST_MODIFIED_BY_ID);

        // Get all the svcSchedulePlanRecordList where lastModifiedById is greater than SMALLER_LAST_MODIFIED_BY_ID
        defaultSvcSchedulePlanRecordShouldBeFound("lastModifiedById.greaterThan=" + SMALLER_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlanRecordsBySvcScheduleUnitRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);
        SvcScheduleUnitRecord svcScheduleUnitRecord = SvcScheduleUnitRecordResourceIT.createEntity(em);
        em.persist(svcScheduleUnitRecord);
        em.flush();
        svcSchedulePlanRecord.addSvcScheduleUnitRecord(svcScheduleUnitRecord);
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);
        Long svcScheduleUnitRecordId = svcScheduleUnitRecord.getId();

        // Get all the svcSchedulePlanRecordList where svcScheduleUnitRecord equals to svcScheduleUnitRecordId
        defaultSvcSchedulePlanRecordShouldBeFound("svcScheduleUnitRecordId.equals=" + svcScheduleUnitRecordId);

        // Get all the svcSchedulePlanRecordList where svcScheduleUnitRecord equals to (svcScheduleUnitRecordId + 1)
        defaultSvcSchedulePlanRecordShouldNotBeFound("svcScheduleUnitRecordId.equals=" + (svcScheduleUnitRecordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcSchedulePlanRecordShouldBeFound(String filter) throws Exception {
        restSvcSchedulePlanRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcSchedulePlanRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].svcSchedulePlanId").value(hasItem(DEFAULT_SVC_SCHEDULE_PLAN_ID)))
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
        restSvcSchedulePlanRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcSchedulePlanRecordShouldNotBeFound(String filter) throws Exception {
        restSvcSchedulePlanRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcSchedulePlanRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcSchedulePlanRecord() throws Exception {
        // Get the svcSchedulePlanRecord
        restSvcSchedulePlanRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcSchedulePlanRecord() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();

        // Update the svcSchedulePlanRecord
        SvcSchedulePlanRecord updatedSvcSchedulePlanRecord = svcSchedulePlanRecordRepository.findById(svcSchedulePlanRecord.getId()).get();
        // Disconnect from session so that the updates on updatedSvcSchedulePlanRecord are not directly saved in db
        em.detach(updatedSvcSchedulePlanRecord);
        updatedSvcSchedulePlanRecord
            .svcSchedulePlanId(UPDATED_SVC_SCHEDULE_PLAN_ID)
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
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(updatedSvcSchedulePlanRecord);

        restSvcSchedulePlanRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcSchedulePlanRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
        SvcSchedulePlanRecord testSvcSchedulePlanRecord = svcSchedulePlanRecordList.get(svcSchedulePlanRecordList.size() - 1);
        assertThat(testSvcSchedulePlanRecord.getSvcSchedulePlanId()).isEqualTo(UPDATED_SVC_SCHEDULE_PLAN_ID);
        assertThat(testSvcSchedulePlanRecord.getIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testSvcSchedulePlanRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcSchedulePlanRecord.getApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testSvcSchedulePlanRecord.getApprovedId()).isEqualTo(UPDATED_APPROVED_ID);
        assertThat(testSvcSchedulePlanRecord.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testSvcSchedulePlanRecord.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSvcSchedulePlanRecord.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcSchedulePlanRecord.getCreateById()).isEqualTo(UPDATED_CREATE_BY_ID);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedById()).isEqualTo(UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void putNonExistingSvcSchedulePlanRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();
        svcSchedulePlanRecord.setId(count.incrementAndGet());

        // Create the SvcSchedulePlanRecord
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcSchedulePlanRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcSchedulePlanRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcSchedulePlanRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();
        svcSchedulePlanRecord.setId(count.incrementAndGet());

        // Create the SvcSchedulePlanRecord
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSchedulePlanRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcSchedulePlanRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();
        svcSchedulePlanRecord.setId(count.incrementAndGet());

        // Create the SvcSchedulePlanRecord
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSchedulePlanRecordMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcSchedulePlanRecordWithPatch() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();

        // Update the svcSchedulePlanRecord using partial update
        SvcSchedulePlanRecord partialUpdatedSvcSchedulePlanRecord = new SvcSchedulePlanRecord();
        partialUpdatedSvcSchedulePlanRecord.setId(svcSchedulePlanRecord.getId());

        partialUpdatedSvcSchedulePlanRecord
            .svcSchedulePlanId(UPDATED_SVC_SCHEDULE_PLAN_ID)
            .isPublished(UPDATED_IS_PUBLISHED)
            .status(UPDATED_STATUS)
            .approved(UPDATED_APPROVED)
            .createBy(UPDATED_CREATE_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSvcSchedulePlanRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcSchedulePlanRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcSchedulePlanRecord))
            )
            .andExpect(status().isOk());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
        SvcSchedulePlanRecord testSvcSchedulePlanRecord = svcSchedulePlanRecordList.get(svcSchedulePlanRecordList.size() - 1);
        assertThat(testSvcSchedulePlanRecord.getSvcSchedulePlanId()).isEqualTo(UPDATED_SVC_SCHEDULE_PLAN_ID);
        assertThat(testSvcSchedulePlanRecord.getIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testSvcSchedulePlanRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcSchedulePlanRecord.getApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testSvcSchedulePlanRecord.getApprovedId()).isEqualTo(DEFAULT_APPROVED_ID);
        assertThat(testSvcSchedulePlanRecord.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testSvcSchedulePlanRecord.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSvcSchedulePlanRecord.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcSchedulePlanRecord.getCreateById()).isEqualTo(DEFAULT_CREATE_BY_ID);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedById()).isEqualTo(DEFAULT_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void fullUpdateSvcSchedulePlanRecordWithPatch() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();

        // Update the svcSchedulePlanRecord using partial update
        SvcSchedulePlanRecord partialUpdatedSvcSchedulePlanRecord = new SvcSchedulePlanRecord();
        partialUpdatedSvcSchedulePlanRecord.setId(svcSchedulePlanRecord.getId());

        partialUpdatedSvcSchedulePlanRecord
            .svcSchedulePlanId(UPDATED_SVC_SCHEDULE_PLAN_ID)
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

        restSvcSchedulePlanRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcSchedulePlanRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcSchedulePlanRecord))
            )
            .andExpect(status().isOk());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
        SvcSchedulePlanRecord testSvcSchedulePlanRecord = svcSchedulePlanRecordList.get(svcSchedulePlanRecordList.size() - 1);
        assertThat(testSvcSchedulePlanRecord.getSvcSchedulePlanId()).isEqualTo(UPDATED_SVC_SCHEDULE_PLAN_ID);
        assertThat(testSvcSchedulePlanRecord.getIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testSvcSchedulePlanRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcSchedulePlanRecord.getApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testSvcSchedulePlanRecord.getApprovedId()).isEqualTo(UPDATED_APPROVED_ID);
        assertThat(testSvcSchedulePlanRecord.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testSvcSchedulePlanRecord.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSvcSchedulePlanRecord.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testSvcSchedulePlanRecord.getCreateById()).isEqualTo(UPDATED_CREATE_BY_ID);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSvcSchedulePlanRecord.getLastModifiedById()).isEqualTo(UPDATED_LAST_MODIFIED_BY_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSvcSchedulePlanRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();
        svcSchedulePlanRecord.setId(count.incrementAndGet());

        // Create the SvcSchedulePlanRecord
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcSchedulePlanRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcSchedulePlanRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcSchedulePlanRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();
        svcSchedulePlanRecord.setId(count.incrementAndGet());

        // Create the SvcSchedulePlanRecord
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSchedulePlanRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcSchedulePlanRecord() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRecordRepository.findAll().size();
        svcSchedulePlanRecord.setId(count.incrementAndGet());

        // Create the SvcSchedulePlanRecord
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSchedulePlanRecordMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcSchedulePlanRecord in the database
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcSchedulePlanRecord() throws Exception {
        // Initialize the database
        svcSchedulePlanRecordRepository.saveAndFlush(svcSchedulePlanRecord);

        int databaseSizeBeforeDelete = svcSchedulePlanRecordRepository.findAll().size();

        // Delete the svcSchedulePlanRecord
        restSvcSchedulePlanRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcSchedulePlanRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcSchedulePlanRecord> svcSchedulePlanRecordList = svcSchedulePlanRecordRepository.findAll();
        assertThat(svcSchedulePlanRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
