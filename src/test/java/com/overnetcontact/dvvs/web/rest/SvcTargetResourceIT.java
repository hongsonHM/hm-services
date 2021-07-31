package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcTarget;
import com.overnetcontact.dvvs.repository.SvcTargetRepository;
import com.overnetcontact.dvvs.service.dto.SvcTargetDTO;
import com.overnetcontact.dvvs.service.mapper.SvcTargetMapper;
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
 * Integration tests for the {@link SvcTargetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcTargetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-targets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcTargetRepository svcTargetRepository;

    @Autowired
    private SvcTargetMapper svcTargetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcTargetMockMvc;

    private SvcTarget svcTarget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcTarget createEntity(EntityManager em) {
        SvcTarget svcTarget = new SvcTarget().name(DEFAULT_NAME);
        return svcTarget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcTarget createUpdatedEntity(EntityManager em) {
        SvcTarget svcTarget = new SvcTarget().name(UPDATED_NAME);
        return svcTarget;
    }

    @BeforeEach
    public void initTest() {
        svcTarget = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcTarget() throws Exception {
        int databaseSizeBeforeCreate = svcTargetRepository.findAll().size();
        // Create the SvcTarget
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(svcTarget);
        restSvcTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcTargetDTO)))
            .andExpect(status().isCreated());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeCreate + 1);
        SvcTarget testSvcTarget = svcTargetList.get(svcTargetList.size() - 1);
        assertThat(testSvcTarget.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSvcTargetWithExistingId() throws Exception {
        // Create the SvcTarget with an existing ID
        svcTarget.setId(1L);
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(svcTarget);

        int databaseSizeBeforeCreate = svcTargetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcTargetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSvcTargets() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        // Get all the svcTargetList
        restSvcTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcTarget.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSvcTarget() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        // Get the svcTarget
        restSvcTargetMockMvc
            .perform(get(ENTITY_API_URL_ID, svcTarget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcTarget.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSvcTarget() throws Exception {
        // Get the svcTarget
        restSvcTargetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcTarget() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();

        // Update the svcTarget
        SvcTarget updatedSvcTarget = svcTargetRepository.findById(svcTarget.getId()).get();
        // Disconnect from session so that the updates on updatedSvcTarget are not directly saved in db
        em.detach(updatedSvcTarget);
        updatedSvcTarget.name(UPDATED_NAME);
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(updatedSvcTarget);

        restSvcTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcTargetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
        SvcTarget testSvcTarget = svcTargetList.get(svcTargetList.size() - 1);
        assertThat(testSvcTarget.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSvcTarget() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();
        svcTarget.setId(count.incrementAndGet());

        // Create the SvcTarget
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(svcTarget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcTargetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcTarget() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();
        svcTarget.setId(count.incrementAndGet());

        // Create the SvcTarget
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(svcTarget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcTarget() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();
        svcTarget.setId(count.incrementAndGet());

        // Create the SvcTarget
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(svcTarget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcTargetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcTargetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcTargetWithPatch() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();

        // Update the svcTarget using partial update
        SvcTarget partialUpdatedSvcTarget = new SvcTarget();
        partialUpdatedSvcTarget.setId(svcTarget.getId());

        partialUpdatedSvcTarget.name(UPDATED_NAME);

        restSvcTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcTarget))
            )
            .andExpect(status().isOk());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
        SvcTarget testSvcTarget = svcTargetList.get(svcTargetList.size() - 1);
        assertThat(testSvcTarget.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSvcTargetWithPatch() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();

        // Update the svcTarget using partial update
        SvcTarget partialUpdatedSvcTarget = new SvcTarget();
        partialUpdatedSvcTarget.setId(svcTarget.getId());

        partialUpdatedSvcTarget.name(UPDATED_NAME);

        restSvcTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcTarget))
            )
            .andExpect(status().isOk());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
        SvcTarget testSvcTarget = svcTargetList.get(svcTargetList.size() - 1);
        assertThat(testSvcTarget.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSvcTarget() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();
        svcTarget.setId(count.incrementAndGet());

        // Create the SvcTarget
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(svcTarget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcTargetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcTarget() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();
        svcTarget.setId(count.incrementAndGet());

        // Create the SvcTarget
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(svcTarget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcTarget() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetRepository.findAll().size();
        svcTarget.setId(count.incrementAndGet());

        // Create the SvcTarget
        SvcTargetDTO svcTargetDTO = svcTargetMapper.toDto(svcTarget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcTargetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcTargetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcTarget in the database
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcTarget() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        int databaseSizeBeforeDelete = svcTargetRepository.findAll().size();

        // Delete the svcTarget
        restSvcTargetMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcTarget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcTarget> svcTargetList = svcTargetRepository.findAll();
        assertThat(svcTargetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
