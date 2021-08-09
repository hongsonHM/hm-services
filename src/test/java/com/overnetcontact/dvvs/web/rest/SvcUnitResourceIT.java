package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcGroup;
import com.overnetcontact.dvvs.domain.SvcUnit;
import com.overnetcontact.dvvs.repository.SvcUnitRepository;
import com.overnetcontact.dvvs.service.criteria.SvcUnitCriteria;
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
    void getSvcUnitsByIdFiltering() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        Long id = svcUnit.getId();

        defaultSvcUnitShouldBeFound("id.equals=" + id);
        defaultSvcUnitShouldNotBeFound("id.notEquals=" + id);

        defaultSvcUnitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcUnitShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcUnitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcUnitShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where name equals to DEFAULT_NAME
        defaultSvcUnitShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the svcUnitList where name equals to UPDATED_NAME
        defaultSvcUnitShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where name not equals to DEFAULT_NAME
        defaultSvcUnitShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the svcUnitList where name not equals to UPDATED_NAME
        defaultSvcUnitShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSvcUnitShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the svcUnitList where name equals to UPDATED_NAME
        defaultSvcUnitShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where name is not null
        defaultSvcUnitShouldBeFound("name.specified=true");

        // Get all the svcUnitList where name is null
        defaultSvcUnitShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcUnitsByNameContainsSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where name contains DEFAULT_NAME
        defaultSvcUnitShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the svcUnitList where name contains UPDATED_NAME
        defaultSvcUnitShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where name does not contain DEFAULT_NAME
        defaultSvcUnitShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the svcUnitList where name does not contain UPDATED_NAME
        defaultSvcUnitShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where description equals to DEFAULT_DESCRIPTION
        defaultSvcUnitShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the svcUnitList where description equals to UPDATED_DESCRIPTION
        defaultSvcUnitShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where description not equals to DEFAULT_DESCRIPTION
        defaultSvcUnitShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the svcUnitList where description not equals to UPDATED_DESCRIPTION
        defaultSvcUnitShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSvcUnitShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the svcUnitList where description equals to UPDATED_DESCRIPTION
        defaultSvcUnitShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where description is not null
        defaultSvcUnitShouldBeFound("description.specified=true");

        // Get all the svcUnitList where description is null
        defaultSvcUnitShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcUnitsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where description contains DEFAULT_DESCRIPTION
        defaultSvcUnitShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the svcUnitList where description contains UPDATED_DESCRIPTION
        defaultSvcUnitShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);

        // Get all the svcUnitList where description does not contain DEFAULT_DESCRIPTION
        defaultSvcUnitShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the svcUnitList where description does not contain UPDATED_DESCRIPTION
        defaultSvcUnitShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSvcUnitsByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        svcUnitRepository.saveAndFlush(svcUnit);
        SvcGroup group = SvcGroupResourceIT.createEntity(em);
        em.persist(group);
        em.flush();
        svcUnit.setGroup(group);
        svcUnitRepository.saveAndFlush(svcUnit);
        Long groupId = group.getId();

        // Get all the svcUnitList where group equals to groupId
        defaultSvcUnitShouldBeFound("groupId.equals=" + groupId);

        // Get all the svcUnitList where group equals to (groupId + 1)
        defaultSvcUnitShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcUnitShouldBeFound(String filter) throws Exception {
        restSvcUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restSvcUnitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcUnitShouldNotBeFound(String filter) throws Exception {
        restSvcUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcUnitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
