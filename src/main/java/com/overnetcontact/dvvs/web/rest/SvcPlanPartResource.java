package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcPlanPartRepository;
import com.overnetcontact.dvvs.service.SvcPlanPartQueryService;
import com.overnetcontact.dvvs.service.SvcPlanPartService;
import com.overnetcontact.dvvs.service.criteria.SvcPlanPartCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanPartDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcPlanPart}.
 */
@RestController
@RequestMapping("/api")
public class SvcPlanPartResource {

    private final Logger log = LoggerFactory.getLogger(SvcPlanPartResource.class);

    private static final String ENTITY_NAME = "svcPlanPart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcPlanPartService svcPlanPartService;

    private final SvcPlanPartRepository svcPlanPartRepository;

    private final SvcPlanPartQueryService svcPlanPartQueryService;

    public SvcPlanPartResource(
        SvcPlanPartService svcPlanPartService,
        SvcPlanPartRepository svcPlanPartRepository,
        SvcPlanPartQueryService svcPlanPartQueryService
    ) {
        this.svcPlanPartService = svcPlanPartService;
        this.svcPlanPartRepository = svcPlanPartRepository;
        this.svcPlanPartQueryService = svcPlanPartQueryService;
    }

    /**
     * {@code POST  /svc-plan-parts} : Create a new svcPlanPart.
     *
     * @param svcPlanPartDTO the svcPlanPartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcPlanPartDTO, or with status {@code 400 (Bad Request)} if the svcPlanPart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-plan-parts")
    public ResponseEntity<SvcPlanPartDTO> createSvcPlanPart(@Valid @RequestBody SvcPlanPartDTO svcPlanPartDTO) throws URISyntaxException {
        log.debug("REST request to save SvcPlanPart : {}", svcPlanPartDTO);
        if (svcPlanPartDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcPlanPart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcPlanPartDTO result = svcPlanPartService.save(svcPlanPartDTO);
        return ResponseEntity
            .created(new URI("/api/svc-plan-parts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-plan-parts/:id} : Updates an existing svcPlanPart.
     *
     * @param id the id of the svcPlanPartDTO to save.
     * @param svcPlanPartDTO the svcPlanPartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcPlanPartDTO,
     * or with status {@code 400 (Bad Request)} if the svcPlanPartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcPlanPartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-plan-parts/{id}")
    public ResponseEntity<SvcPlanPartDTO> updateSvcPlanPart(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcPlanPartDTO svcPlanPartDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcPlanPart : {}, {}", id, svcPlanPartDTO);
        if (svcPlanPartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcPlanPartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcPlanPartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcPlanPartDTO result = svcPlanPartService.save(svcPlanPartDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcPlanPartDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-plan-parts/:id} : Partial updates given fields of an existing svcPlanPart, field will ignore if it is null
     *
     * @param id the id of the svcPlanPartDTO to save.
     * @param svcPlanPartDTO the svcPlanPartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcPlanPartDTO,
     * or with status {@code 400 (Bad Request)} if the svcPlanPartDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcPlanPartDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcPlanPartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-plan-parts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcPlanPartDTO> partialUpdateSvcPlanPart(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcPlanPartDTO svcPlanPartDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcPlanPart partially : {}, {}", id, svcPlanPartDTO);
        if (svcPlanPartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcPlanPartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcPlanPartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcPlanPartDTO> result = svcPlanPartService.partialUpdate(svcPlanPartDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcPlanPartDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-plan-parts} : get all the svcPlanParts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcPlanParts in body.
     */
    @GetMapping("/svc-plan-parts")
    public ResponseEntity<List<SvcPlanPartDTO>> getAllSvcPlanParts(SvcPlanPartCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcPlanParts by criteria: {}", criteria);
        Page<SvcPlanPartDTO> page = svcPlanPartQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-plan-parts/count} : count all the svcPlanParts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-plan-parts/count")
    public ResponseEntity<Long> countSvcPlanParts(SvcPlanPartCriteria criteria) {
        log.debug("REST request to count SvcPlanParts by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcPlanPartQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-plan-parts/:id} : get the "id" svcPlanPart.
     *
     * @param id the id of the svcPlanPartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcPlanPartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-plan-parts/{id}")
    public ResponseEntity<SvcPlanPartDTO> getSvcPlanPart(@PathVariable Long id) {
        log.debug("REST request to get SvcPlanPart : {}", id);
        Optional<SvcPlanPartDTO> svcPlanPartDTO = svcPlanPartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcPlanPartDTO);
    }

    /**
     * {@code DELETE  /svc-plan-parts/:id} : delete the "id" svcPlanPart.
     *
     * @param id the id of the svcPlanPartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-plan-parts/{id}")
    public ResponseEntity<Void> deleteSvcPlanPart(@PathVariable Long id) {
        log.debug("REST request to delete SvcPlanPart : {}", id);
        svcPlanPartService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
