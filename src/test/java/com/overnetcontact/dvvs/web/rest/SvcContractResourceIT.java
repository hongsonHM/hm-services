package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
import com.overnetcontact.dvvs.service.dto.SvcContractDTO;
import com.overnetcontact.dvvs.service.mapper.SvcContractMapper;
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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_EFFECTIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
            .name(DEFAULT_NAME)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE);
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
            .name(UPDATED_NAME)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE);
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
        assertThat(testSvcContract.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcContract.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testSvcContract.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
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
    void checkEffectiveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setEffectiveDate(null);

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
    void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcContractRepository.findAll().size();
        // set the field null
        svcContract.setExpirationDate(null);

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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()));
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
        updatedSvcContract.name(UPDATED_NAME).effectiveDate(UPDATED_EFFECTIVE_DATE).expirationDate(UPDATED_EXPIRATION_DATE);
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
        assertThat(testSvcContract.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcContract.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testSvcContract.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
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

        partialUpdatedSvcContract.effectiveDate(UPDATED_EFFECTIVE_DATE).expirationDate(UPDATED_EXPIRATION_DATE);

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
        assertThat(testSvcContract.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcContract.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testSvcContract.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
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

        partialUpdatedSvcContract.name(UPDATED_NAME).effectiveDate(UPDATED_EFFECTIVE_DATE).expirationDate(UPDATED_EXPIRATION_DATE);

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
        assertThat(testSvcContract.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcContract.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testSvcContract.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
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
