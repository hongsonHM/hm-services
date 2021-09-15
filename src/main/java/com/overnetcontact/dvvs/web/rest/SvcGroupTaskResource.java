package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcGroupTaskRepository;
import com.overnetcontact.dvvs.service.SvcGroupTaskQueryService;
import com.overnetcontact.dvvs.service.SvcGroupTaskService;
import com.overnetcontact.dvvs.service.criteria.SvcGroupTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcGroupTaskDTO;
import com.overnetcontact.dvvs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcGroupTask}.
 */
@RestController
@RequestMapping("/api")
public class SvcGroupTaskResource {

    private final Logger log = LoggerFactory.getLogger(SvcGroupTaskResource.class);

    private static final String ENTITY_NAME = "svcGroupTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcGroupTaskService svcGroupTaskService;

    private final SvcGroupTaskRepository svcGroupTaskRepository;

    private final SvcGroupTaskQueryService svcGroupTaskQueryService;

    public SvcGroupTaskResource(
        SvcGroupTaskService svcGroupTaskService,
        SvcGroupTaskRepository svcGroupTaskRepository,
        SvcGroupTaskQueryService svcGroupTaskQueryService
    ) {
        this.svcGroupTaskService = svcGroupTaskService;
        this.svcGroupTaskRepository = svcGroupTaskRepository;
        this.svcGroupTaskQueryService = svcGroupTaskQueryService;
    }

    /**
     * {@code POST  /svc-group-tasks} : Create a new svcGroupTask.
     *
     * @param svcGroupTaskDTO the svcGroupTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcGroupTaskDTO, or with status {@code 400 (Bad Request)} if the svcGroupTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-group-tasks")
    public ResponseEntity<SvcGroupTaskDTO> createSvcGroupTask(@RequestBody SvcGroupTaskDTO svcGroupTaskDTO) throws URISyntaxException {
        log.debug("REST request to save SvcGroupTask : {}", svcGroupTaskDTO);
        if (svcGroupTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcGroupTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcGroupTaskDTO result = svcGroupTaskService.save(svcGroupTaskDTO);
        return ResponseEntity
            .created(new URI("/api/svc-group-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-group-tasks/:id} : Updates an existing svcGroupTask.
     *
     * @param id the id of the svcGroupTaskDTO to save.
     * @param svcGroupTaskDTO the svcGroupTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcGroupTaskDTO,
     * or with status {@code 400 (Bad Request)} if the svcGroupTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcGroupTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-group-tasks/{id}")
    public ResponseEntity<SvcGroupTaskDTO> updateSvcGroupTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SvcGroupTaskDTO svcGroupTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcGroupTask : {}, {}", id, svcGroupTaskDTO);
        if (svcGroupTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcGroupTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcGroupTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcGroupTaskDTO result = svcGroupTaskService.save(svcGroupTaskDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcGroupTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-group-tasks/:id} : Partial updates given fields of an existing svcGroupTask, field will ignore if it is null
     *
     * @param id the id of the svcGroupTaskDTO to save.
     * @param svcGroupTaskDTO the svcGroupTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcGroupTaskDTO,
     * or with status {@code 400 (Bad Request)} if the svcGroupTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcGroupTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcGroupTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-group-tasks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcGroupTaskDTO> partialUpdateSvcGroupTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SvcGroupTaskDTO svcGroupTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcGroupTask partially : {}, {}", id, svcGroupTaskDTO);
        if (svcGroupTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcGroupTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcGroupTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcGroupTaskDTO> result = svcGroupTaskService.partialUpdate(svcGroupTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcGroupTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-group-tasks} : get all the svcGroupTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcGroupTasks in body.
     */
    @GetMapping("/svc-group-tasks")
    public ResponseEntity<List<SvcGroupTaskDTO>> getAllSvcGroupTasks(SvcGroupTaskCriteria criteria) {
        log.debug("REST request to get SvcGroupTasks by criteria: {}", criteria);
        List<SvcGroupTaskDTO> entityList = svcGroupTaskQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /svc-group-tasks/count} : count all the svcGroupTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-group-tasks/count")
    public ResponseEntity<Long> countSvcGroupTasks(SvcGroupTaskCriteria criteria) {
        log.debug("REST request to count SvcGroupTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcGroupTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-group-tasks/:id} : get the "id" svcGroupTask.
     *
     * @param id the id of the svcGroupTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcGroupTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-group-tasks/{id}")
    public ResponseEntity<SvcGroupTaskDTO> getSvcGroupTask(@PathVariable Long id) {
        log.debug("REST request to get SvcGroupTask : {}", id);
        Optional<SvcGroupTaskDTO> svcGroupTaskDTO = svcGroupTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcGroupTaskDTO);
    }

    /**
     * {@code DELETE  /svc-group-tasks/:id} : delete the "id" svcGroupTask.
     *
     * @param id the id of the svcGroupTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-group-tasks/{id}")
    public ResponseEntity<Void> deleteSvcGroupTask(@PathVariable Long id) {
        log.debug("REST request to delete SvcGroupTask : {}", id);
        svcGroupTaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
