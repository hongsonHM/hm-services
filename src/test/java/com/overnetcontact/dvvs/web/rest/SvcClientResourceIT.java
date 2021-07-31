package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcClient;
import com.overnetcontact.dvvs.repository.SvcClientRepository;
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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

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
        SvcClient svcClient = new SvcClient().name(DEFAULT_NAME).phoneNumber(DEFAULT_PHONE_NUMBER).address(DEFAULT_ADDRESS);
        return svcClient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcClient createUpdatedEntity(EntityManager em) {
        SvcClient svcClient = new SvcClient().name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).address(UPDATED_ADDRESS);
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
        assertThat(testSvcClient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcClient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
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
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcClientRepository.findAll().size();
        // set the field null
        svcClient.setName(null);

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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
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
        updatedSvcClient.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).address(UPDATED_ADDRESS);
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
        assertThat(testSvcClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcClient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
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

        partialUpdatedSvcClient.name(UPDATED_NAME);

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
        assertThat(testSvcClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcClient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSvcClient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
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

        partialUpdatedSvcClient.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).address(UPDATED_ADDRESS);

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
        assertThat(testSvcClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcClient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
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
