package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcScheduleUnitRecordRepository;
import com.overnetcontact.dvvs.service.SvcScheduleUnitRecordQueryService;
import com.overnetcontact.dvvs.service.SvcScheduleUnitRecordService;
import com.overnetcontact.dvvs.service.criteria.SvcScheduleUnitRecordCriteria;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitRecordDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcScheduleUnitRecord}.
 */
@RestController
@RequestMapping("/api")
public class SvcScheduleUnitRecordResource {

    private final Logger log = LoggerFactory.getLogger(SvcScheduleUnitRecordResource.class);

    private static final String ENTITY_NAME = "svcScheduleUnitRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcScheduleUnitRecordService svcScheduleUnitRecordService;

    private final SvcScheduleUnitRecordRepository svcScheduleUnitRecordRepository;

    private final SvcScheduleUnitRecordQueryService svcScheduleUnitRecordQueryService;

    public SvcScheduleUnitRecordResource(
        SvcScheduleUnitRecordService svcScheduleUnitRecordService,
        SvcScheduleUnitRecordRepository svcScheduleUnitRecordRepository,
        SvcScheduleUnitRecordQueryService svcScheduleUnitRecordQueryService
    ) {
        this.svcScheduleUnitRecordService = svcScheduleUnitRecordService;
        this.svcScheduleUnitRecordRepository = svcScheduleUnitRecordRepository;
        this.svcScheduleUnitRecordQueryService = svcScheduleUnitRecordQueryService;
    }

    /**
     * {@code POST  /svc-schedule-unit-records} : Create a new svcScheduleUnitRecord.
     *
     * @param svcScheduleUnitRecordDTO the svcScheduleUnitRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcScheduleUnitRecordDTO, or with status {@code 400 (Bad Request)} if the svcScheduleUnitRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-schedule-unit-records")
    public ResponseEntity<SvcScheduleUnitRecordDTO> createSvcScheduleUnitRecord(
        @Valid @RequestBody SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SvcScheduleUnitRecord : {}", svcScheduleUnitRecordDTO);
        if (svcScheduleUnitRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcScheduleUnitRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcScheduleUnitRecordDTO result = svcScheduleUnitRecordService.save(svcScheduleUnitRecordDTO);
        return ResponseEntity
            .created(new URI("/api/svc-schedule-unit-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-schedule-unit-records/:id} : Updates an existing svcScheduleUnitRecord.
     *
     * @param id the id of the svcScheduleUnitRecordDTO to save.
     * @param svcScheduleUnitRecordDTO the svcScheduleUnitRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcScheduleUnitRecordDTO,
     * or with status {@code 400 (Bad Request)} if the svcScheduleUnitRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcScheduleUnitRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-schedule-unit-records/{id}")
    public ResponseEntity<SvcScheduleUnitRecordDTO> updateSvcScheduleUnitRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcScheduleUnitRecord : {}, {}", id, svcScheduleUnitRecordDTO);
        if (svcScheduleUnitRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcScheduleUnitRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcScheduleUnitRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcScheduleUnitRecordDTO result = svcScheduleUnitRecordService.save(svcScheduleUnitRecordDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcScheduleUnitRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-schedule-unit-records/:id} : Partial updates given fields of an existing svcScheduleUnitRecord, field will ignore if it is null
     *
     * @param id the id of the svcScheduleUnitRecordDTO to save.
     * @param svcScheduleUnitRecordDTO the svcScheduleUnitRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcScheduleUnitRecordDTO,
     * or with status {@code 400 (Bad Request)} if the svcScheduleUnitRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcScheduleUnitRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcScheduleUnitRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-schedule-unit-records/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcScheduleUnitRecordDTO> partialUpdateSvcScheduleUnitRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcScheduleUnitRecord partially : {}, {}", id, svcScheduleUnitRecordDTO);
        if (svcScheduleUnitRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcScheduleUnitRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcScheduleUnitRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcScheduleUnitRecordDTO> result = svcScheduleUnitRecordService.partialUpdate(svcScheduleUnitRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcScheduleUnitRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-schedule-unit-records} : get all the svcScheduleUnitRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcScheduleUnitRecords in body.
     */
    @GetMapping("/svc-schedule-unit-records")
    public ResponseEntity<List<SvcScheduleUnitRecordDTO>> getAllSvcScheduleUnitRecords(
        SvcScheduleUnitRecordCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get SvcScheduleUnitRecords by criteria: {}", criteria);
        Page<SvcScheduleUnitRecordDTO> page = svcScheduleUnitRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-schedule-unit-records/count} : count all the svcScheduleUnitRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-schedule-unit-records/count")
    public ResponseEntity<Long> countSvcScheduleUnitRecords(SvcScheduleUnitRecordCriteria criteria) {
        log.debug("REST request to count SvcScheduleUnitRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcScheduleUnitRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-schedule-unit-records/:id} : get the "id" svcScheduleUnitRecord.
     *
     * @param id the id of the svcScheduleUnitRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcScheduleUnitRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-schedule-unit-records/{id}")
    public ResponseEntity<SvcScheduleUnitRecordDTO> getSvcScheduleUnitRecord(@PathVariable Long id) {
        log.debug("REST request to get SvcScheduleUnitRecord : {}", id);
        Optional<SvcScheduleUnitRecordDTO> svcScheduleUnitRecordDTO = svcScheduleUnitRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcScheduleUnitRecordDTO);
    }

    /**
     * {@code DELETE  /svc-schedule-unit-records/:id} : delete the "id" svcScheduleUnitRecord.
     *
     * @param id the id of the svcScheduleUnitRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-schedule-unit-records/{id}")
    public ResponseEntity<Void> deleteSvcScheduleUnitRecord(@PathVariable Long id) {
        log.debug("REST request to delete SvcScheduleUnitRecord : {}", id);
        svcScheduleUnitRecordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
