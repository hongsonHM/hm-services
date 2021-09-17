package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcLabor;
import com.overnetcontact.dvvs.domain.SvcPlanUnit;
import com.overnetcontact.dvvs.repository.SvcLaborRepository;
import com.overnetcontact.dvvs.service.criteria.SvcLaborCriteria;
import com.overnetcontact.dvvs.service.dto.SvcLaborDTO;
import com.overnetcontact.dvvs.service.mapper.SvcLaborMapper;
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
 * Integration tests for the {@link SvcLaborResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcLaborResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LABOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LABOR_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-labors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcLaborRepository svcLaborRepository;

    @Autowired
    private SvcLaborMapper svcLaborMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcLaborMockMvc;

    private SvcLabor svcLabor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcLabor createEntity(EntityManager em) {
        SvcLabor svcLabor = new SvcLabor().name(DEFAULT_NAME).phone(DEFAULT_PHONE).address(DEFAULT_ADDRESS).laborCode(DEFAULT_LABOR_CODE);
        return svcLabor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcLabor createUpdatedEntity(EntityManager em) {
        SvcLabor svcLabor = new SvcLabor().name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS).laborCode(UPDATED_LABOR_CODE);
        return svcLabor;
    }

    @BeforeEach
    public void initTest() {
        svcLabor = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcLabor() throws Exception {
        int databaseSizeBeforeCreate = svcLaborRepository.findAll().size();
        // Create the SvcLabor
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);
        restSvcLaborMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcLaborDTO)))
            .andExpect(status().isCreated());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeCreate + 1);
        SvcLabor testSvcLabor = svcLaborList.get(svcLaborList.size() - 1);
        assertThat(testSvcLabor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcLabor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSvcLabor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSvcLabor.getLaborCode()).isEqualTo(DEFAULT_LABOR_CODE);
    }

    @Test
    @Transactional
    void createSvcLaborWithExistingId() throws Exception {
        // Create the SvcLabor with an existing ID
        svcLabor.setId(1L);
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);

        int databaseSizeBeforeCreate = svcLaborRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcLaborMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcLaborDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcLaborRepository.findAll().size();
        // set the field null
        svcLabor.setName(null);

        // Create the SvcLabor, which fails.
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);

        restSvcLaborMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcLaborDTO)))
            .andExpect(status().isBadRequest());

        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcLabors() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList
        restSvcLaborMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcLabor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].laborCode").value(hasItem(DEFAULT_LABOR_CODE)));
    }

    @Test
    @Transactional
    void getSvcLabor() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get the svcLabor
        restSvcLaborMockMvc
            .perform(get(ENTITY_API_URL_ID, svcLabor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcLabor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.laborCode").value(DEFAULT_LABOR_CODE));
    }

    @Test
    @Transactional
    void getSvcLaborsByIdFiltering() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        Long id = svcLabor.getId();

        defaultSvcLaborShouldBeFound("id.equals=" + id);
        defaultSvcLaborShouldNotBeFound("id.notEquals=" + id);

        defaultSvcLaborShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcLaborShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcLaborShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcLaborShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where name equals to DEFAULT_NAME
        defaultSvcLaborShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the svcLaborList where name equals to UPDATED_NAME
        defaultSvcLaborShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where name not equals to DEFAULT_NAME
        defaultSvcLaborShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the svcLaborList where name not equals to UPDATED_NAME
        defaultSvcLaborShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSvcLaborShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the svcLaborList where name equals to UPDATED_NAME
        defaultSvcLaborShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where name is not null
        defaultSvcLaborShouldBeFound("name.specified=true");

        // Get all the svcLaborList where name is null
        defaultSvcLaborShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcLaborsByNameContainsSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where name contains DEFAULT_NAME
        defaultSvcLaborShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the svcLaborList where name contains UPDATED_NAME
        defaultSvcLaborShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where name does not contain DEFAULT_NAME
        defaultSvcLaborShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the svcLaborList where name does not contain UPDATED_NAME
        defaultSvcLaborShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where phone equals to DEFAULT_PHONE
        defaultSvcLaborShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the svcLaborList where phone equals to UPDATED_PHONE
        defaultSvcLaborShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where phone not equals to DEFAULT_PHONE
        defaultSvcLaborShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the svcLaborList where phone not equals to UPDATED_PHONE
        defaultSvcLaborShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultSvcLaborShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the svcLaborList where phone equals to UPDATED_PHONE
        defaultSvcLaborShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where phone is not null
        defaultSvcLaborShouldBeFound("phone.specified=true");

        // Get all the svcLaborList where phone is null
        defaultSvcLaborShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcLaborsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where phone contains DEFAULT_PHONE
        defaultSvcLaborShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the svcLaborList where phone contains UPDATED_PHONE
        defaultSvcLaborShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where phone does not contain DEFAULT_PHONE
        defaultSvcLaborShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the svcLaborList where phone does not contain UPDATED_PHONE
        defaultSvcLaborShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where address equals to DEFAULT_ADDRESS
        defaultSvcLaborShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the svcLaborList where address equals to UPDATED_ADDRESS
        defaultSvcLaborShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where address not equals to DEFAULT_ADDRESS
        defaultSvcLaborShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the svcLaborList where address not equals to UPDATED_ADDRESS
        defaultSvcLaborShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSvcLaborShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the svcLaborList where address equals to UPDATED_ADDRESS
        defaultSvcLaborShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where address is not null
        defaultSvcLaborShouldBeFound("address.specified=true");

        // Get all the svcLaborList where address is null
        defaultSvcLaborShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcLaborsByAddressContainsSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where address contains DEFAULT_ADDRESS
        defaultSvcLaborShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the svcLaborList where address contains UPDATED_ADDRESS
        defaultSvcLaborShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where address does not contain DEFAULT_ADDRESS
        defaultSvcLaborShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the svcLaborList where address does not contain UPDATED_ADDRESS
        defaultSvcLaborShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByLaborCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where laborCode equals to DEFAULT_LABOR_CODE
        defaultSvcLaborShouldBeFound("laborCode.equals=" + DEFAULT_LABOR_CODE);

        // Get all the svcLaborList where laborCode equals to UPDATED_LABOR_CODE
        defaultSvcLaborShouldNotBeFound("laborCode.equals=" + UPDATED_LABOR_CODE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByLaborCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where laborCode not equals to DEFAULT_LABOR_CODE
        defaultSvcLaborShouldNotBeFound("laborCode.notEquals=" + DEFAULT_LABOR_CODE);

        // Get all the svcLaborList where laborCode not equals to UPDATED_LABOR_CODE
        defaultSvcLaborShouldBeFound("laborCode.notEquals=" + UPDATED_LABOR_CODE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByLaborCodeIsInShouldWork() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where laborCode in DEFAULT_LABOR_CODE or UPDATED_LABOR_CODE
        defaultSvcLaborShouldBeFound("laborCode.in=" + DEFAULT_LABOR_CODE + "," + UPDATED_LABOR_CODE);

        // Get all the svcLaborList where laborCode equals to UPDATED_LABOR_CODE
        defaultSvcLaborShouldNotBeFound("laborCode.in=" + UPDATED_LABOR_CODE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByLaborCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where laborCode is not null
        defaultSvcLaborShouldBeFound("laborCode.specified=true");

        // Get all the svcLaborList where laborCode is null
        defaultSvcLaborShouldNotBeFound("laborCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcLaborsByLaborCodeContainsSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where laborCode contains DEFAULT_LABOR_CODE
        defaultSvcLaborShouldBeFound("laborCode.contains=" + DEFAULT_LABOR_CODE);

        // Get all the svcLaborList where laborCode contains UPDATED_LABOR_CODE
        defaultSvcLaborShouldNotBeFound("laborCode.contains=" + UPDATED_LABOR_CODE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsByLaborCodeNotContainsSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        // Get all the svcLaborList where laborCode does not contain DEFAULT_LABOR_CODE
        defaultSvcLaborShouldNotBeFound("laborCode.doesNotContain=" + DEFAULT_LABOR_CODE);

        // Get all the svcLaborList where laborCode does not contain UPDATED_LABOR_CODE
        defaultSvcLaborShouldBeFound("laborCode.doesNotContain=" + UPDATED_LABOR_CODE);
    }

    @Test
    @Transactional
    void getAllSvcLaborsBySvcPlanUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);
        SvcPlanUnit svcPlanUnit = SvcPlanUnitResourceIT.createEntity(em);
        em.persist(svcPlanUnit);
        em.flush();
        svcLabor.setSvcPlanUnit(svcPlanUnit);
        svcLaborRepository.saveAndFlush(svcLabor);
        Long svcPlanUnitId = svcPlanUnit.getId();

        // Get all the svcLaborList where svcPlanUnit equals to svcPlanUnitId
        defaultSvcLaborShouldBeFound("svcPlanUnitId.equals=" + svcPlanUnitId);

        // Get all the svcLaborList where svcPlanUnit equals to (svcPlanUnitId + 1)
        defaultSvcLaborShouldNotBeFound("svcPlanUnitId.equals=" + (svcPlanUnitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcLaborShouldBeFound(String filter) throws Exception {
        restSvcLaborMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcLabor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].laborCode").value(hasItem(DEFAULT_LABOR_CODE)));

        // Check, that the count call also returns 1
        restSvcLaborMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcLaborShouldNotBeFound(String filter) throws Exception {
        restSvcLaborMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcLaborMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcLabor() throws Exception {
        // Get the svcLabor
        restSvcLaborMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcLabor() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();

        // Update the svcLabor
        SvcLabor updatedSvcLabor = svcLaborRepository.findById(svcLabor.getId()).get();
        // Disconnect from session so that the updates on updatedSvcLabor are not directly saved in db
        em.detach(updatedSvcLabor);
        updatedSvcLabor.name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS).laborCode(UPDATED_LABOR_CODE);
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(updatedSvcLabor);

        restSvcLaborMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcLaborDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcLaborDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
        SvcLabor testSvcLabor = svcLaborList.get(svcLaborList.size() - 1);
        assertThat(testSvcLabor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcLabor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSvcLabor.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSvcLabor.getLaborCode()).isEqualTo(UPDATED_LABOR_CODE);
    }

    @Test
    @Transactional
    void putNonExistingSvcLabor() throws Exception {
        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();
        svcLabor.setId(count.incrementAndGet());

        // Create the SvcLabor
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcLaborMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcLaborDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcLaborDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcLabor() throws Exception {
        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();
        svcLabor.setId(count.incrementAndGet());

        // Create the SvcLabor
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcLaborMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcLaborDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcLabor() throws Exception {
        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();
        svcLabor.setId(count.incrementAndGet());

        // Create the SvcLabor
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcLaborMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcLaborDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcLaborWithPatch() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();

        // Update the svcLabor using partial update
        SvcLabor partialUpdatedSvcLabor = new SvcLabor();
        partialUpdatedSvcLabor.setId(svcLabor.getId());

        partialUpdatedSvcLabor.phone(UPDATED_PHONE).address(UPDATED_ADDRESS).laborCode(UPDATED_LABOR_CODE);

        restSvcLaborMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcLabor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcLabor))
            )
            .andExpect(status().isOk());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
        SvcLabor testSvcLabor = svcLaborList.get(svcLaborList.size() - 1);
        assertThat(testSvcLabor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcLabor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSvcLabor.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSvcLabor.getLaborCode()).isEqualTo(UPDATED_LABOR_CODE);
    }

    @Test
    @Transactional
    void fullUpdateSvcLaborWithPatch() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();

        // Update the svcLabor using partial update
        SvcLabor partialUpdatedSvcLabor = new SvcLabor();
        partialUpdatedSvcLabor.setId(svcLabor.getId());

        partialUpdatedSvcLabor.name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS).laborCode(UPDATED_LABOR_CODE);

        restSvcLaborMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcLabor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcLabor))
            )
            .andExpect(status().isOk());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
        SvcLabor testSvcLabor = svcLaborList.get(svcLaborList.size() - 1);
        assertThat(testSvcLabor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcLabor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSvcLabor.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSvcLabor.getLaborCode()).isEqualTo(UPDATED_LABOR_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingSvcLabor() throws Exception {
        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();
        svcLabor.setId(count.incrementAndGet());

        // Create the SvcLabor
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcLaborMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcLaborDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcLaborDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcLabor() throws Exception {
        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();
        svcLabor.setId(count.incrementAndGet());

        // Create the SvcLabor
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcLaborMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcLaborDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcLabor() throws Exception {
        int databaseSizeBeforeUpdate = svcLaborRepository.findAll().size();
        svcLabor.setId(count.incrementAndGet());

        // Create the SvcLabor
        SvcLaborDTO svcLaborDTO = svcLaborMapper.toDto(svcLabor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcLaborMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcLaborDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcLabor in the database
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcLabor() throws Exception {
        // Initialize the database
        svcLaborRepository.saveAndFlush(svcLabor);

        int databaseSizeBeforeDelete = svcLaborRepository.findAll().size();

        // Delete the svcLabor
        restSvcLaborMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcLabor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcLabor> svcLaborList = svcLaborRepository.findAll();
        assertThat(svcLaborList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
