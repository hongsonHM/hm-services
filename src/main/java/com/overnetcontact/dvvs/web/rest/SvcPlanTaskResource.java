package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcPlanTaskRepository;
import com.overnetcontact.dvvs.service.SvcPlanTaskQueryService;
import com.overnetcontact.dvvs.service.SvcPlanTaskService;
import com.overnetcontact.dvvs.service.criteria.SvcPlanTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanTaskDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcPlanTask}.
 */
@RestController
@RequestMapping("/api")
public class SvcPlanTaskResource {

    private final Logger log = LoggerFactory.getLogger(SvcPlanTaskResource.class);

    private static final String ENTITY_NAME = "svcPlanTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcPlanTaskService svcPlanTaskService;

    private final SvcPlanTaskRepository svcPlanTaskRepository;

    private final SvcPlanTaskQueryService svcPlanTaskQueryService;

    public SvcPlanTaskResource(
        SvcPlanTaskService svcPlanTaskService,
        SvcPlanTaskRepository svcPlanTaskRepository,
        SvcPlanTaskQueryService svcPlanTaskQueryService
    ) {
        this.svcPlanTaskService = svcPlanTaskService;
        this.svcPlanTaskRepository = svcPlanTaskRepository;
        this.svcPlanTaskQueryService = svcPlanTaskQueryService;
    }

    /**
     * {@code POST  /svc-plan-tasks} : Create a new svcPlanTask.
     *
     * @param svcPlanTaskDTO the svcPlanTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcPlanTaskDTO, or with status {@code 400 (Bad Request)} if the svcPlanTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-plan-tasks")
    public ResponseEntity<SvcPlanTaskDTO> createSvcPlanTask(@Valid @RequestBody SvcPlanTaskDTO svcPlanTaskDTO) throws URISyntaxException {
        log.debug("REST request to save SvcPlanTask : {}", svcPlanTaskDTO);
        if (svcPlanTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcPlanTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcPlanTaskDTO result = svcPlanTaskService.save(svcPlanTaskDTO);
        return ResponseEntity
            .created(new URI("/api/svc-plan-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-plan-tasks/:id} : Updates an existing svcPlanTask.
     *
     * @param id the id of the svcPlanTaskDTO to save.
     * @param svcPlanTaskDTO the svcPlanTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcPlanTaskDTO,
     * or with status {@code 400 (Bad Request)} if the svcPlanTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcPlanTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-plan-tasks/{id}")
    public ResponseEntity<SvcPlanTaskDTO> updateSvcPlanTask(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcPlanTaskDTO svcPlanTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcPlanTask : {}, {}", id, svcPlanTaskDTO);
        if (svcPlanTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcPlanTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcPlanTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcPlanTaskDTO result = svcPlanTaskService.save(svcPlanTaskDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcPlanTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-plan-tasks/:id} : Partial updates given fields of an existing svcPlanTask, field will ignore if it is null
     *
     * @param id the id of the svcPlanTaskDTO to save.
     * @param svcPlanTaskDTO the svcPlanTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcPlanTaskDTO,
     * or with status {@code 400 (Bad Request)} if the svcPlanTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcPlanTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcPlanTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-plan-tasks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcPlanTaskDTO> partialUpdateSvcPlanTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcPlanTaskDTO svcPlanTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcPlanTask partially : {}, {}", id, svcPlanTaskDTO);
        if (svcPlanTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcPlanTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcPlanTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcPlanTaskDTO> result = svcPlanTaskService.partialUpdate(svcPlanTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcPlanTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-plan-tasks} : get all the svcPlanTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcPlanTasks in body.
     */
    @GetMapping("/svc-plan-tasks")
    public ResponseEntity<List<SvcPlanTaskDTO>> getAllSvcPlanTasks(SvcPlanTaskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcPlanTasks by criteria: {}", criteria);
        Page<SvcPlanTaskDTO> page = svcPlanTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-plan-tasks/count} : count all the svcPlanTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-plan-tasks/count")
    public ResponseEntity<Long> countSvcPlanTasks(SvcPlanTaskCriteria criteria) {
        log.debug("REST request to count SvcPlanTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcPlanTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-plan-tasks/:id} : get the "id" svcPlanTask.
     *
     * @param id the id of the svcPlanTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcPlanTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-plan-tasks/{id}")
    public ResponseEntity<SvcPlanTaskDTO> getSvcPlanTask(@PathVariable Long id) {
        log.debug("REST request to get SvcPlanTask : {}", id);
        Optional<SvcPlanTaskDTO> svcPlanTaskDTO = svcPlanTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcPlanTaskDTO);
    }

    /**
     * {@code DELETE  /svc-plan-tasks/:id} : delete the "id" svcPlanTask.
     *
     * @param id the id of the svcPlanTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-plan-tasks/{id}")
    public ResponseEntity<Void> deleteSvcPlanTask(@PathVariable Long id) {
        log.debug("REST request to delete SvcPlanTask : {}", id);
        svcPlanTaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
