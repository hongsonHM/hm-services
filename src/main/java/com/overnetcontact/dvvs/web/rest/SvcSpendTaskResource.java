package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcSpendTaskRepository;
import com.overnetcontact.dvvs.service.SvcAreaService;
import com.overnetcontact.dvvs.service.SvcGroupTaskService;
import com.overnetcontact.dvvs.service.SvcSpendTaskQueryService;
import com.overnetcontact.dvvs.service.SvcSpendTaskService;
import com.overnetcontact.dvvs.service.criteria.SvcSpendTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcAreaDTO;
import com.overnetcontact.dvvs.service.dto.SvcGroupTaskDTO;
import com.overnetcontact.dvvs.service.dto.SvcSpendTaskDTO;
import com.overnetcontact.dvvs.service.dto.SvcSpendTaskForAreaDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcSpendTask}.
 */
@RestController
@RequestMapping("/api")
public class SvcSpendTaskResource {

    private final Logger log = LoggerFactory.getLogger(SvcSpendTaskResource.class);

    private static final String ENTITY_NAME = "svcSpendTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcSpendTaskService svcSpendTaskService;

    private final SvcGroupTaskService svcGroupTaskService;

    private final SvcAreaService svcAreaService;

    private final SvcSpendTaskRepository svcSpendTaskRepository;

    private final SvcSpendTaskQueryService svcSpendTaskQueryService;

    public SvcSpendTaskResource(
        SvcSpendTaskService svcSpendTaskService,
        SvcGroupTaskService svcGroupTaskService,
        SvcAreaService svcAreaService,
        SvcSpendTaskRepository svcSpendTaskRepository,
        SvcSpendTaskQueryService svcSpendTaskQueryService
    ) {
        this.svcSpendTaskService = svcSpendTaskService;
        this.svcGroupTaskService = svcGroupTaskService;
        this.svcAreaService = svcAreaService;
        this.svcSpendTaskRepository = svcSpendTaskRepository;
        this.svcSpendTaskQueryService = svcSpendTaskQueryService;
    }

    /**
     * {@code POST  /svc-spend-tasks} : Create a new svcSpendTask.
     *
     * @param svcSpendTaskDTO the svcSpendTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcSpendTaskDTO, or with status {@code 400 (Bad Request)} if the svcSpendTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-spend-tasks")
    public ResponseEntity<SvcSpendTaskDTO> createSvcSpendTask(@RequestBody SvcSpendTaskDTO svcSpendTaskDTO) throws URISyntaxException {
        log.debug("REST request to save SvcSpendTask : {}", svcSpendTaskDTO);
        if (svcSpendTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcSpendTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcSpendTaskDTO result = svcSpendTaskService.save(svcSpendTaskDTO);
        return ResponseEntity
            .created(new URI("/api/svc-spend-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-spend-tasks/:id} : Updates an existing svcSpendTask.
     *
     * @param id the id of the svcSpendTaskDTO to save.
     * @param svcSpendTaskDTO the svcSpendTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcSpendTaskDTO,
     * or with status {@code 400 (Bad Request)} if the svcSpendTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcSpendTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-spend-tasks/{id}")
    public ResponseEntity<SvcSpendTaskDTO> updateSvcSpendTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SvcSpendTaskDTO svcSpendTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcSpendTask : {}, {}", id, svcSpendTaskDTO);
        if (svcSpendTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcSpendTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcSpendTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcSpendTaskDTO result = svcSpendTaskService.save(svcSpendTaskDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcSpendTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-spend-tasks/:id} : Partial updates given fields of an existing svcSpendTask, field will ignore if it is null
     *
     * @param id the id of the svcSpendTaskDTO to save.
     * @param svcSpendTaskDTO the svcSpendTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcSpendTaskDTO,
     * or with status {@code 400 (Bad Request)} if the svcSpendTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcSpendTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcSpendTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-spend-tasks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcSpendTaskDTO> partialUpdateSvcSpendTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SvcSpendTaskDTO svcSpendTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcSpendTask partially : {}, {}", id, svcSpendTaskDTO);
        if (svcSpendTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcSpendTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcSpendTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcSpendTaskDTO> result = svcSpendTaskService.partialUpdate(svcSpendTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcSpendTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-spend-tasks} : get all the svcSpendTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcSpendTasks in body.
     */
    @GetMapping("/svc-spend-tasks")
    public ResponseEntity<List<SvcSpendTaskDTO>> getAllSvcSpendTasks(SvcSpendTaskCriteria criteria) {
        log.debug("REST request to get SvcSpendTasks by criteria: {}", criteria);
        List<SvcSpendTaskDTO> entityList = svcSpendTaskQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /svc-spend-tasks/count} : count all the svcSpendTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-spend-tasks/count")
    public ResponseEntity<Long> countSvcSpendTasks(SvcSpendTaskCriteria criteria) {
        log.debug("REST request to count SvcSpendTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcSpendTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-spend-tasks/:id} : get the "id" svcSpendTask.
     *
     * @param id the id of the svcSpendTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcSpendTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-spend-tasks/{id}")
    public ResponseEntity<SvcSpendTaskDTO> getSvcSpendTask(@PathVariable Long id) {
        log.debug("REST request to get SvcSpendTask : {}", id);
        Optional<SvcSpendTaskDTO> svcSpendTaskDTO = svcSpendTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcSpendTaskDTO);
    }

    /**
     * {@code DELETE  /svc-spend-tasks/:id} : delete the "id" svcSpendTask.
     *
     * @param id the id of the svcSpendTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-spend-tasks/{id}")
    public ResponseEntity<Void> deleteSvcSpendTask(@PathVariable Long id) {
        log.debug("REST request to delete SvcSpendTask : {}", id);
        svcSpendTaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * Customize
     * {@code POST  /svc-spend-tasks} : Create a new batch svcSpendTask.
     *
     * @param svcSpendTaskForAreaDTO the svcSpendTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcSpendTaskDTO, or with status {@code 400 (Bad Request)} if the svcSpendTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-spend-tasks/batch")
    public ResponseEntity<String> createSvcSpendTaskBatch(@RequestBody SvcSpendTaskForAreaDTO svcSpendTaskForAreaDTO)
        throws URISyntaxException {
        log.debug("REST request to save batch SvcSpendTask : {}", svcSpendTaskForAreaDTO);

        int count = 0;
        SvcAreaDTO result = svcAreaService.save(svcSpendTaskForAreaDTO.getSvcAreaDTO());
        SvcGroupTaskDTO svcGroupTaskDTO = new SvcGroupTaskDTO();
        svcGroupTaskDTO.setSvcArea(result);
        svcGroupTaskDTO.setName("Group task of " + result.getName());
        SvcGroupTaskDTO rsGroupTask = svcGroupTaskService.save(svcGroupTaskDTO);

        for (SvcSpendTaskDTO svcSpendTaskDTO : svcSpendTaskForAreaDTO.getSvcSpendTaskDTOs()) {
            if (svcSpendTaskDTO.getId() != null || svcSpendTaskDTO.getSvcGroupTask() != null) {
                throw new BadRequestAlertException("A new svcSpendTask cannot already have an ID", ENTITY_NAME, "idexists");
            } else {
                svcSpendTaskDTO.setSvcGroupTask(rsGroupTask);
                svcSpendTaskService.save(svcSpendTaskDTO);
                count++;
            }
        }

        return ResponseEntity
            .created(new URI("/api/svc-spend-tasks/count" + count))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, String.valueOf(count)))
            .body("Total create: " + count);
    }
}
