package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcClient;
import com.overnetcontact.dvvs.repository.SvcClientRepository;
import com.overnetcontact.dvvs.service.criteria.SvcClientCriteria;
import com.overnetcontact.dvvs.service.dto.SvcClientDTO;
import com.overnetcontact.dvvs.service.mapper.SvcClientMapper;
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
 * Integration tests for the {@link SvcClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcClientResourceIT {

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcClientRepository svcClientRepository;

    @Autowired
    private SvcClientMapper svcClientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcClientMockMvc;

    private SvcClient svcClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcClient createEntity(EntityManager em) {
        SvcClient svcClient = new SvcClient()
            .customerName(DEFAULT_CUSTOMER_NAME)
            .customerCity(DEFAULT_CUSTOMER_CITY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .type(DEFAULT_TYPE)
            .address(DEFAULT_ADDRESS);
        return svcClient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcClient createUpdatedEntity(EntityManager em) {
        SvcClient svcClient = new SvcClient()
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerCity(UPDATED_CUSTOMER_CITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .type(UPDATED_TYPE)
            .address(UPDATED_ADDRESS);
        return svcClient;
    }

    @BeforeEach
    public void initTest() {
        svcClient = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcClient() throws Exception {
        int databaseSizeBeforeCreate = svcClientRepository.findAll().size();
        // Create the SvcClient
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);
        restSvcClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcClientDTO)))
            .andExpect(status().isCreated());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeCreate + 1);
        SvcClient testSvcClient = svcClientList.get(svcClientList.size() - 1);
        assertThat(testSvcClient.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testSvcClient.getCustomerCity()).isEqualTo(DEFAULT_CUSTOMER_CITY);
        assertThat(testSvcClient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSvcClient.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSvcClient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createSvcClientWithExistingId() throws Exception {
        // Create the SvcClient with an existing ID
        svcClient.setId(1L);
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        int databaseSizeBeforeCreate = svcClientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcClientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcClientRepository.findAll().size();
        // set the field null
        svcClient.setCustomerName(null);

        // Create the SvcClient, which fails.
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        restSvcClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcClientDTO)))
            .andExpect(status().isBadRequest());

        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcClientRepository.findAll().size();
        // set the field null
        svcClient.setCustomerCity(null);

        // Create the SvcClient, which fails.
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        restSvcClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcClientDTO)))
            .andExpect(status().isBadRequest());

        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcClientRepository.findAll().size();
        // set the field null
        svcClient.setPhoneNumber(null);

        // Create the SvcClient, which fails.
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        restSvcClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcClientDTO)))
            .andExpect(status().isBadRequest());

        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcClientRepository.findAll().size();
        // set the field null
        svcClient.setType(null);

        // Create the SvcClient, which fails.
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        restSvcClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcClientDTO)))
            .andExpect(status().isBadRequest());

        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcClientRepository.findAll().size();
        // set the field null
        svcClient.setAddress(null);

        // Create the SvcClient, which fails.
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        restSvcClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcClientDTO)))
            .andExpect(status().isBadRequest());

        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcClients() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList
        restSvcClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].customerCity").value(hasItem(DEFAULT_CUSTOMER_CITY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getSvcClient() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get the svcClient
        restSvcClientMockMvc
            .perform(get(ENTITY_API_URL_ID, svcClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcClient.getId().intValue()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.customerCity").value(DEFAULT_CUSTOMER_CITY))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getSvcClientsByIdFiltering() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        Long id = svcClient.getId();

        defaultSvcClientShouldBeFound("id.equals=" + id);
        defaultSvcClientShouldNotBeFound("id.notEquals=" + id);

        defaultSvcClientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcClientShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcClientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcClientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultSvcClientShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the svcClientList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultSvcClientShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerName not equals to DEFAULT_CUSTOMER_NAME
        defaultSvcClientShouldNotBeFound("customerName.notEquals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the svcClientList where customerName not equals to UPDATED_CUSTOMER_NAME
        defaultSvcClientShouldBeFound("customerName.notEquals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultSvcClientShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the svcClientList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultSvcClientShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerName is not null
        defaultSvcClientShouldBeFound("customerName.specified=true");

        // Get all the svcClientList where customerName is null
        defaultSvcClientShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultSvcClientShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the svcClientList where customerName contains UPDATED_CUSTOMER_NAME
        defaultSvcClientShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultSvcClientShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the svcClientList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultSvcClientShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerCityIsEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerCity equals to DEFAULT_CUSTOMER_CITY
        defaultSvcClientShouldBeFound("customerCity.equals=" + DEFAULT_CUSTOMER_CITY);

        // Get all the svcClientList where customerCity equals to UPDATED_CUSTOMER_CITY
        defaultSvcClientShouldNotBeFound("customerCity.equals=" + UPDATED_CUSTOMER_CITY);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerCity not equals to DEFAULT_CUSTOMER_CITY
        defaultSvcClientShouldNotBeFound("customerCity.notEquals=" + DEFAULT_CUSTOMER_CITY);

        // Get all the svcClientList where customerCity not equals to UPDATED_CUSTOMER_CITY
        defaultSvcClientShouldBeFound("customerCity.notEquals=" + UPDATED_CUSTOMER_CITY);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerCityIsInShouldWork() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerCity in DEFAULT_CUSTOMER_CITY or UPDATED_CUSTOMER_CITY
        defaultSvcClientShouldBeFound("customerCity.in=" + DEFAULT_CUSTOMER_CITY + "," + UPDATED_CUSTOMER_CITY);

        // Get all the svcClientList where customerCity equals to UPDATED_CUSTOMER_CITY
        defaultSvcClientShouldNotBeFound("customerCity.in=" + UPDATED_CUSTOMER_CITY);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerCity is not null
        defaultSvcClientShouldBeFound("customerCity.specified=true");

        // Get all the svcClientList where customerCity is null
        defaultSvcClientShouldNotBeFound("customerCity.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerCityContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerCity contains DEFAULT_CUSTOMER_CITY
        defaultSvcClientShouldBeFound("customerCity.contains=" + DEFAULT_CUSTOMER_CITY);

        // Get all the svcClientList where customerCity contains UPDATED_CUSTOMER_CITY
        defaultSvcClientShouldNotBeFound("customerCity.contains=" + UPDATED_CUSTOMER_CITY);
    }

    @Test
    @Transactional
    void getAllSvcClientsByCustomerCityNotContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where customerCity does not contain DEFAULT_CUSTOMER_CITY
        defaultSvcClientShouldNotBeFound("customerCity.doesNotContain=" + DEFAULT_CUSTOMER_CITY);

        // Get all the svcClientList where customerCity does not contain UPDATED_CUSTOMER_CITY
        defaultSvcClientShouldBeFound("customerCity.doesNotContain=" + UPDATED_CUSTOMER_CITY);
    }

    @Test
    @Transactional
    void getAllSvcClientsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultSvcClientShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the svcClientList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSvcClientShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcClientsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultSvcClientShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the svcClientList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultSvcClientShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcClientsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultSvcClientShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the svcClientList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSvcClientShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcClientsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where phoneNumber is not null
        defaultSvcClientShouldBeFound("phoneNumber.specified=true");

        // Get all the svcClientList where phoneNumber is null
        defaultSvcClientShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcClientsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultSvcClientShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the svcClientList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultSvcClientShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcClientsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultSvcClientShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the svcClientList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultSvcClientShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllSvcClientsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where type equals to DEFAULT_TYPE
        defaultSvcClientShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the svcClientList where type equals to UPDATED_TYPE
        defaultSvcClientShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcClientsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where type not equals to DEFAULT_TYPE
        defaultSvcClientShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the svcClientList where type not equals to UPDATED_TYPE
        defaultSvcClientShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcClientsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSvcClientShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the svcClientList where type equals to UPDATED_TYPE
        defaultSvcClientShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcClientsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where type is not null
        defaultSvcClientShouldBeFound("type.specified=true");

        // Get all the svcClientList where type is null
        defaultSvcClientShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcClientsByTypeContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where type contains DEFAULT_TYPE
        defaultSvcClientShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the svcClientList where type contains UPDATED_TYPE
        defaultSvcClientShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcClientsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where type does not contain DEFAULT_TYPE
        defaultSvcClientShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the svcClientList where type does not contain UPDATED_TYPE
        defaultSvcClientShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSvcClientsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where address equals to DEFAULT_ADDRESS
        defaultSvcClientShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the svcClientList where address equals to UPDATED_ADDRESS
        defaultSvcClientShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcClientsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where address not equals to DEFAULT_ADDRESS
        defaultSvcClientShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the svcClientList where address not equals to UPDATED_ADDRESS
        defaultSvcClientShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcClientsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSvcClientShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the svcClientList where address equals to UPDATED_ADDRESS
        defaultSvcClientShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcClientsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where address is not null
        defaultSvcClientShouldBeFound("address.specified=true");

        // Get all the svcClientList where address is null
        defaultSvcClientShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcClientsByAddressContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where address contains DEFAULT_ADDRESS
        defaultSvcClientShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the svcClientList where address contains UPDATED_ADDRESS
        defaultSvcClientShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSvcClientsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        // Get all the svcClientList where address does not contain DEFAULT_ADDRESS
        defaultSvcClientShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the svcClientList where address does not contain UPDATED_ADDRESS
        defaultSvcClientShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcClientShouldBeFound(String filter) throws Exception {
        restSvcClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].customerCity").value(hasItem(DEFAULT_CUSTOMER_CITY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restSvcClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcClientShouldNotBeFound(String filter) throws Exception {
        restSvcClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcClient() throws Exception {
        // Get the svcClient
        restSvcClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcClient() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();

        // Update the svcClient
        SvcClient updatedSvcClient = svcClientRepository.findById(svcClient.getId()).get();
        // Disconnect from session so that the updates on updatedSvcClient are not directly saved in db
        em.detach(updatedSvcClient);
        updatedSvcClient
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerCity(UPDATED_CUSTOMER_CITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .type(UPDATED_TYPE)
            .address(UPDATED_ADDRESS);
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(updatedSvcClient);

        restSvcClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcClientDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
        SvcClient testSvcClient = svcClientList.get(svcClientList.size() - 1);
        assertThat(testSvcClient.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testSvcClient.getCustomerCity()).isEqualTo(UPDATED_CUSTOMER_CITY);
        assertThat(testSvcClient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSvcClient.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSvcClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingSvcClient() throws Exception {
        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();
        svcClient.setId(count.incrementAndGet());

        // Create the SvcClient
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcClient() throws Exception {
        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();
        svcClient.setId(count.incrementAndGet());

        // Create the SvcClient
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcClient() throws Exception {
        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();
        svcClient.setId(count.incrementAndGet());

        // Create the SvcClient
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcClientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcClientWithPatch() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();

        // Update the svcClient using partial update
        SvcClient partialUpdatedSvcClient = new SvcClient();
        partialUpdatedSvcClient.setId(svcClient.getId());

        partialUpdatedSvcClient.customerName(UPDATED_CUSTOMER_NAME).address(UPDATED_ADDRESS);

        restSvcClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcClient))
            )
            .andExpect(status().isOk());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
        SvcClient testSvcClient = svcClientList.get(svcClientList.size() - 1);
        assertThat(testSvcClient.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testSvcClient.getCustomerCity()).isEqualTo(DEFAULT_CUSTOMER_CITY);
        assertThat(testSvcClient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSvcClient.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSvcClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateSvcClientWithPatch() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();

        // Update the svcClient using partial update
        SvcClient partialUpdatedSvcClient = new SvcClient();
        partialUpdatedSvcClient.setId(svcClient.getId());

        partialUpdatedSvcClient
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerCity(UPDATED_CUSTOMER_CITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .type(UPDATED_TYPE)
            .address(UPDATED_ADDRESS);

        restSvcClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcClient))
            )
            .andExpect(status().isOk());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
        SvcClient testSvcClient = svcClientList.get(svcClientList.size() - 1);
        assertThat(testSvcClient.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testSvcClient.getCustomerCity()).isEqualTo(UPDATED_CUSTOMER_CITY);
        assertThat(testSvcClient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSvcClient.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSvcClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingSvcClient() throws Exception {
        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();
        svcClient.setId(count.incrementAndGet());

        // Create the SvcClient
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcClientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcClient() throws Exception {
        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();
        svcClient.setId(count.incrementAndGet());

        // Create the SvcClient
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcClient() throws Exception {
        int databaseSizeBeforeUpdate = svcClientRepository.findAll().size();
        svcClient.setId(count.incrementAndGet());

        // Create the SvcClient
        SvcClientDTO svcClientDTO = svcClientMapper.toDto(svcClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcClientMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcClientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcClient in the database
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcClient() throws Exception {
        // Initialize the database
        svcClientRepository.saveAndFlush(svcClient);

        int databaseSizeBeforeDelete = svcClientRepository.findAll().size();

        // Delete the svcClient
        restSvcClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcClient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcClient> svcClientList = svcClientRepository.findAll();
        assertThat(svcClientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
