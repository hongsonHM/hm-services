package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcLaborRepository;
import com.overnetcontact.dvvs.service.SvcLaborQueryService;
import com.overnetcontact.dvvs.service.SvcLaborService;
import com.overnetcontact.dvvs.service.criteria.SvcLaborCriteria;
import com.overnetcontact.dvvs.service.dto.SvcLaborDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcLabor}.
 */
@RestController
@RequestMapping("/api")
public class SvcLaborResource {

    private final Logger log = LoggerFactory.getLogger(SvcLaborResource.class);

    private static final String ENTITY_NAME = "svcLabor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcLaborService svcLaborService;

    private final SvcLaborRepository svcLaborRepository;

    private final SvcLaborQueryService svcLaborQueryService;

    public SvcLaborResource(
        SvcLaborService svcLaborService,
        SvcLaborRepository svcLaborRepository,
        SvcLaborQueryService svcLaborQueryService
    ) {
        this.svcLaborService = svcLaborService;
        this.svcLaborRepository = svcLaborRepository;
        this.svcLaborQueryService = svcLaborQueryService;
    }

    /**
     * {@code POST  /svc-labors} : Create a new svcLabor.
     *
     * @param svcLaborDTO the svcLaborDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcLaborDTO, or with status {@code 400 (Bad Request)} if the svcLabor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-labors")
    public ResponseEntity<SvcLaborDTO> createSvcLabor(@Valid @RequestBody SvcLaborDTO svcLaborDTO) throws URISyntaxException {
        log.debug("REST request to save SvcLabor : {}", svcLaborDTO);
        if (svcLaborDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcLabor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcLaborDTO result = svcLaborService.save(svcLaborDTO);
        return ResponseEntity
            .created(new URI("/api/svc-labors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-labors/:id} : Updates an existing svcLabor.
     *
     * @param id the id of the svcLaborDTO to save.
     * @param svcLaborDTO the svcLaborDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcLaborDTO,
     * or with status {@code 400 (Bad Request)} if the svcLaborDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcLaborDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-labors/{id}")
    public ResponseEntity<SvcLaborDTO> updateSvcLabor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcLaborDTO svcLaborDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcLabor : {}, {}", id, svcLaborDTO);
        if (svcLaborDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcLaborDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcLaborRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcLaborDTO result = svcLaborService.save(svcLaborDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcLaborDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-labors/:id} : Partial updates given fields of an existing svcLabor, field will ignore if it is null
     *
     * @param id the id of the svcLaborDTO to save.
     * @param svcLaborDTO the svcLaborDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcLaborDTO,
     * or with status {@code 400 (Bad Request)} if the svcLaborDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcLaborDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcLaborDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-labors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcLaborDTO> partialUpdateSvcLabor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcLaborDTO svcLaborDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcLabor partially : {}, {}", id, svcLaborDTO);
        if (svcLaborDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcLaborDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcLaborRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcLaborDTO> result = svcLaborService.partialUpdate(svcLaborDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcLaborDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-labors} : get all the svcLabors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcLabors in body.
     */
    @GetMapping("/svc-labors")
    public ResponseEntity<List<SvcLaborDTO>> getAllSvcLabors(SvcLaborCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcLabors by criteria: {}", criteria);
        Page<SvcLaborDTO> page = svcLaborQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-labors/count} : count all the svcLabors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-labors/count")
    public ResponseEntity<Long> countSvcLabors(SvcLaborCriteria criteria) {
        log.debug("REST request to count SvcLabors by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcLaborQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-labors/:id} : get the "id" svcLabor.
     *
     * @param id the id of the svcLaborDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcLaborDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-labors/{id}")
    public ResponseEntity<SvcLaborDTO> getSvcLabor(@PathVariable Long id) {
        log.debug("REST request to get SvcLabor : {}", id);
        Optional<SvcLaborDTO> svcLaborDTO = svcLaborService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcLaborDTO);
    }

    /**
     * {@code DELETE  /svc-labors/:id} : delete the "id" svcLabor.
     *
     * @param id the id of the svcLaborDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-labors/{id}")
    public ResponseEntity<Void> deleteSvcLabor(@PathVariable Long id) {
        log.debug("REST request to delete SvcLabor : {}", id);
        svcLaborService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
