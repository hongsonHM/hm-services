package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.service.OrgUserService;
import com.overnetcontact.dvvs.service.dto.OrgUserDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.OrgUser}.
 */
@RestController
@RequestMapping("/api")
public class OrgUserResource {

    private final Logger log = LoggerFactory.getLogger(OrgUserResource.class);

    private static final String ENTITY_NAME = "orgUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgUserService orgUserService;

    private final OrgUserRepository orgUserRepository;

    public OrgUserResource(OrgUserService orgUserService, OrgUserRepository orgUserRepository) {
        this.orgUserService = orgUserService;
        this.orgUserRepository = orgUserRepository;
    }

    /**
     * {@code POST  /org-users} : Create a new orgUser.
     *
     * @param orgUserDTO the orgUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgUserDTO, or with status {@code 400 (Bad Request)} if the orgUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/org-users")
    public ResponseEntity<OrgUserDTO> createOrgUser(@Valid @RequestBody OrgUserDTO orgUserDTO) throws URISyntaxException {
        log.debug("REST request to save OrgUser : {}", orgUserDTO);
        if (orgUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new orgUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgUserDTO result = orgUserService.save(orgUserDTO);
        return ResponseEntity
            .created(new URI("/api/org-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /org-users/:id} : Updates an existing orgUser.
     *
     * @param id the id of the orgUserDTO to save.
     * @param orgUserDTO the orgUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUserDTO,
     * or with status {@code 400 (Bad Request)} if the orgUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/org-users/{id}")
    public ResponseEntity<OrgUserDTO> updateOrgUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrgUserDTO orgUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrgUser : {}, {}", id, orgUserDTO);
        if (orgUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrgUserDTO result = orgUserService.save(orgUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /org-users/:id} : Partial updates given fields of an existing orgUser, field will ignore if it is null
     *
     * @param id the id of the orgUserDTO to save.
     * @param orgUserDTO the orgUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgUserDTO,
     * or with status {@code 400 (Bad Request)} if the orgUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orgUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/org-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrgUserDTO> partialUpdateOrgUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrgUserDTO orgUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgUser partially : {}, {}", id, orgUserDTO);
        if (orgUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgUserDTO> result = orgUserService.partialUpdate(orgUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /org-users} : get all the orgUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgUsers in body.
     */
    @GetMapping("/org-users")
    public ResponseEntity<List<OrgUserDTO>> getAllOrgUsers(Pageable pageable) {
        log.debug("REST request to get a page of OrgUsers");
        Page<OrgUserDTO> page = orgUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /org-users/:id} : get the "id" orgUser.
     *
     * @param id the id of the orgUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/org-users/{id}")
    public ResponseEntity<OrgUserDTO> getOrgUser(@PathVariable Long id) {
        log.debug("REST request to get OrgUser : {}", id);
        Optional<OrgUserDTO> orgUserDTO = orgUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgUserDTO);
    }

    /**
     * {@code DELETE  /org-users/:id} : delete the "id" orgUser.
     *
     * @param id the id of the orgUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/org-users/{id}")
    public ResponseEntity<Void> deleteOrgUser(@PathVariable Long id) {
        log.debug("REST request to delete OrgUser : {}", id);
        orgUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
