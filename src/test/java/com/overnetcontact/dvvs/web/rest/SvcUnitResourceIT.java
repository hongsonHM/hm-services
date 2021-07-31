package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcUnit;
import com.overnetcontact.dvvs.repository.SvcUnitRepository;
import com.overnetcontact.dvvs.service.dto.SvcUnitDTO;
import com.overnetcontact.dvvs.service.mapper.SvcUnitMapper;
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
 * Integration tests for the {@link SvcUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcUnitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcUnitRepository svcUnitRepository;

    @Autowired
    private SvcUnitMapper svcUnitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcUnitMockMvc;

    private SvcUnit svcUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcUnit createEntity(EntityManager em) {
        SvcUnit svcUnit = new SvcUnit().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return svcUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcUnit createUpdatedEntity(EntityManager em) {
        SvcUnit svcUnit = new SvcUnit().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return svcUnit;
    }

    @BeforeEach
    public void initTest() {
        svcUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcUnit() throws Exception {
        int databaseSizeBeforeCreate = svcUnitRepository.findAll().size();
        // Create the SvcUnit
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);
        restSvcUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcUnitDTO)))
            .andExpect(status().isCreated());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeCreate + 1);
        SvcUnit testSvcUnit = svcUnitList.get(svcUnitList.size() - 1);
        assertThat(testSvcUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createSvcUnitWithExistingId() throws Exception {
        // Create the SvcUnit with an existing ID
        svcUnit.setId(1L);
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        int databaseSizeBeforeCreate = svcUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcUnitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcUnitRepository.findAll().size();
        // set the field null
        svcUnit.setName(null);

        // Create the SvcUnit, which fails.
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        restSvcUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcUnitDTO)))
            .andExpect(status().isBadRequest());

        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcUnitRepository.findAll().size();
        // set the field null
        svcUnit.setDescription(null);

        // Create the SvcUnit, which fails.
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        restSvcUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcUnitDTO)))
            .andExpect(status().isBadRequest());

        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcUnits() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList
        restSvcUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSvcUnit() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get the svcUnit
        restSvcUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, svcUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcUnit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSvcUnit() throws Exception {
        // Get the svcUnit
        restSvcUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcUnit() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();

        // Update the svcUnit
        SvcUnit updatedSvcUnit = svcUnitRepository.findById(svcUnit.getId()).get();
        // Disconnect from session so that the updates on updatedSvcUnit are not directly saved in db
        em.detach(updatedSvcUnit);
        updatedSvcUnit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(updatedSvcUnit);

        restSvcUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcUnitDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcUnit testSvcUnit = svcUnitList.get(svcUnitList.size() - 1);
        assertThat(testSvcUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSvcUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();
        svcUnit.setId(count.incrementAndGet());

        // Create the SvcUnit
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();
        svcUnit.setId(count.incrementAndGet());

        // Create the SvcUnit
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();
        svcUnit.setId(count.incrementAndGet());

        // Create the SvcUnit
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcUnitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcUnitWithPatch() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();

        // Update the svcUnit using partial update
        SvcUnit partialUpdatedSvcUnit = new SvcUnit();
        partialUpdatedSvcUnit.setId(svcUnit.getId());

        partialUpdatedSvcUnit.name(UPDATED_NAME);

        restSvcUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcUnit))
            )
            .andExpect(status().isOk());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcUnit testSvcUnit = svcUnitList.get(svcUnitList.size() - 1);
        assertThat(testSvcUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateSvcUnitWithPatch() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();

        // Update the svcUnit using partial update
        SvcUnit partialUpdatedSvcUnit = new SvcUnit();
        partialUpdatedSvcUnit.setId(svcUnit.getId());

        partialUpdatedSvcUnit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSvcUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcUnit))
            )
            .andExpect(status().isOk());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
        SvcUnit testSvcUnit = svcUnitList.get(svcUnitList.size() - 1);
        assertThat(testSvcUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSvcUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();
        svcUnit.setId(count.incrementAndGet());

        // Create the SvcUnit
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcUnitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();
        svcUnit.setId(count.incrementAndGet());

        // Create the SvcUnit
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcUnit() throws Exception {
        int databaseSizeBeforeUpdate = svcUnitRepository.findAll().size();
        svcUnit.setId(count.incrementAndGet());

        // Create the SvcUnit
        SvcUnitDTO svcUnitDTO = svcUnitMapper.toDto(svcUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcUnit in the database
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcUnit() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        int databaseSizeBeforeDelete = svcUnitRepository.findAll().size();

        // Delete the svcUnit
        restSvcUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcUnit> svcUnitList = svcUnitRepository.findAll();
        assertThat(svcUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
