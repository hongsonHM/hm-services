package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.SvcGroup;
import com.overnetcontact.dvvs.repository.SvcGroupRepository;
import com.overnetcontact.dvvs.service.dto.SvcGroupDTO;
import com.overnetcontact.dvvs.service.mapper.SvcGroupMapper;
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
 * Integration tests for the {@link SvcGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SvcGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/svc-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SvcGroupRepository svcGroupRepository;

    @Autowired
    private SvcGroupMapper svcGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSvcGroupMockMvc;

    private SvcGroup svcGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcGroup createEntity(EntityManager em) {
        SvcGroup svcGroup = new SvcGroup().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).address(DEFAULT_ADDRESS);
        return svcGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SvcGroup createUpdatedEntity(EntityManager em) {
        SvcGroup svcGroup = new SvcGroup().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS);
        return svcGroup;
    }

    @BeforeEach
    public void initTest() {
        svcGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createSvcGroup() throws Exception {
        int databaseSizeBeforeCreate = svcGroupRepository.findAll().size();
        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);
        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeCreate + 1);
        SvcGroup testSvcGroup = svcGroupList.get(svcGroupList.size() - 1);
        assertThat(testSvcGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSvcGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSvcGroup.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createSvcGroupWithExistingId() throws Exception {
        // Create the SvcGroup with an existing ID
        svcGroup.setId(1L);
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        int databaseSizeBeforeCreate = svcGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcGroupRepository.findAll().size();
        // set the field null
        svcGroup.setName(null);

        // Create the SvcGroup, which fails.
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isBadRequest());

        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcGroupRepository.findAll().size();
        // set the field null
        svcGroup.setDescription(null);

        // Create the SvcGroup, which fails.
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isBadRequest());

        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = svcGroupRepository.findAll().size();
        // set the field null
        svcGroup.setAddress(null);

        // Create the SvcGroup, which fails.
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        restSvcGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isBadRequest());

        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSvcGroups() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get all the svcGroupList
        restSvcGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(svcGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getSvcGroup() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        // Get the svcGroup
        restSvcGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, svcGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(svcGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingSvcGroup() throws Exception {
        // Get the svcGroup
        restSvcGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSvcGroup() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();

        // Update the svcGroup
        SvcGroup updatedSvcGroup = svcGroupRepository.findById(svcGroup.getId()).get();
        // Disconnect from session so that the updates on updatedSvcGroup are not directly saved in db
        em.detach(updatedSvcGroup);
        updatedSvcGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS);
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(updatedSvcGroup);

        restSvcGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
        SvcGroup testSvcGroup = svcGroupList.get(svcGroupList.size() - 1);
        assertThat(testSvcGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSvcGroup.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, svcGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(svcGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSvcGroupWithPatch() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();

        // Update the svcGroup using partial update
        SvcGroup partialUpdatedSvcGroup = new SvcGroup();
        partialUpdatedSvcGroup.setId(svcGroup.getId());

        partialUpdatedSvcGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS);

        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcGroup))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
        SvcGroup testSvcGroup = svcGroupList.get(svcGroupList.size() - 1);
        assertThat(testSvcGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSvcGroup.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateSvcGroupWithPatch() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();

        // Update the svcGroup using partial update
        SvcGroup partialUpdatedSvcGroup = new SvcGroup();
        partialUpdatedSvcGroup.setId(svcGroup.getId());

        partialUpdatedSvcGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS);

        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSvcGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSvcGroup))
            )
            .andExpect(status().isOk());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
        SvcGroup testSvcGroup = svcGroupList.get(svcGroupList.size() - 1);
        assertThat(testSvcGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSvcGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSvcGroup.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, svcGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSvcGroup() throws Exception {
        int databaseSizeBeforeUpdate = svcGroupRepository.findAll().size();
        svcGroup.setId(count.incrementAndGet());

        // Create the SvcGroup
        SvcGroupDTO svcGroupDTO = svcGroupMapper.toDto(svcGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSvcGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(svcGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SvcGroup in the database
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSvcGroup() throws Exception {
        // Initialize the database
        svcGroupRepository.saveAndFlush(svcGroup);

        int databaseSizeBeforeDelete = svcGroupRepository.findAll().size();

        // Delete the svcGroup
        restSvcGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, svcGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SvcGroup> svcGroupList = svcGroupRepository.findAll();
        assertThat(svcGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
