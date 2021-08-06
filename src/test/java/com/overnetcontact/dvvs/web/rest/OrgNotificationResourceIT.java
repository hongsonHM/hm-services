package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.OrgNotification;
import com.overnetcontact.dvvs.domain.enumeration.NotificationStatus;
import com.overnetcontact.dvvs.repository.OrgNotificationRepository;
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
