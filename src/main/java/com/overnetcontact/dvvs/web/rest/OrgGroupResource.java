package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.OrgGroupRepository;
import com.overnetcontact.dvvs.service.OrgGroupService;
import com.overnetcontact.dvvs.service.dto.OrgGroupDTO;
import com.overnetcontact.dvvs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.OrgGroup}.
 */
@RestController
@RequestMapping("/api")
public class OrgGroupResource {

    private final Logger log = LoggerFactory.getLogger(OrgGroupResource.class);

    private static final String ENTITY_NAME = "orgGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgGroupService orgGroupService;

    private final OrgGroupRepository orgGroupRepository;

    public OrgGroupResource(OrgGroupService orgGroupService, OrgGroupRepository orgGroupRepository) {
        this.orgGroupService = orgGroupService;
        this.orgGroupRepository = orgGroupRepository;
    }

    /**
     * {@code POST  /org-groups} : Create a new orgGroup.
     *
     * @param orgGroupDTO the orgGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgGroupDTO, or with status {@code 400 (Bad Request)} if the orgGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/org-groups")
    public ResponseEntity<OrgGroupDTO> createOrgGroup(@Valid @RequestBody OrgGroupDTO orgGroupDTO) throws URISyntaxException {
        log.debug("REST request to save OrgGroup : {}", orgGroupDTO);
        if (orgGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new orgGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgGroupDTO result = orgGroupService.save(orgGroupDTO);
        return ResponseEntity
            .created(new URI("/api/org-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /org-groups/:id} : Updates an existing orgGroup.
     *
     * @param id the id of the orgGroupDTO to save.
     * @param orgGroupDTO the orgGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgGroupDTO,
     * or with status {@code 400 (Bad Request)} if the orgGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/org-groups/{id}")
    public ResponseEntity<OrgGroupDTO> updateOrgGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrgGroupDTO orgGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrgGroup : {}, {}", id, orgGroupDTO);
        if (orgGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrgGroupDTO result = orgGroupService.save(orgGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /org-groups/:id} : Partial updates given fields of an existing orgGroup, field will ignore if it is null
     *
     * @param id the id of the orgGroupDTO to save.
     * @param orgGroupDTO the orgGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgGroupDTO,
     * or with status {@code 400 (Bad Request)} if the orgGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orgGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/org-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrgGroupDTO> partialUpdateOrgGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrgGroupDTO orgGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgGroup partially : {}, {}", id, orgGroupDTO);
        if (orgGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgGroupDTO> result = orgGroupService.partialUpdate(orgGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /org-groups} : get all the orgGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgGroups in body.
     */
    @GetMapping("/org-groups")
    public ResponseEntity<List<OrgGroupDTO>> getAllOrgGroups(Pageable pageable) {
        log.debug("REST request to get a page of OrgGroups");
        Page<OrgGroupDTO> page = orgGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /org-groups/:id} : get the "id" orgGroup.
     *
     * @param id the id of the orgGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/org-groups/{id}")
    public ResponseEntity<OrgGroupDTO> getOrgGroup(@PathVariable Long id) {
        log.debug("REST request to get OrgGroup : {}", id);
        Optional<OrgGroupDTO> orgGroupDTO = orgGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgGroupDTO);
    }

    /**
     * {@code DELETE  /org-groups/:id} : delete the "id" orgGroup.
     *
     * @param id the id of the orgGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/org-groups/{id}")
    public ResponseEntity<Void> deleteOrgGroup(@PathVariable Long id) {
        log.debug("REST request to delete OrgGroup : {}", id);
        orgGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
