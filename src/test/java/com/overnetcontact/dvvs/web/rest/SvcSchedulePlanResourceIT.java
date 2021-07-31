package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcSchedulePlan;
import com.overnetcontact.dvvs.repository.SvcSchedulePlanRepository;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanDTO;
import com.overnetcontact.dvvs.service.mapper.SvcSchedulePlanMapper;
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
 * Integration tests for the {@link SvcSchedulePlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcSchedulePlanResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/svc-schedule-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcSchedulePlanRepository svcSchedulePlanRepository;

    @Autowired
    private SvcSchedulePlanMapper svcSchedulePlanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcSchedulePlanMockMvc;

    private SvcSchedulePlan svcSchedulePlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcSchedulePlan createEntity(EntityManager em) {
        SvcSchedulePlan svcSchedulePlan = new SvcSchedulePlan().active(DEFAULT_ACTIVE);
        return svcSchedulePlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcSchedulePlan createUpdatedEntity(EntityManager em) {
        SvcSchedulePlan svcSchedulePlan = new SvcSchedulePlan().active(UPDATED_ACTIVE);
        return svcSchedulePlan;
    }

    @BeforeEach
    public void initTest() {
        svcSchedulePlan = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcSchedulePlan() throws Exception {
        int databaseSizeBeforeCreate = svcSchedulePlanRepository.findAll().size();
        // Create the SvcSchedulePlan
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);
        restSvcSchedulePlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeCreate + 1);
        SvcSchedulePlan testSvcSchedulePlan = svcSchedulePlanList.get(svcSchedulePlanList.size() - 1);
        assertThat(testSvcSchedulePlan.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createSvcSchedulePlanWithExistingId() throws Exception {
        // Create the SvcSchedulePlan with an existing ID
        svcSchedulePlan.setId(1L);
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);

        int databaseSizeBeforeCreate = svcSchedulePlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcSchedulePlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcSchedulePlanRepository.findAll().size();
        // set the field null
        svcSchedulePlan.setActive(null);

        // Create the SvcSchedulePlan, which fails.
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);

        restSvcSchedulePlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcSchedulePlans() throws Exception {
        // Initialize the database
        svcSchedulePlanRepository.saveAndFlush(svcSchedulePlan);

        // Get all the svcSchedulePlanList
        restSvcSchedulePlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcSchedulePlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSvcSchedulePlan() throws Exception {
        // Initialize the database
        svcSchedulePlanRepository.saveAndFlush(svcSchedulePlan);

        // Get the svcSchedulePlan
        restSvcSchedulePlanMockMvc
            .perform(get(ENTITY_API_URL_ID, svcSchedulePlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcSchedulePlan.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSvcSchedulePlan() throws Exception {
        // Get the svcSchedulePlan
        restSvcSchedulePlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcSchedulePlan() throws Exception {
        // Initialize the database
        svcSchedulePlanRepository.saveAndFlush(svcSchedulePlan);

        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();

        // Update the svcSchedulePlan
        SvcSchedulePlan updatedSvcSchedulePlan = svcSchedulePlanRepository.findById(svcSchedulePlan.getId()).get();
        // Disconnect from session so that the updates on updatedSvcSchedulePlan are not directly saved in db
        em.detach(updatedSvcSchedulePlan);
        updatedSvcSchedulePlan.active(UPDATED_ACTIVE);
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(updatedSvcSchedulePlan);

        restSvcSchedulePlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcSchedulePlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
        SvcSchedulePlan testSvcSchedulePlan = svcSchedulePlanList.get(svcSchedulePlanList.size() - 1);
        assertThat(testSvcSchedulePlan.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSvcSchedulePlan() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();
        svcSchedulePlan.setId(count.incrementAndGet());

        // Create the SvcSchedulePlan
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcSchedulePlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcSchedulePlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcSchedulePlan() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();
        svcSchedulePlan.setId(count.incrementAndGet());

        // Create the SvcSchedulePlan
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSchedulePlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcSchedulePlan() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();
        svcSchedulePlan.setId(count.incrementAndGet());

        // Create the SvcSchedulePlan
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSchedulePlanMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcSchedulePlanWithPatch() throws Exception {
        // Initialize the database
        svcSchedulePlanRepository.saveAndFlush(svcSchedulePlan);

        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();

        // Update the svcSchedulePlan using partial update
        SvcSchedulePlan partialUpdatedSvcSchedulePlan = new SvcSchedulePlan();
        partialUpdatedSvcSchedulePlan.setId(svcSchedulePlan.getId());

        partialUpdatedSvcSchedulePlan.active(UPDATED_ACTIVE);

        restSvcSchedulePlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcSchedulePlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcSchedulePlan))
            )
            .andExpect(status().isOk());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
        SvcSchedulePlan testSvcSchedulePlan = svcSchedulePlanList.get(svcSchedulePlanList.size() - 1);
        assertThat(testSvcSchedulePlan.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSvcSchedulePlanWithPatch() throws Exception {
        // Initialize the database
        svcSchedulePlanRepository.saveAndFlush(svcSchedulePlan);

        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();

        // Update the svcSchedulePlan using partial update
        SvcSchedulePlan partialUpdatedSvcSchedulePlan = new SvcSchedulePlan();
        partialUpdatedSvcSchedulePlan.setId(svcSchedulePlan.getId());

        partialUpdatedSvcSchedulePlan.active(UPDATED_ACTIVE);

        restSvcSchedulePlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcSchedulePlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcSchedulePlan))
            )
            .andExpect(status().isOk());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
        SvcSchedulePlan testSvcSchedulePlan = svcSchedulePlanList.get(svcSchedulePlanList.size() - 1);
        assertThat(testSvcSchedulePlan.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSvcSchedulePlan() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();
        svcSchedulePlan.setId(count.incrementAndGet());

        // Create the SvcSchedulePlan
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcSchedulePlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcSchedulePlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcSchedulePlan() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();
        svcSchedulePlan.setId(count.incrementAndGet());

        // Create the SvcSchedulePlan
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSchedulePlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcSchedulePlan() throws Exception {
        int databaseSizeBeforeUpdate = svcSchedulePlanRepository.findAll().size();
        svcSchedulePlan.setId(count.incrementAndGet());

        // Create the SvcSchedulePlan
        SvcSchedulePlanDTO svcSchedulePlanDTO = svcSchedulePlanMapper.toDto(svcSchedulePlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcSchedulePlanMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcSchedulePlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcSchedulePlan in the database
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcSchedulePlan() throws Exception {
        // Initialize the database
        svcSchedulePlanRepository.saveAndFlush(svcSchedulePlan);

        int databaseSizeBeforeDelete = svcSchedulePlanRepository.findAll().size();

        // Delete the svcSchedulePlan
        restSvcSchedulePlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcSchedulePlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcSchedulePlan> svcSchedulePlanList = svcSchedulePlanRepository.findAll();
        assertThat(svcSchedulePlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
