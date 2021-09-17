package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcPlanUnitRepository;
import com.overnetcontact.dvvs.service.SvcPlanUnitQueryService;
import com.overnetcontact.dvvs.service.SvcPlanUnitService;
import com.overnetcontact.dvvs.service.criteria.SvcPlanUnitCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanUnitDTO;
import com.overnetcontact.dvvs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcPlanUnit}.
 */
@RestController
@RequestMapping("/api")
public class SvcPlanUnitResource {

    private final Logger log = LoggerFactory.getLogger(SvcPlanUnitResource.class);

    private static final String ENTITY_NAME = "svcPlanUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcPlanUnitService svcPlanUnitService;

    private final SvcPlanUnitRepository svcPlanUnitRepository;

    private final SvcPlanUnitQueryService svcPlanUnitQueryService;

    public SvcPlanUnitResource(
        SvcPlanUnitService svcPlanUnitService,
        SvcPlanUnitRepository svcPlanUnitRepository,
        SvcPlanUnitQueryService svcPlanUnitQueryService
    ) {
        this.svcPlanUnitService = svcPlanUnitService;
        this.svcPlanUnitRepository = svcPlanUnitRepository;
        this.svcPlanUnitQueryService = svcPlanUnitQueryService;
    }

    /**
     * {@code POST  /svc-plan-units} : Create a new svcPlanUnit.
     *
     * @param svcPlanUnitDTO the svcPlanUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcPlanUnitDTO, or with status {@code 400 (Bad Request)} if the svcPlanUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-plan-units")
    public ResponseEntity<SvcPlanUnitDTO> createSvcPlanUnit(@RequestBody SvcPlanUnitDTO svcPlanUnitDTO) throws URISyntaxException {
        log.debug("REST request to save SvcPlanUnit : {}", svcPlanUnitDTO);
        if (svcPlanUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcPlanUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcPlanUnitDTO result = svcPlanUnitService.save(svcPlanUnitDTO);
        return ResponseEntity
            .created(new URI("/api/svc-plan-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-plan-units/:id} : Updates an existing svcPlanUnit.
     *
     * @param id the id of the svcPlanUnitDTO to save.
     * @param svcPlanUnitDTO the svcPlanUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcPlanUnitDTO,
     * or with status {@code 400 (Bad Request)} if the svcPlanUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcPlanUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-plan-units/{id}")
    public ResponseEntity<SvcPlanUnitDTO> updateSvcPlanUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SvcPlanUnitDTO svcPlanUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcPlanUnit : {}, {}", id, svcPlanUnitDTO);
        if (svcPlanUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcPlanUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcPlanUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcPlanUnitDTO result = svcPlanUnitService.save(svcPlanUnitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcPlanUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-plan-units/:id} : Partial updates given fields of an existing svcPlanUnit, field will ignore if it is null
     *
     * @param id the id of the svcPlanUnitDTO to save.
     * @param svcPlanUnitDTO the svcPlanUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcPlanUnitDTO,
     * or with status {@code 400 (Bad Request)} if the svcPlanUnitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcPlanUnitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcPlanUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-plan-units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcPlanUnitDTO> partialUpdateSvcPlanUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SvcPlanUnitDTO svcPlanUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcPlanUnit partially : {}, {}", id, svcPlanUnitDTO);
        if (svcPlanUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcPlanUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcPlanUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcPlanUnitDTO> result = svcPlanUnitService.partialUpdate(svcPlanUnitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcPlanUnitDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-plan-units} : get all the svcPlanUnits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcPlanUnits in body.
     */
    @GetMapping("/svc-plan-units")
    public ResponseEntity<List<SvcPlanUnitDTO>> getAllSvcPlanUnits(SvcPlanUnitCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcPlanUnits by criteria: {}", criteria);
        Page<SvcPlanUnitDTO> page = svcPlanUnitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-plan-units/count} : count all the svcPlanUnits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-plan-units/count")
    public ResponseEntity<Long> countSvcPlanUnits(SvcPlanUnitCriteria criteria) {
        log.debug("REST request to count SvcPlanUnits by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcPlanUnitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-plan-units/:id} : get the "id" svcPlanUnit.
     *
     * @param id the id of the svcPlanUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcPlanUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-plan-units/{id}")
    public ResponseEntity<SvcPlanUnitDTO> getSvcPlanUnit(@PathVariable Long id) {
        log.debug("REST request to get SvcPlanUnit : {}", id);
        Optional<SvcPlanUnitDTO> svcPlanUnitDTO = svcPlanUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcPlanUnitDTO);
    }

    /**
     * {@code DELETE  /svc-plan-units/:id} : delete the "id" svcPlanUnit.
     *
     * @param id the id of the svcPlanUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-plan-units/{id}")
    public ResponseEntity<Void> deleteSvcPlanUnit(@PathVariable Long id) {
        log.debug("REST request to delete SvcPlanUnit : {}", id);
        svcPlanUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
