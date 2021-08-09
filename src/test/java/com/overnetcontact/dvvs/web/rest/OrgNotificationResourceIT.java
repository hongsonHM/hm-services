package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.OrgNotification;
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.domain.enumeration.NotificationStatus;
import com.overnetcontact.dvvs.repository.OrgNotificationRepository;
import com.overnetcontact.dvvs.service.criteria.OrgNotificationCriteria;
import com.overnetcontact.dvvs.service.dto.OrgNotificationDTO;
import com.overnetcontact.dvvs.service.mapper.OrgNotificationMapper;
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
 * Integration tests for the {@link OrgNotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgNotificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_READ = false;
    private static final Boolean UPDATED_IS_READ = true;

    private static final NotificationStatus DEFAULT_STATUS = NotificationStatus.PROCESS;
    private static final NotificationStatus UPDATED_STATUS = NotificationStatus.SUCCESS;

    private static final String ENTITY_API_URL = "/api/org-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgNotificationRepository orgNotificationRepository;

    @Autowired
    private OrgNotificationMapper orgNotificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgNotificationMockMvc;

    private OrgNotification orgNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgNotification createEntity(EntityManager em) {
        OrgNotification orgNotification = new OrgNotification()
            .title(DEFAULT_TITLE)
            .desc(DEFAULT_DESC)
            .data(DEFAULT_DATA)
            .isRead(DEFAULT_IS_READ)
            .status(DEFAULT_STATUS);
        return orgNotification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgNotification createUpdatedEntity(EntityManager em) {
        OrgNotification orgNotification = new OrgNotification()
            .title(UPDATED_TITLE)
            .desc(UPDATED_DESC)
            .data(UPDATED_DATA)
            .isRead(UPDATED_IS_READ)
            .status(UPDATED_STATUS);
        return orgNotification;
    }

    @BeforeEach
    public void initTest() {
        orgNotification = createEntity(em);
    }

    @Test
    @Transactional
    void createOrgNotification() throws Exception {
        int databaseSizeBeforeCreate = orgNotificationRepository.findAll().size();
        // Create the OrgNotification
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);
        restOrgNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeCreate + 1);
        OrgNotification testOrgNotification = orgNotificationList.get(orgNotificationList.size() - 1);
        assertThat(testOrgNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrgNotification.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testOrgNotification.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testOrgNotification.getIsRead()).isEqualTo(DEFAULT_IS_READ);
        assertThat(testOrgNotification.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createOrgNotificationWithExistingId() throws Exception {
        // Create the OrgNotification with an existing ID
        orgNotification.setId(1L);
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        int databaseSizeBeforeCreate = orgNotificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgNotificationRepository.findAll().size();
        // set the field null
        orgNotification.setTitle(null);

        // Create the OrgNotification, which fails.
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        restOrgNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgNotificationRepository.findAll().size();
        // set the field null
        orgNotification.setDesc(null);

        // Create the OrgNotification, which fails.
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        restOrgNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsReadIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgNotificationRepository.findAll().size();
        // set the field null
        orgNotification.setIsRead(null);

        // Create the OrgNotification, which fails.
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        restOrgNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgNotificationRepository.findAll().size();
        // set the field null
        orgNotification.setStatus(null);

        // Create the OrgNotification, which fails.
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        restOrgNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrgNotifications() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList
        restOrgNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getOrgNotification() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get the orgNotification
        restOrgNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, orgNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgNotification.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.isRead").value(DEFAULT_IS_READ.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getOrgNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        Long id = orgNotification.getId();

        defaultOrgNotificationShouldBeFound("id.equals=" + id);
        defaultOrgNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultOrgNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrgNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrgNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrgNotificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where title equals to DEFAULT_TITLE
        defaultOrgNotificationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the orgNotificationList where title equals to UPDATED_TITLE
        defaultOrgNotificationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where title not equals to DEFAULT_TITLE
        defaultOrgNotificationShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the orgNotificationList where title not equals to UPDATED_TITLE
        defaultOrgNotificationShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultOrgNotificationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the orgNotificationList where title equals to UPDATED_TITLE
        defaultOrgNotificationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where title is not null
        defaultOrgNotificationShouldBeFound("title.specified=true");

        // Get all the orgNotificationList where title is null
        defaultOrgNotificationShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where title contains DEFAULT_TITLE
        defaultOrgNotificationShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the orgNotificationList where title contains UPDATED_TITLE
        defaultOrgNotificationShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where title does not contain DEFAULT_TITLE
        defaultOrgNotificationShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the orgNotificationList where title does not contain UPDATED_TITLE
        defaultOrgNotificationShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDescIsEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where desc equals to DEFAULT_DESC
        defaultOrgNotificationShouldBeFound("desc.equals=" + DEFAULT_DESC);

        // Get all the orgNotificationList where desc equals to UPDATED_DESC
        defaultOrgNotificationShouldNotBeFound("desc.equals=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDescIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where desc not equals to DEFAULT_DESC
        defaultOrgNotificationShouldNotBeFound("desc.notEquals=" + DEFAULT_DESC);

        // Get all the orgNotificationList where desc not equals to UPDATED_DESC
        defaultOrgNotificationShouldBeFound("desc.notEquals=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDescIsInShouldWork() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where desc in DEFAULT_DESC or UPDATED_DESC
        defaultOrgNotificationShouldBeFound("desc.in=" + DEFAULT_DESC + "," + UPDATED_DESC);

        // Get all the orgNotificationList where desc equals to UPDATED_DESC
        defaultOrgNotificationShouldNotBeFound("desc.in=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where desc is not null
        defaultOrgNotificationShouldBeFound("desc.specified=true");

        // Get all the orgNotificationList where desc is null
        defaultOrgNotificationShouldNotBeFound("desc.specified=false");
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDescContainsSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where desc contains DEFAULT_DESC
        defaultOrgNotificationShouldBeFound("desc.contains=" + DEFAULT_DESC);

        // Get all the orgNotificationList where desc contains UPDATED_DESC
        defaultOrgNotificationShouldNotBeFound("desc.contains=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDescNotContainsSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where desc does not contain DEFAULT_DESC
        defaultOrgNotificationShouldNotBeFound("desc.doesNotContain=" + DEFAULT_DESC);

        // Get all the orgNotificationList where desc does not contain UPDATED_DESC
        defaultOrgNotificationShouldBeFound("desc.doesNotContain=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where data equals to DEFAULT_DATA
        defaultOrgNotificationShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the orgNotificationList where data equals to UPDATED_DATA
        defaultOrgNotificationShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where data not equals to DEFAULT_DATA
        defaultOrgNotificationShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the orgNotificationList where data not equals to UPDATED_DATA
        defaultOrgNotificationShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDataIsInShouldWork() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where data in DEFAULT_DATA or UPDATED_DATA
        defaultOrgNotificationShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the orgNotificationList where data equals to UPDATED_DATA
        defaultOrgNotificationShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where data is not null
        defaultOrgNotificationShouldBeFound("data.specified=true");

        // Get all the orgNotificationList where data is null
        defaultOrgNotificationShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDataContainsSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where data contains DEFAULT_DATA
        defaultOrgNotificationShouldBeFound("data.contains=" + DEFAULT_DATA);

        // Get all the orgNotificationList where data contains UPDATED_DATA
        defaultOrgNotificationShouldNotBeFound("data.contains=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByDataNotContainsSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where data does not contain DEFAULT_DATA
        defaultOrgNotificationShouldNotBeFound("data.doesNotContain=" + DEFAULT_DATA);

        // Get all the orgNotificationList where data does not contain UPDATED_DATA
        defaultOrgNotificationShouldBeFound("data.doesNotContain=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByIsReadIsEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where isRead equals to DEFAULT_IS_READ
        defaultOrgNotificationShouldBeFound("isRead.equals=" + DEFAULT_IS_READ);

        // Get all the orgNotificationList where isRead equals to UPDATED_IS_READ
        defaultOrgNotificationShouldNotBeFound("isRead.equals=" + UPDATED_IS_READ);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByIsReadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where isRead not equals to DEFAULT_IS_READ
        defaultOrgNotificationShouldNotBeFound("isRead.notEquals=" + DEFAULT_IS_READ);

        // Get all the orgNotificationList where isRead not equals to UPDATED_IS_READ
        defaultOrgNotificationShouldBeFound("isRead.notEquals=" + UPDATED_IS_READ);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByIsReadIsInShouldWork() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where isRead in DEFAULT_IS_READ or UPDATED_IS_READ
        defaultOrgNotificationShouldBeFound("isRead.in=" + DEFAULT_IS_READ + "," + UPDATED_IS_READ);

        // Get all the orgNotificationList where isRead equals to UPDATED_IS_READ
        defaultOrgNotificationShouldNotBeFound("isRead.in=" + UPDATED_IS_READ);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByIsReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where isRead is not null
        defaultOrgNotificationShouldBeFound("isRead.specified=true");

        // Get all the orgNotificationList where isRead is null
        defaultOrgNotificationShouldNotBeFound("isRead.specified=false");
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where status equals to DEFAULT_STATUS
        defaultOrgNotificationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the orgNotificationList where status equals to UPDATED_STATUS
        defaultOrgNotificationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where status not equals to DEFAULT_STATUS
        defaultOrgNotificationShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the orgNotificationList where status not equals to UPDATED_STATUS
        defaultOrgNotificationShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrgNotificationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the orgNotificationList where status equals to UPDATED_STATUS
        defaultOrgNotificationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        // Get all the orgNotificationList where status is not null
        defaultOrgNotificationShouldBeFound("status.specified=true");

        // Get all the orgNotificationList where status is null
        defaultOrgNotificationShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrgNotificationsByOrgUserIsEqualToSomething() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);
        OrgUser orgUser = OrgUserResourceIT.createEntity(em);
        em.persist(orgUser);
        em.flush();
        orgNotification.setOrgUser(orgUser);
        orgNotificationRepository.saveAndFlush(orgNotification);
        Long orgUserId = orgUser.getId();

        // Get all the orgNotificationList where orgUser equals to orgUserId
        defaultOrgNotificationShouldBeFound("orgUserId.equals=" + orgUserId);

        // Get all the orgNotificationList where orgUser equals to (orgUserId + 1)
        defaultOrgNotificationShouldNotBeFound("orgUserId.equals=" + (orgUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrgNotificationShouldBeFound(String filter) throws Exception {
        restOrgNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restOrgNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrgNotificationShouldNotBeFound(String filter) throws Exception {
        restOrgNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrgNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrgNotification() throws Exception {
        // Get the orgNotification
        restOrgNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrgNotification() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();

        // Update the orgNotification
        OrgNotification updatedOrgNotification = orgNotificationRepository.findById(orgNotification.getId()).get();
        // Disconnect from session so that the updates on updatedOrgNotification are not directly saved in db
        em.detach(updatedOrgNotification);
        updatedOrgNotification.title(UPDATED_TITLE).desc(UPDATED_DESC).data(UPDATED_DATA).isRead(UPDATED_IS_READ).status(UPDATED_STATUS);
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(updatedOrgNotification);

        restOrgNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
        OrgNotification testOrgNotification = orgNotificationList.get(orgNotificationList.size() - 1);
        assertThat(testOrgNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOrgNotification.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testOrgNotification.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testOrgNotification.getIsRead()).isEqualTo(UPDATED_IS_READ);
        assertThat(testOrgNotification.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingOrgNotification() throws Exception {
        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();
        orgNotification.setId(count.incrementAndGet());

        // Create the OrgNotification
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgNotification() throws Exception {
        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();
        orgNotification.setId(count.incrementAndGet());

        // Create the OrgNotification
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgNotification() throws Exception {
        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();
        orgNotification.setId(count.incrementAndGet());

        // Create the OrgNotification
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgNotificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrgNotificationWithPatch() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();

        // Update the orgNotification using partial update
        OrgNotification partialUpdatedOrgNotification = new OrgNotification();
        partialUpdatedOrgNotification.setId(orgNotification.getId());

        partialUpdatedOrgNotification.desc(UPDATED_DESC).data(UPDATED_DATA).status(UPDATED_STATUS);

        restOrgNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgNotification))
            )
            .andExpect(status().isOk());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
        OrgNotification testOrgNotification = orgNotificationList.get(orgNotificationList.size() - 1);
        assertThat(testOrgNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrgNotification.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testOrgNotification.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testOrgNotification.getIsRead()).isEqualTo(DEFAULT_IS_READ);
        assertThat(testOrgNotification.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateOrgNotificationWithPatch() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();

        // Update the orgNotification using partial update
        OrgNotification partialUpdatedOrgNotification = new OrgNotification();
        partialUpdatedOrgNotification.setId(orgNotification.getId());

        partialUpdatedOrgNotification
            .title(UPDATED_TITLE)
            .desc(UPDATED_DESC)
            .data(UPDATED_DATA)
            .isRead(UPDATED_IS_READ)
            .status(UPDATED_STATUS);

        restOrgNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgNotification))
            )
            .andExpect(status().isOk());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
        OrgNotification testOrgNotification = orgNotificationList.get(orgNotificationList.size() - 1);
        assertThat(testOrgNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOrgNotification.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testOrgNotification.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testOrgNotification.getIsRead()).isEqualTo(UPDATED_IS_READ);
        assertThat(testOrgNotification.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingOrgNotification() throws Exception {
        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();
        orgNotification.setId(count.incrementAndGet());

        // Create the OrgNotification
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgNotificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgNotification() throws Exception {
        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();
        orgNotification.setId(count.incrementAndGet());

        // Create the OrgNotification
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgNotification() throws Exception {
        int databaseSizeBeforeUpdate = orgNotificationRepository.findAll().size();
        orgNotification.setId(count.incrementAndGet());

        // Create the OrgNotification
        OrgNotificationDTO orgNotificationDTO = orgNotificationMapper.toDto(orgNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgNotification in the database
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrgNotification() throws Exception {
        // Initialize the database
        orgNotificationRepository.saveAndFlush(orgNotification);

        int databaseSizeBeforeDelete = orgNotificationRepository.findAll().size();

        // Delete the orgNotification
        restOrgNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgNotification> orgNotificationList = orgNotificationRepository.findAll();
        assertThat(orgNotificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
