package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcSchedulePlanRecordRepository;
import com.overnetcontact.dvvs.service.SvcSchedulePlanRecordQueryService;
import com.overnetcontact.dvvs.service.SvcSchedulePlanRecordService;
import com.overnetcontact.dvvs.service.criteria.SvcSchedulePlanRecordCriteria;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanRecordDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcSchedulePlanRecord}.
 */
@RestController
@RequestMapping("/api")
public class SvcSchedulePlanRecordResource {

    private final Logger log = LoggerFactory.getLogger(SvcSchedulePlanRecordResource.class);

    private static final String ENTITY_NAME = "svcSchedulePlanRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcSchedulePlanRecordService svcSchedulePlanRecordService;

    private final SvcSchedulePlanRecordRepository svcSchedulePlanRecordRepository;

    private final SvcSchedulePlanRecordQueryService svcSchedulePlanRecordQueryService;

    public SvcSchedulePlanRecordResource(
        SvcSchedulePlanRecordService svcSchedulePlanRecordService,
        SvcSchedulePlanRecordRepository svcSchedulePlanRecordRepository,
        SvcSchedulePlanRecordQueryService svcSchedulePlanRecordQueryService
    ) {
        this.svcSchedulePlanRecordService = svcSchedulePlanRecordService;
        this.svcSchedulePlanRecordRepository = svcSchedulePlanRecordRepository;
        this.svcSchedulePlanRecordQueryService = svcSchedulePlanRecordQueryService;
    }

    /**
     * {@code POST  /svc-schedule-plan-records} : Create a new svcSchedulePlanRecord.
     *
     * @param svcSchedulePlanRecordDTO the svcSchedulePlanRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcSchedulePlanRecordDTO, or with status {@code 400 (Bad Request)} if the svcSchedulePlanRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-schedule-plan-records")
    public ResponseEntity<SvcSchedulePlanRecordDTO> createSvcSchedulePlanRecord(
        @Valid @RequestBody SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SvcSchedulePlanRecord : {}", svcSchedulePlanRecordDTO);
        if (svcSchedulePlanRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcSchedulePlanRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcSchedulePlanRecordDTO result = svcSchedulePlanRecordService.save(svcSchedulePlanRecordDTO);
        return ResponseEntity
            .created(new URI("/api/svc-schedule-plan-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-schedule-plan-records/:id} : Updates an existing svcSchedulePlanRecord.
     *
     * @param id the id of the svcSchedulePlanRecordDTO to save.
     * @param svcSchedulePlanRecordDTO the svcSchedulePlanRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcSchedulePlanRecordDTO,
     * or with status {@code 400 (Bad Request)} if the svcSchedulePlanRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcSchedulePlanRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-schedule-plan-records/{id}")
    public ResponseEntity<SvcSchedulePlanRecordDTO> updateSvcSchedulePlanRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcSchedulePlanRecord : {}, {}", id, svcSchedulePlanRecordDTO);
        if (svcSchedulePlanRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcSchedulePlanRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcSchedulePlanRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcSchedulePlanRecordDTO result = svcSchedulePlanRecordService.save(svcSchedulePlanRecordDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcSchedulePlanRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-schedule-plan-records/:id} : Partial updates given fields of an existing svcSchedulePlanRecord, field will ignore if it is null
     *
     * @param id the id of the svcSchedulePlanRecordDTO to save.
     * @param svcSchedulePlanRecordDTO the svcSchedulePlanRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcSchedulePlanRecordDTO,
     * or with status {@code 400 (Bad Request)} if the svcSchedulePlanRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcSchedulePlanRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcSchedulePlanRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-schedule-plan-records/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcSchedulePlanRecordDTO> partialUpdateSvcSchedulePlanRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcSchedulePlanRecord partially : {}, {}", id, svcSchedulePlanRecordDTO);
        if (svcSchedulePlanRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcSchedulePlanRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcSchedulePlanRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcSchedulePlanRecordDTO> result = svcSchedulePlanRecordService.partialUpdate(svcSchedulePlanRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcSchedulePlanRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-schedule-plan-records} : get all the svcSchedulePlanRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcSchedulePlanRecords in body.
     */
    @GetMapping("/svc-schedule-plan-records")
    public ResponseEntity<List<SvcSchedulePlanRecordDTO>> getAllSvcSchedulePlanRecords(
        SvcSchedulePlanRecordCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get SvcSchedulePlanRecords by criteria: {}", criteria);
        Page<SvcSchedulePlanRecordDTO> page = svcSchedulePlanRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-schedule-plan-records/count} : count all the svcSchedulePlanRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-schedule-plan-records/count")
    public ResponseEntity<Long> countSvcSchedulePlanRecords(SvcSchedulePlanRecordCriteria criteria) {
        log.debug("REST request to count SvcSchedulePlanRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcSchedulePlanRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-schedule-plan-records/:id} : get the "id" svcSchedulePlanRecord.
     *
     * @param id the id of the svcSchedulePlanRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcSchedulePlanRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-schedule-plan-records/{id}")
    public ResponseEntity<SvcSchedulePlanRecordDTO> getSvcSchedulePlanRecord(@PathVariable Long id) {
        log.debug("REST request to get SvcSchedulePlanRecord : {}", id);
        Optional<SvcSchedulePlanRecordDTO> svcSchedulePlanRecordDTO = svcSchedulePlanRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcSchedulePlanRecordDTO);
    }

    /**
     * {@code DELETE  /svc-schedule-plan-records/:id} : delete the "id" svcSchedulePlanRecord.
     *
     * @param id the id of the svcSchedulePlanRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-schedule-plan-records/{id}")
    public ResponseEntity<Void> deleteSvcSchedulePlanRecord(@PathVariable Long id) {
        log.debug("REST request to delete SvcSchedulePlanRecord : {}", id);
        svcSchedulePlanRecordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
