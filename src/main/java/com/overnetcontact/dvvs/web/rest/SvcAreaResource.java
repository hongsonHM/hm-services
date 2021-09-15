package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcAreaRepository;
import com.overnetcontact.dvvs.service.SvcAreaQueryService;
import com.overnetcontact.dvvs.service.SvcAreaService;
import com.overnetcontact.dvvs.service.criteria.SvcAreaCriteria;
import com.overnetcontact.dvvs.service.dto.SvcAreaDTO;
import com.overnetcontact.dvvs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcArea}.
 */
@RestController
@RequestMapping("/api")
public class SvcAreaResource {

    private final Logger log = LoggerFactory.getLogger(SvcAreaResource.class);

    private static final String ENTITY_NAME = "svcArea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcAreaService svcAreaService;

    private final SvcAreaRepository svcAreaRepository;

    private final SvcAreaQueryService svcAreaQueryService;

    public SvcAreaResource(SvcAreaService svcAreaService, SvcAreaRepository svcAreaRepository, SvcAreaQueryService svcAreaQueryService) {
        this.svcAreaService = svcAreaService;
        this.svcAreaRepository = svcAreaRepository;
        this.svcAreaQueryService = svcAreaQueryService;
    }

    /**
     * {@code POST  /svc-areas} : Create a new svcArea.
     *
     * @param svcAreaDTO the svcAreaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcAreaDTO, or with status {@code 400 (Bad Request)} if the svcArea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-areas")
    public ResponseEntity<SvcAreaDTO> createSvcArea(@RequestBody SvcAreaDTO svcAreaDTO) throws URISyntaxException {
        log.debug("REST request to save SvcArea : {}", svcAreaDTO);
        if (svcAreaDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcArea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcAreaDTO result = svcAreaService.save(svcAreaDTO);
        return ResponseEntity
            .created(new URI("/api/svc-areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-areas/:id} : Updates an existing svcArea.
     *
     * @param id the id of the svcAreaDTO to save.
     * @param svcAreaDTO the svcAreaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcAreaDTO,
     * or with status {@code 400 (Bad Request)} if the svcAreaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcAreaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-areas/{id}")
    public ResponseEntity<SvcAreaDTO> updateSvcArea(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SvcAreaDTO svcAreaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcArea : {}, {}", id, svcAreaDTO);
        if (svcAreaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcAreaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcAreaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcAreaDTO result = svcAreaService.save(svcAreaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcAreaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-areas/:id} : Partial updates given fields of an existing svcArea, field will ignore if it is null
     *
     * @param id the id of the svcAreaDTO to save.
     * @param svcAreaDTO the svcAreaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcAreaDTO,
     * or with status {@code 400 (Bad Request)} if the svcAreaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcAreaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcAreaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-areas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcAreaDTO> partialUpdateSvcArea(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SvcAreaDTO svcAreaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcArea partially : {}, {}", id, svcAreaDTO);
        if (svcAreaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcAreaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcAreaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcAreaDTO> result = svcAreaService.partialUpdate(svcAreaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcAreaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-areas} : get all the svcAreas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcAreas in body.
     */
    @GetMapping("/svc-areas")
    public ResponseEntity<List<SvcAreaDTO>> getAllSvcAreas(SvcAreaCriteria criteria) {
        log.debug("REST request to get SvcAreas by criteria: {}", criteria);
        List<SvcAreaDTO> entityList = svcAreaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /svc-areas/count} : count all the svcAreas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-areas/count")
    public ResponseEntity<Long> countSvcAreas(SvcAreaCriteria criteria) {
        log.debug("REST request to count SvcAreas by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcAreaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-areas/:id} : get the "id" svcArea.
     *
     * @param id the id of the svcAreaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcAreaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-areas/{id}")
    public ResponseEntity<SvcAreaDTO> getSvcArea(@PathVariable Long id) {
        log.debug("REST request to get SvcArea : {}", id);
        Optional<SvcAreaDTO> svcAreaDTO = svcAreaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcAreaDTO);
    }

    /**
     * {@code DELETE  /svc-areas/:id} : delete the "id" svcArea.
     *
     * @param id the id of the svcAreaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-areas/{id}")
    public ResponseEntity<Void> deleteSvcArea(@PathVariable Long id) {
        log.debug("REST request to delete SvcArea : {}", id);
        svcAreaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
