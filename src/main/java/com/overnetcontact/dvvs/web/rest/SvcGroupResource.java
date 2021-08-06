package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcGroupRepository;
import com.overnetcontact.dvvs.service.SvcGroupService;
import com.overnetcontact.dvvs.service.dto.SvcGroupDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcGroup}.
 */
@RestController
@RequestMapping("/api")
public class SvcGroupResource {

    private final Logger log = LoggerFactory.getLogger(SvcGroupResource.class);

    private static final String ENTITY_NAME = "svcGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcGroupService svcGroupService;

    private final SvcGroupRepository svcGroupRepository;

    public SvcGroupResource(SvcGroupService svcGroupService, SvcGroupRepository svcGroupRepository) {
        this.svcGroupService = svcGroupService;
        this.svcGroupRepository = svcGroupRepository;
    }

    /**
     * {@code POST  /svc-groups} : Create a new svcGroup.
     *
     * @param svcGroupDTO the svcGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcGroupDTO, or with status {@code 400 (Bad Request)} if the svcGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-groups")
    public ResponseEntity<SvcGroupDTO> createSvcGroup(@Valid @RequestBody SvcGroupDTO svcGroupDTO) throws URISyntaxException {
        log.debug("REST request to save SvcGroup : {}", svcGroupDTO);
        if (svcGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcGroupDTO result = svcGroupService.save(svcGroupDTO);
        return ResponseEntity
            .created(new URI("/api/svc-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-groups/:id} : Updates an existing svcGroup.
     *
     * @param id the id of the svcGroupDTO to save.
     * @param svcGroupDTO the svcGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcGroupDTO,
     * or with status {@code 400 (Bad Request)} if the svcGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-groups/{id}")
    public ResponseEntity<SvcGroupDTO> updateSvcGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcGroupDTO svcGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcGroup : {}, {}", id, svcGroupDTO);
        if (svcGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcGroupDTO result = svcGroupService.save(svcGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-groups/:id} : Partial updates given fields of an existing svcGroup, field will ignore if it is null
     *
     * @param id the id of the svcGroupDTO to save.
     * @param svcGroupDTO the svcGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcGroupDTO,
     * or with status {@code 400 (Bad Request)} if the svcGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcGroupDTO> partialUpdateSvcGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcGroupDTO svcGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcGroup partially : {}, {}", id, svcGroupDTO);
        if (svcGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcGroupDTO> result = svcGroupService.partialUpdate(svcGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-groups} : get all the svcGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcGroups in body.
     */
    @GetMapping("/svc-groups")
    public ResponseEntity<List<SvcGroupDTO>> getAllSvcGroups(Pageable pageable) {
        log.debug("REST request to get a page of SvcGroups");
        Page<SvcGroupDTO> page = svcGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-groups/:id} : get the "id" svcGroup.
     *
     * @param id the id of the svcGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-groups/{id}")
    public ResponseEntity<SvcGroupDTO> getSvcGroup(@PathVariable Long id) {
        log.debug("REST request to get SvcGroup : {}", id);
        Optional<SvcGroupDTO> svcGroupDTO = svcGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcGroupDTO);
    }

    /**
     * {@code DELETE  /svc-groups/:id} : delete the "id" svcGroup.
     *
     * @param id the id of the svcGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-groups/{id}")
    public ResponseEntity<Void> deleteSvcGroup(@PathVariable Long id) {
        log.debug("REST request to delete SvcGroup : {}", id);
        svcGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
