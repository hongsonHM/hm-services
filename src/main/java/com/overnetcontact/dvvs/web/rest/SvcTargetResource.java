package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcTargetRepository;
import com.overnetcontact.dvvs.service.SvcTargetQueryService;
import com.overnetcontact.dvvs.service.SvcTargetService;
import com.overnetcontact.dvvs.service.criteria.SvcTargetCriteria;
import com.overnetcontact.dvvs.service.dto.SvcTargetDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcTarget}.
 */
@RestController
@RequestMapping("/api")
public class SvcTargetResource {

    private final Logger log = LoggerFactory.getLogger(SvcTargetResource.class);

    private static final String ENTITY_NAME = "svcTarget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcTargetService svcTargetService;

    private final SvcTargetRepository svcTargetRepository;

    private final SvcTargetQueryService svcTargetQueryService;

    public SvcTargetResource(
        SvcTargetService svcTargetService,
        SvcTargetRepository svcTargetRepository,
        SvcTargetQueryService svcTargetQueryService
    ) {
        this.svcTargetService = svcTargetService;
        this.svcTargetRepository = svcTargetRepository;
        this.svcTargetQueryService = svcTargetQueryService;
    }

    /**
     * {@code POST  /svc-targets} : Create a new svcTarget.
     *
     * @param svcTargetDTO the svcTargetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcTargetDTO, or with status {@code 400 (Bad Request)} if the svcTarget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-targets")
    public ResponseEntity<SvcTargetDTO> createSvcTarget(@Valid @RequestBody SvcTargetDTO svcTargetDTO) throws URISyntaxException {
        log.debug("REST request to save SvcTarget : {}", svcTargetDTO);
        if (svcTargetDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcTarget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcTargetDTO result = svcTargetService.save(svcTargetDTO);
        return ResponseEntity
            .created(new URI("/api/svc-targets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-targets/:id} : Updates an existing svcTarget.
     *
     * @param id the id of the svcTargetDTO to save.
     * @param svcTargetDTO the svcTargetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcTargetDTO,
     * or with status {@code 400 (Bad Request)} if the svcTargetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcTargetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-targets/{id}")
    public ResponseEntity<SvcTargetDTO> updateSvcTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcTargetDTO svcTargetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcTarget : {}, {}", id, svcTargetDTO);
        if (svcTargetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcTargetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcTargetDTO result = svcTargetService.save(svcTargetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcTargetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-targets/:id} : Partial updates given fields of an existing svcTarget, field will ignore if it is null
     *
     * @param id the id of the svcTargetDTO to save.
     * @param svcTargetDTO the svcTargetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcTargetDTO,
     * or with status {@code 400 (Bad Request)} if the svcTargetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcTargetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcTargetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-targets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcTargetDTO> partialUpdateSvcTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcTargetDTO svcTargetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcTarget partially : {}, {}", id, svcTargetDTO);
        if (svcTargetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcTargetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcTargetDTO> result = svcTargetService.partialUpdate(svcTargetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcTargetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-targets} : get all the svcTargets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcTargets in body.
     */
    @GetMapping("/svc-targets")
    public ResponseEntity<List<SvcTargetDTO>> getAllSvcTargets(SvcTargetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcTargets by criteria: {}", criteria);
        Page<SvcTargetDTO> page = svcTargetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-targets/count} : count all the svcTargets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-targets/count")
    public ResponseEntity<Long> countSvcTargets(SvcTargetCriteria criteria) {
        log.debug("REST request to count SvcTargets by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcTargetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-targets/:id} : get the "id" svcTarget.
     *
     * @param id the id of the svcTargetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcTargetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-targets/{id}")
    public ResponseEntity<SvcTargetDTO> getSvcTarget(@PathVariable Long id) {
        log.debug("REST request to get SvcTarget : {}", id);
        Optional<SvcTargetDTO> svcTargetDTO = svcTargetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcTargetDTO);
    }

    /**
     * {@code DELETE  /svc-targets/:id} : delete the "id" svcTarget.
     *
     * @param id the id of the svcTargetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-targets/{id}")
    public ResponseEntity<Void> deleteSvcTarget(@PathVariable Long id) {
        log.debug("REST request to delete SvcTarget : {}", id);
        svcTargetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
