package com.overnetcontact.dvvs.web.rest;

import static com.overnetcontact.dvvs.web.rest.TestUtil.sameInstant;
import static com.overnetcontact.dvvs.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcScheduleUnit;
import com.overnetcontact.dvvs.domain.enumeration.ScheduleStatus;
import com.overnetcontact.dvvs.repository.SvcScheduleUnitRepository;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitDTO;
import com.overnetcontact.dvvs.service.mapper.SvcScheduleUnitMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link SvcScheduleUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcScheduleUnitResourceIT {

    private static final ZonedDateTime DEFAULT_START_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ACTUAL_START_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTUAL_START_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ACTUAL_END_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ACTUAL_END_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_SCHEDULE_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SCHEDULE_QUANTITY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ACTUAL_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACTUAL_QUANTITY = new BigDecimal(2);

    private static final LocalDate DEFAULT_APPLIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ScheduleStatus DEFAULT_STATUS = ScheduleStatus.PLANED;
    private static final ScheduleStatus UPDATED_STATUS = ScheduleStatus.FINISHED;

    private static final String DEFAULT_PLANED_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_PLANED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_LABORER_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_LABORER_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPERVISOR_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_SUPERVISOR_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-schedule-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcScheduleUnitRepository svcScheduleUnitRepository;

    @Autowired
    private SvcScheduleUnitMapper svcScheduleUnitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcScheduleUnitMockMvc;

    private SvcScheduleUnit svcScheduleUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcScheduleUnit createEntity(EntityManager em) {
        SvcScheduleUnit svcScheduleUnit = new SvcScheduleUnit()
            .startFrom(DEFAULT_START_FROM)
            .endAt(DEFAULT_END_AT)
            .actualStartFrom(DEFAULT_ACTUAL_START_FROM)
            .actualEndAt(DEFAULT_ACTUAL_END_AT)
            .scheduleQuantity(DEFAULT_SCHEDULE_QUANTITY)
            .actualQuantity(DEFAULT_ACTUAL_QUANTITY)
            .appliedDate(DEFAULT_APPLIED_DATE)
            .status(DEFAULT_STATUS)
            .planedNote(DEFAULT_PLANED_NOTE)
            .laborerNote(DEFAULT_LABORER_NOTE)
            .supervisorNote(DEFAULT_SUPERVISOR_NOTE);
        return svcScheduleUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcScheduleUnit createUpdatedEntity(EntityManager em) {
        SvcScheduleUnit svcScheduleUnit = new SvcScheduleUnit()
            .startFrom(UPDATED_START_FROM)
            .endAt(UPDATED_END_AT)
            .actualStartFrom(UPDATED_ACTUAL_START_FROM)
            .actualEndAt(UPDATED_ACTUAL_END_AT)
            .scheduleQuantity(UPDATED_SCHEDULE_QUANTITY)
            .actualQuantity(UPDATED_ACTUAL_QUANTITY)
            .appliedDate(UPDATED_APPLIED_DATE)
            .status(UPDATED_STATUS)
            .planedNote(UPDATED_PLANED_NOTE)
            .laborerNote(UPDATED_LABORER_NOTE)
            .supervisorNote(UPDATED_SUPERVISOR_NOTE);
        return svcScheduleUnit;
    }

    @BeforeEach
    public void initTest() {
        svcScheduleUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcScheduleUnit() throws Exception {
        int databaseSizeBeforeCreate = svcScheduleUnitRepository.findAll().size();
        // Create the SvcScheduleUnit
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);
        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeCreate + 1);
        SvcScheduleUnit testSvcScheduleUnit = svcScheduleUnitList.get(svcScheduleUnitList.size() - 1);
        assertThat(testSvcScheduleUnit.getStartFrom()).isEqualTo(DEFAULT_START_FROM);
        assertThat(testSvcScheduleUnit.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testSvcScheduleUnit.getActualStartFrom()).isEqualTo(DEFAULT_ACTUAL_START_FROM);
        assertThat(testSvcScheduleUnit.getActualEndAt()).isEqualTo(DEFAULT_ACTUAL_END_AT);
        assertThat(testSvcScheduleUnit.getScheduleQuantity()).isEqualByComparingTo(DEFAULT_SCHEDULE_QUANTITY);
        assertThat(testSvcScheduleUnit.getActualQuantity()).isEqualByComparingTo(DEFAULT_ACTUAL_QUANTITY);
        assertThat(testSvcScheduleUnit.getAppliedDate()).isEqualTo(DEFAULT_APPLIED_DATE);
        assertThat(testSvcScheduleUnit.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSvcScheduleUnit.getPlanedNote()).isEqualTo(DEFAULT_PLANED_NOTE);
        assertThat(testSvcScheduleUnit.getLaborerNote()).isEqualTo(DEFAULT_LABORER_NOTE);
        assertThat(testSvcScheduleUnit.getSupervisorNote()).isEqualTo(DEFAULT_SUPERVISOR_NOTE);
    }

    @Test
    @Transactional
    void createSvcScheduleUnitWithExistingId() throws Exception {
        // Create the SvcScheduleUnit with an existing ID
        svcScheduleUnit.setId(1L);
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        int databaseSizeBeforeCreate = svcScheduleUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRepository.findAll().size();
        // set the field null
        svcScheduleUnit.setStartFrom(null);

        // Create the SvcScheduleUnit, which fails.
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRepository.findAll().size();
        // set the field null
        svcScheduleUnit.setEndAt(null);

        // Create the SvcScheduleUnit, which fails.
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActualStartFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRepository.findAll().size();
        // set the field null
        svcScheduleUnit.setActualStartFrom(null);

        // Create the SvcScheduleUnit, which fails.
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActualEndAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRepository.findAll().size();
        // set the field null
        svcScheduleUnit.setActualEndAt(null);

        // Create the SvcScheduleUnit, which fails.
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduleQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRepository.findAll().size();
        // set the field null
        svcScheduleUnit.setScheduleQuantity(null);

        // Create the SvcScheduleUnit, which fails.
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAppliedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRepository.findAll().size();
        // set the field null
        svcScheduleUnit.setAppliedDate(null);

        // Create the SvcScheduleUnit, which fails.
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcScheduleUnitRepository.findAll().size();
        // set the field null
        svcScheduleUnit.setStatus(null);

        // Create the SvcScheduleUnit, which fails.
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        restSvcScheduleUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcScheduleUnits() throws Exception {
        // Initialize the database
        svcScheduleUnitRepository.saveAndFlush(svcScheduleUnit);

        // Get all the svcScheduleUnitList
        restSvcScheduleUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcScheduleUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].startFrom").value(hasItem(sameInstant(DEFAULT_START_FROM))))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(sameInstant(DEFAULT_END_AT))))
            .andExpect(jsonPath("$.[*].actualStartFrom").value(hasItem(sameInstant(DEFAULT_ACTUAL_START_FROM))))
            .andExpect(jsonPath("$.[*].actualEndAt").value(hasItem(sameInstant(DEFAULT_ACTUAL_END_AT))))
            .andExpect(jsonPath("$.[*].scheduleQuantity").value(hasItem(sameNumber(DEFAULT_SCHEDULE_QUANTITY))))
            .andExpect(jsonPath("$.[*].actualQuantity").value(hasItem(sameNumber(DEFAULT_ACTUAL_QUANTITY))))
            .andExpect(jsonPath("$.[*].appliedDate").value(hasItem(DEFAULT_APPLIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].planedNote").value(hasItem(DEFAULT_PLANED_NOTE)))
            .andExpect(jsonPath("$.[*].laborerNote").value(hasItem(DEFAULT_LABORER_NOTE)))
            .andExpect(jsonPath("$.[*].supervisorNote").value(hasItem(DEFAULT_SUPERVISOR_NOTE)));
    }

    @Test
    @Transactional
    void getSvcScheduleUnit() throws Exception {
        // Initialize the database
        svcScheduleUnitRepository.saveAndFlush(svcScheduleUnit);

        // Get the svcScheduleUnit
        restSvcScheduleUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, svcScheduleUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcScheduleUnit.getId().intValue()))
            .andExpect(jsonPath("$.startFrom").value(sameInstant(DEFAULT_START_FROM)))
            .andExpect(jsonPath("$.endAt").value(sameInstant(DEFAULT_END_AT)))
            .andExpect(jsonPath("$.actualStartFrom").value(sameInstant(DEFAULT_ACTUAL_START_FROM)))
            .andExpect(jsonPath("$.actualEndAt").value(sameInstant(DEFAULT_ACTUAL_END_AT)))
            .andExpect(jsonPath("$.scheduleQuantity").value(sameNumber(DEFAULT_SCHEDULE_QUANTITY)))
            .andExpect(jsonPath("$.actualQuantity").value(sameNumber(DEFAULT_ACTUAL_QUANTITY)))
            .andExpect(jsonPath("$.appliedDate").value(DEFAULT_APPLIED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.planedNote").value(DEFAULT_PLANED_NOTE))
            .andExpect(jsonPath("$.laborerNote").value(DEFAULT_LABORER_NOTE))
            .andExpect(jsonPath("$.supervisorNote").value(DEFAULT_SUPERVISOR_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingSvcScheduleUnit() throws Exception {
        // Get the svcScheduleUnit
        restSvcScheduleUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcScheduleUnit() throws Exception {
        // Initialize the database
        svcScheduleUnitRepository.saveAndFlush(svcScheduleUnit);

        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();

        // Update the svcScheduleUnit
        SvcScheduleUnit updatedSvcScheduleUnit = svcScheduleUnitRepository.findById(svcScheduleUnit.getId()).get();
        // Disconnect from session so that the updates on updatedSvcScheduleUnit are not directly saved in db
        em.detach(updatedSvcScheduleUnit);
        updatedSvcScheduleUnit
            .startFrom(UPDATED_START_FROM)
            .endAt(UPDATED_END_AT)
            .actualStartFrom(UPDATED_ACTUAL_START_FROM)
            .actualEndAt(UPDATED_ACTUAL_END_AT)
            .scheduleQuantity(UPDATED_SCHEDULE_QUANTITY)
            .actualQuantity(UPDATED_ACTUAL_QUANTITY)
            .appliedDate(UPDATED_APPLIED_DATE)
            .status(UPDATED_STATUS)
            .planedNote(UPDATED_PLANED_NOTE)
            .laborerNote(UPDATED_LABORER_NOTE)
            .supervisorNote(UPDATED_SUPERVISOR_NOTE);
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(updatedSvcScheduleUnit);

        restSvcScheduleUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcScheduleUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcScheduleUnit testSvcScheduleUnit = svcScheduleUnitList.get(svcScheduleUnitList.size() - 1);
        assertThat(testSvcScheduleUnit.getStartFrom()).isEqualTo(UPDATED_START_FROM);
        assertThat(testSvcScheduleUnit.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testSvcScheduleUnit.getActualStartFrom()).isEqualTo(UPDATED_ACTUAL_START_FROM);
        assertThat(testSvcScheduleUnit.getActualEndAt()).isEqualTo(UPDATED_ACTUAL_END_AT);
        assertThat(testSvcScheduleUnit.getScheduleQuantity()).isEqualTo(UPDATED_SCHEDULE_QUANTITY);
        assertThat(testSvcScheduleUnit.getActualQuantity()).isEqualTo(UPDATED_ACTUAL_QUANTITY);
        assertThat(testSvcScheduleUnit.getAppliedDate()).isEqualTo(UPDATED_APPLIED_DATE);
        assertThat(testSvcScheduleUnit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcScheduleUnit.getPlanedNote()).isEqualTo(UPDATED_PLANED_NOTE);
        assertThat(testSvcScheduleUnit.getLaborerNote()).isEqualTo(UPDATED_LABORER_NOTE);
        assertThat(testSvcScheduleUnit.getSupervisorNote()).isEqualTo(UPDATED_SUPERVISOR_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingSvcScheduleUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();
        svcScheduleUnit.setId(count.incrementAndGet());

        // Create the SvcScheduleUnit
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcScheduleUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcScheduleUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcScheduleUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();
        svcScheduleUnit.setId(count.incrementAndGet());

        // Create the SvcScheduleUnit
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcScheduleUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcScheduleUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();
        svcScheduleUnit.setId(count.incrementAndGet());

        // Create the SvcScheduleUnit
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcScheduleUnitMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcScheduleUnitWithPatch() throws Exception {
        // Initialize the database
        svcScheduleUnitRepository.saveAndFlush(svcScheduleUnit);

        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();

        // Update the svcScheduleUnit using partial update
        SvcScheduleUnit partialUpdatedSvcScheduleUnit = new SvcScheduleUnit();
        partialUpdatedSvcScheduleUnit.setId(svcScheduleUnit.getId());

        partialUpdatedSvcScheduleUnit
            .actualEndAt(UPDATED_ACTUAL_END_AT)
            .status(UPDATED_STATUS)
            .planedNote(UPDATED_PLANED_NOTE)
            .laborerNote(UPDATED_LABORER_NOTE)
            .supervisorNote(UPDATED_SUPERVISOR_NOTE);

        restSvcScheduleUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcScheduleUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcScheduleUnit))
            )
            .andExpect(status().isOk());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcScheduleUnit testSvcScheduleUnit = svcScheduleUnitList.get(svcScheduleUnitList.size() - 1);
        assertThat(testSvcScheduleUnit.getStartFrom()).isEqualTo(DEFAULT_START_FROM);
        assertThat(testSvcScheduleUnit.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testSvcScheduleUnit.getActualStartFrom()).isEqualTo(DEFAULT_ACTUAL_START_FROM);
        assertThat(testSvcScheduleUnit.getActualEndAt()).isEqualTo(UPDATED_ACTUAL_END_AT);
        assertThat(testSvcScheduleUnit.getScheduleQuantity()).isEqualByComparingTo(DEFAULT_SCHEDULE_QUANTITY);
        assertThat(testSvcScheduleUnit.getActualQuantity()).isEqualByComparingTo(DEFAULT_ACTUAL_QUANTITY);
        assertThat(testSvcScheduleUnit.getAppliedDate()).isEqualTo(DEFAULT_APPLIED_DATE);
        assertThat(testSvcScheduleUnit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcScheduleUnit.getPlanedNote()).isEqualTo(UPDATED_PLANED_NOTE);
        assertThat(testSvcScheduleUnit.getLaborerNote()).isEqualTo(UPDATED_LABORER_NOTE);
        assertThat(testSvcScheduleUnit.getSupervisorNote()).isEqualTo(UPDATED_SUPERVISOR_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateSvcScheduleUnitWithPatch() throws Exception {
        // Initialize the database
        svcScheduleUnitRepository.saveAndFlush(svcScheduleUnit);

        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();

        // Update the svcScheduleUnit using partial update
        SvcScheduleUnit partialUpdatedSvcScheduleUnit = new SvcScheduleUnit();
        partialUpdatedSvcScheduleUnit.setId(svcScheduleUnit.getId());

        partialUpdatedSvcScheduleUnit
            .startFrom(UPDATED_START_FROM)
            .endAt(UPDATED_END_AT)
            .actualStartFrom(UPDATED_ACTUAL_START_FROM)
            .actualEndAt(UPDATED_ACTUAL_END_AT)
            .scheduleQuantity(UPDATED_SCHEDULE_QUANTITY)
            .actualQuantity(UPDATED_ACTUAL_QUANTITY)
            .appliedDate(UPDATED_APPLIED_DATE)
            .status(UPDATED_STATUS)
            .planedNote(UPDATED_PLANED_NOTE)
            .laborerNote(UPDATED_LABORER_NOTE)
            .supervisorNote(UPDATED_SUPERVISOR_NOTE);

        restSvcScheduleUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcScheduleUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcScheduleUnit))
            )
            .andExpect(status().isOk());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcScheduleUnit testSvcScheduleUnit = svcScheduleUnitList.get(svcScheduleUnitList.size() - 1);
        assertThat(testSvcScheduleUnit.getStartFrom()).isEqualTo(UPDATED_START_FROM);
        assertThat(testSvcScheduleUnit.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testSvcScheduleUnit.getActualStartFrom()).isEqualTo(UPDATED_ACTUAL_START_FROM);
        assertThat(testSvcScheduleUnit.getActualEndAt()).isEqualTo(UPDATED_ACTUAL_END_AT);
        assertThat(testSvcScheduleUnit.getScheduleQuantity()).isEqualByComparingTo(UPDATED_SCHEDULE_QUANTITY);
        assertThat(testSvcScheduleUnit.getActualQuantity()).isEqualByComparingTo(UPDATED_ACTUAL_QUANTITY);
        assertThat(testSvcScheduleUnit.getAppliedDate()).isEqualTo(UPDATED_APPLIED_DATE);
        assertThat(testSvcScheduleUnit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSvcScheduleUnit.getPlanedNote()).isEqualTo(UPDATED_PLANED_NOTE);
        assertThat(testSvcScheduleUnit.getLaborerNote()).isEqualTo(UPDATED_LABORER_NOTE);
        assertThat(testSvcScheduleUnit.getSupervisorNote()).isEqualTo(UPDATED_SUPERVISOR_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingSvcScheduleUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();
        svcScheduleUnit.setId(count.incrementAndGet());

        // Create the SvcScheduleUnit
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcScheduleUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcScheduleUnitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcScheduleUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();
        svcScheduleUnit.setId(count.incrementAndGet());

        // Create the SvcScheduleUnit
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcScheduleUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcScheduleUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcScheduleUnitRepository.findAll().size();
        svcScheduleUnit.setId(count.incrementAndGet());

        // Create the SvcScheduleUnit
        SvcScheduleUnitDTO svcScheduleUnitDTO = svcScheduleUnitMapper.toDto(svcScheduleUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcScheduleUnitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcScheduleUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcScheduleUnit in the database
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcScheduleUnit() throws Exception {
        // Initialize the database
        svcScheduleUnitRepository.saveAndFlush(svcScheduleUnit);

        int databaseSizeBeforeDelete = svcScheduleUnitRepository.findAll().size();

        // Delete the svcScheduleUnit
        restSvcScheduleUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcScheduleUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcScheduleUnit> svcScheduleUnitList = svcScheduleUnitRepository.findAll();
        assertThat(svcScheduleUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
