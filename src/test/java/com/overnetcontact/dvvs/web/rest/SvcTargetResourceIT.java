package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.domain.SvcTarget;
import com.overnetcontact.dvvs.domain.SvcTarget;
import com.overnetcontact.dvvs.domain.SvcTargetType;
import com.overnetcontact.dvvs.repository.SvcTargetRepository;
import com.overnetcontact.dvvs.service.criteria.SvcTargetCriteria;
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
    void getSvcTargetsByIdFiltering() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        Long id = svcTarget.getId();

        defaultSvcTargetShouldBeFound("id.equals=" + id);
        defaultSvcTargetShouldNotBeFound("id.notEquals=" + id);

        defaultSvcTargetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSvcTargetShouldNotBeFound("id.greaterThan=" + id);

        defaultSvcTargetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSvcTargetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSvcTargetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        // Get all the svcTargetList where name equals to DEFAULT_NAME
        defaultSvcTargetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the svcTargetList where name equals to UPDATED_NAME
        defaultSvcTargetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        // Get all the svcTargetList where name not equals to DEFAULT_NAME
        defaultSvcTargetShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the svcTargetList where name not equals to UPDATED_NAME
        defaultSvcTargetShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        // Get all the svcTargetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSvcTargetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the svcTargetList where name equals to UPDATED_NAME
        defaultSvcTargetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        // Get all the svcTargetList where name is not null
        defaultSvcTargetShouldBeFound("name.specified=true");

        // Get all the svcTargetList where name is null
        defaultSvcTargetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSvcTargetsByNameContainsSomething() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        // Get all the svcTargetList where name contains DEFAULT_NAME
        defaultSvcTargetShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the svcTargetList where name contains UPDATED_NAME
        defaultSvcTargetShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);

        // Get all the svcTargetList where name does not contain DEFAULT_NAME
        defaultSvcTargetShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the svcTargetList where name does not contain UPDATED_NAME
        defaultSvcTargetShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSvcTargetsByChildsIsEqualToSomething() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);
        SvcTarget childs = SvcTargetResourceIT.createEntity(em);
        em.persist(childs);
        em.flush();
        svcTarget.addChilds(childs);
        svcTargetRepository.saveAndFlush(svcTarget);
        Long childsId = childs.getId();

        // Get all the svcTargetList where childs equals to childsId
        defaultSvcTargetShouldBeFound("childsId.equals=" + childsId);

        // Get all the svcTargetList where childs equals to (childsId + 1)
        defaultSvcTargetShouldNotBeFound("childsId.equals=" + (childsId + 1));
    }

    @Test
    @Transactional
    void getAllSvcTargetsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);
        SvcTargetType type = SvcTargetTypeResourceIT.createEntity(em);
        em.persist(type);
        em.flush();
        svcTarget.setType(type);
        svcTargetRepository.saveAndFlush(svcTarget);
        Long typeId = type.getId();

        // Get all the svcTargetList where type equals to typeId
        defaultSvcTargetShouldBeFound("typeId.equals=" + typeId);

        // Get all the svcTargetList where type equals to (typeId + 1)
        defaultSvcTargetShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    @Test
    @Transactional
    void getAllSvcTargetsBySvcTargetIsEqualToSomething() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);
        SvcTarget svcTarget = SvcTargetResourceIT.createEntity(em);
        em.persist(svcTarget);
        em.flush();
        svcTarget.setSvcTarget(svcTarget);
        svcTargetRepository.saveAndFlush(svcTarget);
        Long svcTargetId = svcTarget.getId();

        // Get all the svcTargetList where svcTarget equals to svcTargetId
        defaultSvcTargetShouldBeFound("svcTargetId.equals=" + svcTargetId);

        // Get all the svcTargetList where svcTarget equals to (svcTargetId + 1)
        defaultSvcTargetShouldNotBeFound("svcTargetId.equals=" + (svcTargetId + 1));
    }

    @Test
    @Transactional
    void getAllSvcTargetsBySvcContractIsEqualToSomething() throws Exception {
        // Initialize the database
        svcTargetRepository.saveAndFlush(svcTarget);
        SvcContract svcContract = SvcContractResourceIT.createEntity(em);
        em.persist(svcContract);
        em.flush();
        svcTarget.setSvcContract(svcContract);
        svcTargetRepository.saveAndFlush(svcTarget);
        Long svcContractId = svcContract.getId();

        // Get all the svcTargetList where svcContract equals to svcContractId
        defaultSvcTargetShouldBeFound("svcContractId.equals=" + svcContractId);

        // Get all the svcTargetList where svcContract equals to (svcContractId + 1)
        defaultSvcTargetShouldNotBeFound("svcContractId.equals=" + (svcContractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSvcTargetShouldBeFound(String filter) throws Exception {
        restSvcTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcTarget.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restSvcTargetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSvcTargetShouldNotBeFound(String filter) throws Exception {
        restSvcTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSvcTargetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
