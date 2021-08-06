package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcSchedulePlanRepository;
import com.overnetcontact.dvvs.service.SvcSchedulePlanService;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcSchedulePlan}.
 */
@RestController
@RequestMapping("/api")
public class SvcSchedulePlanResource {

    private final Logger log = LoggerFactory.getLogger(SvcSchedulePlanResource.class);

    private static final String ENTITY_NAME = "svcSchedulePlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcSchedulePlanService svcSchedulePlanService;

    private final SvcSchedulePlanRepository svcSchedulePlanRepository;

    public SvcSchedulePlanResource(SvcSchedulePlanService svcSchedulePlanService, SvcSchedulePlanRepository svcSchedulePlanRepository) {
        this.svcSchedulePlanService = svcSchedulePlanService;
        this.svcSchedulePlanRepository = svcSchedulePlanRepository;
    }

    /**
     * {@code POST  /svc-schedule-plans} : Create a new svcSchedulePlan.
     *
     * @param svcSchedulePlanDTO the svcSchedulePlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcSchedulePlanDTO, or with status {@code 400 (Bad Request)} if the svcSchedulePlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-schedule-plans")
    public ResponseEntity<SvcSchedulePlanDTO> createSvcSchedulePlan(@Valid @RequestBody SvcSchedulePlanDTO svcSchedulePlanDTO)
        throws URISyntaxException {
        log.debug("REST request to save SvcSchedulePlan : {}", svcSchedulePlanDTO);
        if (svcSchedulePlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcSchedulePlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcSchedulePlanDTO result = svcSchedulePlanService.save(svcSchedulePlanDTO);
        return ResponseEntity
            .created(new URI("/api/svc-schedule-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-schedule-plans/:id} : Updates an existing svcSchedulePlan.
     *
     * @param id the id of the svcSchedulePlanDTO to save.
     * @param svcSchedulePlanDTO the svcSchedulePlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcSchedulePlanDTO,
     * or with status {@code 400 (Bad Request)} if the svcSchedulePlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcSchedulePlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-schedule-plans/{id}")
    public ResponseEntity<SvcSchedulePlanDTO> updateSvcSchedulePlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcSchedulePlanDTO svcSchedulePlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcSchedulePlan : {}, {}", id, svcSchedulePlanDTO);
        if (svcSchedulePlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcSchedulePlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcSchedulePlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcSchedulePlanDTO result = svcSchedulePlanService.save(svcSchedulePlanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcSchedulePlanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-schedule-plans/:id} : Partial updates given fields of an existing svcSchedulePlan, field will ignore if it is null
     *
     * @param id the id of the svcSchedulePlanDTO to save.
     * @param svcSchedulePlanDTO the svcSchedulePlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcSchedulePlanDTO,
     * or with status {@code 400 (Bad Request)} if the svcSchedulePlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcSchedulePlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcSchedulePlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-schedule-plans/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcSchedulePlanDTO> partialUpdateSvcSchedulePlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcSchedulePlanDTO svcSchedulePlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcSchedulePlan partially : {}, {}", id, svcSchedulePlanDTO);
        if (svcSchedulePlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcSchedulePlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcSchedulePlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcSchedulePlanDTO> result = svcSchedulePlanService.partialUpdate(svcSchedulePlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcSchedulePlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-schedule-plans} : get all the svcSchedulePlans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcSchedulePlans in body.
     */
    @GetMapping("/svc-schedule-plans")
    public ResponseEntity<List<SvcSchedulePlanDTO>> getAllSvcSchedulePlans(Pageable pageable) {
        log.debug("REST request to get a page of SvcSchedulePlans");
        Page<SvcSchedulePlanDTO> page = svcSchedulePlanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-schedule-plans/:id} : get the "id" svcSchedulePlan.
     *
     * @param id the id of the svcSchedulePlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcSchedulePlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-schedule-plans/{id}")
    public ResponseEntity<SvcSchedulePlanDTO> getSvcSchedulePlan(@PathVariable Long id) {
        log.debug("REST request to get SvcSchedulePlan : {}", id);
        Optional<SvcSchedulePlanDTO> svcSchedulePlanDTO = svcSchedulePlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcSchedulePlanDTO);
    }

    /**
     * {@code DELETE  /svc-schedule-plans/:id} : delete the "id" svcSchedulePlan.
     *
     * @param id the id of the svcSchedulePlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-schedule-plans/{id}")
    public ResponseEntity<Void> deleteSvcSchedulePlan(@PathVariable Long id) {
        log.debug("REST request to delete SvcSchedulePlan : {}", id);
        svcSchedulePlanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
