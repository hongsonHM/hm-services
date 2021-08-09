package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcTargetType;
import com.overnetcontact.dvvs.repository.SvcTargetTypeRepository;
import com.overnetcontact.dvvs.service.criteria.SvcTargetTypeCriteria;
import com.overnetcontact.dvvs.service.dto.SvcTargetTypeDTO;
import com.overnetcontact.dvvs.service.mapper.SvcTargetTypeMapper;
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
 * Integration tests for the {@link SvcTargetTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcTargetTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-target-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcTargetTypeRepository svcTargetTypeRepository;

    @Autowired
    private SvcTargetTypeMapper svcTargetTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcTargetTypeMockMvc;

    private SvcTargetType svcTargetType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcTargetType createEntity(EntityManager em) {
        SvcTargetType svcTargetType = new SvcTargetType().name(DEFAULT_NAME);
        return svcTargetType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcTargetType createUpdatedEntity(EntityManager em) {
        SvcTargetType svcTargetType = new SvcTargetType().name(UPDATED_NAME);
        return svcTargetType;
    }

    @BeforeEach
    public void initTest() {
        svcTargetType = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcTargetType() throws Exception {
        int databaseSizeBeforeCreate = svcTargetTypeRepository.findAll().size();
        // Create the SvcTargetType
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(svcTargetType);
        restSvcTargetTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SvcTargetType testSvcTargetType = svcTargetTypeList.get(svcTargetTypeList.size() - 1);
        assertThat(testSvcTargetType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSvcTargetTypeWithExistingId() throws Exception {
        // Create the SvcTargetType with an existing ID
        svcTargetType.setId(1L);
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(svcTargetType);

        int databaseSizeBeforeCreate = svcTargetTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcTargetTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSvcTargetTypes() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        // Get all the svcTargetTypeList
        restSvcTargetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcTargetType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSvcTargetType() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        // Get the svcTargetType
        restSvcTargetTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, svcTargetType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcTargetType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getSvcTargetTypesByIdFiltering() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        Long id = svcTargetType.getId();

        defaultSvcTargetTypeShouldBeFound("id.equals=" + id);
        defaultSvcTargetTypeShouldNotBeFound("id.notEquals=" + id);

        defaultSvcTargetTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcTargetTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcTargetTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcTargetTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcTargetTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        // Get all the svcTargetTypeList where name equals to DEFAULT_NAME
        defaultSvcTargetTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the svcTargetTypeList where name equals to UPDATED_NAME
        defaultSvcTargetTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        // Get all the svcTargetTypeList where name not equals to DEFAULT_NAME
        defaultSvcTargetTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the svcTargetTypeList where name not equals to UPDATED_NAME
        defaultSvcTargetTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        // Get all the svcTargetTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSvcTargetTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the svcTargetTypeList where name equals to UPDATED_NAME
        defaultSvcTargetTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        // Get all the svcTargetTypeList where name is not null
        defaultSvcTargetTypeShouldBeFound("name.specified=true");

        // Get all the svcTargetTypeList where name is null
        defaultSvcTargetTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcTargetTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        // Get all the svcTargetTypeList where name contains DEFAULT_NAME
        defaultSvcTargetTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the svcTargetTypeList where name contains UPDATED_NAME
        defaultSvcTargetTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        // Get all the svcTargetTypeList where name does not contain DEFAULT_NAME
        defaultSvcTargetTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the svcTargetTypeList where name does not contain UPDATED_NAME
        defaultSvcTargetTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcTargetTypeShouldBeFound(String filter) throws Exception {
        restSvcTargetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcTargetType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restSvcTargetTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcTargetTypeShouldNotBeFound(String filter) throws Exception {
        restSvcTargetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcTargetTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSvcTargetType() throws Exception {
        // Get the svcTargetType
        restSvcTargetTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcTargetType() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();

        // Update the svcTargetType
        SvcTargetType updatedSvcTargetType = svcTargetTypeRepository.findById(svcTargetType.getId()).get();
        // Disconnect from session so that the updates on updatedSvcTargetType are not directly saved in db
        em.detach(updatedSvcTargetType);
        updatedSvcTargetType.name(UPDATED_NAME);
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(updatedSvcTargetType);

        restSvcTargetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcTargetTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
        SvcTargetType testSvcTargetType = svcTargetTypeList.get(svcTargetTypeList.size() - 1);
        assertThat(testSvcTargetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSvcTargetType() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();
        svcTargetType.setId(count.incrementAndGet());

        // Create the SvcTargetType
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(svcTargetType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcTargetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcTargetTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcTargetType() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();
        svcTargetType.setId(count.incrementAndGet());

        // Create the SvcTargetType
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(svcTargetType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcTargetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcTargetType() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();
        svcTargetType.setId(count.incrementAndGet());

        // Create the SvcTargetType
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(svcTargetType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcTargetTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcTargetTypeWithPatch() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();

        // Update the svcTargetType using partial update
        SvcTargetType partialUpdatedSvcTargetType = new SvcTargetType();
        partialUpdatedSvcTargetType.setId(svcTargetType.getId());

        restSvcTargetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcTargetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcTargetType))
            )
            .andExpect(status().isOk());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
        SvcTargetType testSvcTargetType = svcTargetTypeList.get(svcTargetTypeList.size() - 1);
        assertThat(testSvcTargetType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSvcTargetTypeWithPatch() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();

        // Update the svcTargetType using partial update
        SvcTargetType partialUpdatedSvcTargetType = new SvcTargetType();
        partialUpdatedSvcTargetType.setId(svcTargetType.getId());

        partialUpdatedSvcTargetType.name(UPDATED_NAME);

        restSvcTargetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcTargetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcTargetType))
            )
            .andExpect(status().isOk());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
        SvcTargetType testSvcTargetType = svcTargetTypeList.get(svcTargetTypeList.size() - 1);
        assertThat(testSvcTargetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSvcTargetType() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();
        svcTargetType.setId(count.incrementAndGet());

        // Create the SvcTargetType
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(svcTargetType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcTargetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcTargetTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcTargetType() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();
        svcTargetType.setId(count.incrementAndGet());

        // Create the SvcTargetType
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(svcTargetType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcTargetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcTargetType() throws Exception {
        int databaseSizeBeforeUpdate = svcTargetTypeRepository.findAll().size();
        svcTargetType.setId(count.incrementAndGet());

        // Create the SvcTargetType
        SvcTargetTypeDTO svcTargetTypeDTO = svcTargetTypeMapper.toDto(svcTargetType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcTargetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcTargetTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcTargetType in the database
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcTargetType() throws Exception {
        // Initialize the database
        svcTargetTypeRepository.saveAndFlush(svcTargetType);

        int databaseSizeBeforeDelete = svcTargetTypeRepository.findAll().size();

        // Delete the svcTargetType
        restSvcTargetTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcTargetType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcTargetType> svcTargetTypeList = svcTargetTypeRepository.findAll();
        assertThat(svcTargetTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
