package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcUnitRepository;
import com.overnetcontact.dvvs.service.SvcUnitService;
import com.overnetcontact.dvvs.service.dto.SvcUnitDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcUnit}.
 */
@RestController
@RequestMapping("/api")
public class SvcUnitResource {

    private final Logger log = LoggerFactory.getLogger(SvcUnitResource.class);

    private static final String ENTITY_NAME = "svcUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcUnitService svcUnitService;

    private final SvcUnitRepository svcUnitRepository;

    public SvcUnitResource(SvcUnitService svcUnitService, SvcUnitRepository svcUnitRepository) {
        this.svcUnitService = svcUnitService;
        this.svcUnitRepository = svcUnitRepository;
    }

    /**
     * {@code POST  /svc-units} : Create a new svcUnit.
     *
     * @param svcUnitDTO the svcUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcUnitDTO, or with status {@code 400 (Bad Request)} if the svcUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-units")
    public ResponseEntity<SvcUnitDTO> createSvcUnit(@Valid @RequestBody SvcUnitDTO svcUnitDTO) throws URISyntaxException {
        log.debug("REST request to save SvcUnit : {}", svcUnitDTO);
        if (svcUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcUnitDTO result = svcUnitService.save(svcUnitDTO);
        return ResponseEntity
            .created(new URI("/api/svc-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-units/:id} : Updates an existing svcUnit.
     *
     * @param id the id of the svcUnitDTO to save.
     * @param svcUnitDTO the svcUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcUnitDTO,
     * or with status {@code 400 (Bad Request)} if the svcUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-units/{id}")
    public ResponseEntity<SvcUnitDTO> updateSvcUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcUnitDTO svcUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcUnit : {}, {}", id, svcUnitDTO);
        if (svcUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcUnitDTO result = svcUnitService.save(svcUnitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-units/:id} : Partial updates given fields of an existing svcUnit, field will ignore if it is null
     *
     * @param id the id of the svcUnitDTO to save.
     * @param svcUnitDTO the svcUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcUnitDTO,
     * or with status {@code 400 (Bad Request)} if the svcUnitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcUnitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcUnitDTO> partialUpdateSvcUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcUnitDTO svcUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcUnit partially : {}, {}", id, svcUnitDTO);
        if (svcUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcUnitDTO> result = svcUnitService.partialUpdate(svcUnitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcUnitDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-units} : get all the svcUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcUnits in body.
     */
    @GetMapping("/svc-units")
    public ResponseEntity<List<SvcUnitDTO>> getAllSvcUnits(Pageable pageable) {
        log.debug("REST request to get a page of SvcUnits");
        Page<SvcUnitDTO> page = svcUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-units/:id} : get the "id" svcUnit.
     *
     * @param id the id of the svcUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-units/{id}")
    public ResponseEntity<SvcUnitDTO> getSvcUnit(@PathVariable Long id) {
        log.debug("REST request to get SvcUnit : {}", id);
        Optional<SvcUnitDTO> svcUnitDTO = svcUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcUnitDTO);
    }

    /**
     * {@code DELETE  /svc-units/:id} : delete the "id" svcUnit.
     *
     * @param id the id of the svcUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-units/{id}")
    public ResponseEntity<Void> deleteSvcUnit(@PathVariable Long id) {
        log.debug("REST request to delete SvcUnit : {}", id);
        svcUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
