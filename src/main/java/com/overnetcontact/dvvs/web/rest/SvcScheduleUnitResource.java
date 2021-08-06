package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcScheduleUnitRepository;
import com.overnetcontact.dvvs.service.SvcScheduleUnitService;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcScheduleUnit}.
 */
@RestController
@RequestMapping("/api")
public class SvcScheduleUnitResource {

    private final Logger log = LoggerFactory.getLogger(SvcScheduleUnitResource.class);

    private static final String ENTITY_NAME = "svcScheduleUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcScheduleUnitService svcScheduleUnitService;

    private final SvcScheduleUnitRepository svcScheduleUnitRepository;

    public SvcScheduleUnitResource(SvcScheduleUnitService svcScheduleUnitService, SvcScheduleUnitRepository svcScheduleUnitRepository) {
        this.svcScheduleUnitService = svcScheduleUnitService;
        this.svcScheduleUnitRepository = svcScheduleUnitRepository;
    }

    /**
     * {@code POST  /svc-schedule-units} : Create a new svcScheduleUnit.
     *
     * @param svcScheduleUnitDTO the svcScheduleUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcScheduleUnitDTO, or with status {@code 400 (Bad Request)} if the svcScheduleUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-schedule-units")
    public ResponseEntity<SvcScheduleUnitDTO> createSvcScheduleUnit(@Valid @RequestBody SvcScheduleUnitDTO svcScheduleUnitDTO)
        throws URISyntaxException {
        log.debug("REST request to save SvcScheduleUnit : {}", svcScheduleUnitDTO);
        if (svcScheduleUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcScheduleUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcScheduleUnitDTO result = svcScheduleUnitService.save(svcScheduleUnitDTO);
        return ResponseEntity
            .created(new URI("/api/svc-schedule-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-schedule-units/:id} : Updates an existing svcScheduleUnit.
     *
     * @param id the id of the svcScheduleUnitDTO to save.
     * @param svcScheduleUnitDTO the svcScheduleUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcScheduleUnitDTO,
     * or with status {@code 400 (Bad Request)} if the svcScheduleUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcScheduleUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-schedule-units/{id}")
    public ResponseEntity<SvcScheduleUnitDTO> updateSvcScheduleUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcScheduleUnitDTO svcScheduleUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcScheduleUnit : {}, {}", id, svcScheduleUnitDTO);
        if (svcScheduleUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcScheduleUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcScheduleUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcScheduleUnitDTO result = svcScheduleUnitService.save(svcScheduleUnitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcScheduleUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-schedule-units/:id} : Partial updates given fields of an existing svcScheduleUnit, field will ignore if it is null
     *
     * @param id the id of the svcScheduleUnitDTO to save.
     * @param svcScheduleUnitDTO the svcScheduleUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcScheduleUnitDTO,
     * or with status {@code 400 (Bad Request)} if the svcScheduleUnitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcScheduleUnitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcScheduleUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-schedule-units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcScheduleUnitDTO> partialUpdateSvcScheduleUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcScheduleUnitDTO svcScheduleUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcScheduleUnit partially : {}, {}", id, svcScheduleUnitDTO);
        if (svcScheduleUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcScheduleUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcScheduleUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcScheduleUnitDTO> result = svcScheduleUnitService.partialUpdate(svcScheduleUnitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcScheduleUnitDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-schedule-units} : get all the svcScheduleUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcScheduleUnits in body.
     */
    @GetMapping("/svc-schedule-units")
    public ResponseEntity<List<SvcScheduleUnitDTO>> getAllSvcScheduleUnits(Pageable pageable) {
        log.debug("REST request to get a page of SvcScheduleUnits");
        Page<SvcScheduleUnitDTO> page = svcScheduleUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-schedule-units/:id} : get the "id" svcScheduleUnit.
     *
     * @param id the id of the svcScheduleUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcScheduleUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-schedule-units/{id}")
    public ResponseEntity<SvcScheduleUnitDTO> getSvcScheduleUnit(@PathVariable Long id) {
        log.debug("REST request to get SvcScheduleUnit : {}", id);
        Optional<SvcScheduleUnitDTO> svcScheduleUnitDTO = svcScheduleUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcScheduleUnitDTO);
    }

    /**
     * {@code DELETE  /svc-schedule-units/:id} : delete the "id" svcScheduleUnit.
     *
     * @param id the id of the svcScheduleUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-schedule-units/{id}")
    public ResponseEntity<Void> deleteSvcScheduleUnit(@PathVariable Long id) {
        log.debug("REST request to delete SvcScheduleUnit : {}", id);
        svcScheduleUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
