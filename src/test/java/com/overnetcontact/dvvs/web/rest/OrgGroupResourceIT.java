package com.overnetcontact.dvvs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.overnetcontact.dvvs.IntegrationTest;
import com.overnetcontact.dvvs.domain.OrgGroup;
import com.overnetcontact.dvvs.repository.OrgGroupRepository;
import com.overnetcontact.dvvs.service.dto.OrgGroupDTO;
import com.overnetcontact.dvvs.service.mapper.OrgGroupMapper;
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
 * Integration tests for the {@link OrgGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/org-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgGroupRepository orgGroupRepository;

    @Autowired
    private OrgGroupMapper orgGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgGroupMockMvc;

    private OrgGroup orgGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgGroup createEntity(EntityManager em) {
        OrgGroup orgGroup = new OrgGroup().name(DEFAULT_NAME);
        return orgGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgGroup createUpdatedEntity(EntityManager em) {
        OrgGroup orgGroup = new OrgGroup().name(UPDATED_NAME);
        return orgGroup;
    }

    @BeforeEach
    public void initTest() {
        orgGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createOrgGroup() throws Exception {
        int databaseSizeBeforeCreate = orgGroupRepository.findAll().size();
        // Create the OrgGroup
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(orgGroup);
        restOrgGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeCreate + 1);
        OrgGroup testOrgGroup = orgGroupList.get(orgGroupList.size() - 1);
        assertThat(testOrgGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOrgGroupWithExistingId() throws Exception {
        // Create the OrgGroup with an existing ID
        orgGroup.setId(1L);
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(orgGroup);

        int databaseSizeBeforeCreate = orgGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrgGroups() throws Exception {
        // Initialize the database
        orgGroupRepository.saveAndFlush(orgGroup);

        // Get all the orgGroupList
        restOrgGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getOrgGroup() throws Exception {
        // Initialize the database
        orgGroupRepository.saveAndFlush(orgGroup);

        // Get the orgGroup
        restOrgGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, orgGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOrgGroup() throws Exception {
        // Get the orgGroup
        restOrgGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrgGroup() throws Exception {
        // Initialize the database
        orgGroupRepository.saveAndFlush(orgGroup);

        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();

        // Update the orgGroup
        OrgGroup updatedOrgGroup = orgGroupRepository.findById(orgGroup.getId()).get();
        // Disconnect from session so that the updates on updatedOrgGroup are not directly saved in db
        em.detach(updatedOrgGroup);
        updatedOrgGroup.name(UPDATED_NAME);
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(updatedOrgGroup);

        restOrgGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
        OrgGroup testOrgGroup = orgGroupList.get(orgGroupList.size() - 1);
        assertThat(testOrgGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOrgGroup() throws Exception {
        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();
        orgGroup.setId(count.incrementAndGet());

        // Create the OrgGroup
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(orgGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgGroup() throws Exception {
        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();
        orgGroup.setId(count.incrementAndGet());

        // Create the OrgGroup
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(orgGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgGroup() throws Exception {
        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();
        orgGroup.setId(count.incrementAndGet());

        // Create the OrgGroup
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(orgGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrgGroupWithPatch() throws Exception {
        // Initialize the database
        orgGroupRepository.saveAndFlush(orgGroup);

        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();

        // Update the orgGroup using partial update
        OrgGroup partialUpdatedOrgGroup = new OrgGroup();
        partialUpdatedOrgGroup.setId(orgGroup.getId());

        restOrgGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgGroup))
            )
            .andExpect(status().isOk());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
        OrgGroup testOrgGroup = orgGroupList.get(orgGroupList.size() - 1);
        assertThat(testOrgGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOrgGroupWithPatch() throws Exception {
        // Initialize the database
        orgGroupRepository.saveAndFlush(orgGroup);

        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();

        // Update the orgGroup using partial update
        OrgGroup partialUpdatedOrgGroup = new OrgGroup();
        partialUpdatedOrgGroup.setId(orgGroup.getId());

        partialUpdatedOrgGroup.name(UPDATED_NAME);

        restOrgGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgGroup))
            )
            .andExpect(status().isOk());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
        OrgGroup testOrgGroup = orgGroupList.get(orgGroupList.size() - 1);
        assertThat(testOrgGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOrgGroup() throws Exception {
        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();
        orgGroup.setId(count.incrementAndGet());

        // Create the OrgGroup
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(orgGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgGroup() throws Exception {
        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();
        orgGroup.setId(count.incrementAndGet());

        // Create the OrgGroup
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(orgGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgGroup() throws Exception {
        int databaseSizeBeforeUpdate = orgGroupRepository.findAll().size();
        orgGroup.setId(count.incrementAndGet());

        // Create the OrgGroup
        OrgGroupDTO orgGroupDTO = orgGroupMapper.toDto(orgGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orgGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgGroup in the database
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrgGroup() throws Exception {
        // Initialize the database
        orgGroupRepository.saveAndFlush(orgGroup);

        int databaseSizeBeforeDelete = orgGroupRepository.findAll().size();

        // Delete the orgGroup
        restOrgGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgGroup> orgGroupList = orgGroupRepository.findAll();
        assertThat(orgGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
