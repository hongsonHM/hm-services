package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.CoreTaskRepository;
import com.overnetcontact.dvvs.service.CoreTaskQueryService;
import com.overnetcontact.dvvs.service.CoreTaskService;
import com.overnetcontact.dvvs.service.criteria.CoreTaskCriteria;
import com.overnetcontact.dvvs.service.dto.CoreTaskDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.CoreTask}.
 */
@RestController
@RequestMapping("/api")
public class CoreTaskResource {

    private final Logger log = LoggerFactory.getLogger(CoreTaskResource.class);

    private static final String ENTITY_NAME = "coreTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoreTaskService coreTaskService;

    private final CoreTaskRepository coreTaskRepository;

    private final CoreTaskQueryService coreTaskQueryService;

    public CoreTaskResource(
        CoreTaskService coreTaskService,
        CoreTaskRepository coreTaskRepository,
        CoreTaskQueryService coreTaskQueryService
    ) {
        this.coreTaskService = coreTaskService;
        this.coreTaskRepository = coreTaskRepository;
        this.coreTaskQueryService = coreTaskQueryService;
    }

    /**
     * {@code POST  /core-tasks} : Create a new coreTask.
     *
     * @param coreTaskDTO the coreTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coreTaskDTO, or with status {@code 400 (Bad Request)} if the coreTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/core-tasks")
    public ResponseEntity<CoreTaskDTO> createCoreTask(@RequestBody CoreTaskDTO coreTaskDTO) throws URISyntaxException {
        log.debug("REST request to save CoreTask : {}", coreTaskDTO);
        if (coreTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new coreTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoreTaskDTO result = coreTaskService.save(coreTaskDTO);
        return ResponseEntity
            .created(new URI("/api/core-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /core-tasks/:id} : Updates an existing coreTask.
     *
     * @param id the id of the coreTaskDTO to save.
     * @param coreTaskDTO the coreTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coreTaskDTO,
     * or with status {@code 400 (Bad Request)} if the coreTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coreTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/core-tasks/{id}")
    public ResponseEntity<CoreTaskDTO> updateCoreTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoreTaskDTO coreTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CoreTask : {}, {}", id, coreTaskDTO);
        if (coreTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coreTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coreTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CoreTaskDTO result = coreTaskService.save(coreTaskDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coreTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /core-tasks/:id} : Partial updates given fields of an existing coreTask, field will ignore if it is null
     *
     * @param id the id of the coreTaskDTO to save.
     * @param coreTaskDTO the coreTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coreTaskDTO,
     * or with status {@code 400 (Bad Request)} if the coreTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coreTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coreTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/core-tasks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CoreTaskDTO> partialUpdateCoreTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoreTaskDTO coreTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CoreTask partially : {}, {}", id, coreTaskDTO);
        if (coreTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coreTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coreTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoreTaskDTO> result = coreTaskService.partialUpdate(coreTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coreTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /core-tasks} : get all the coreTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coreTasks in body.
     */
    @GetMapping("/core-tasks")
    public ResponseEntity<List<CoreTaskDTO>> getAllCoreTasks(CoreTaskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CoreTasks by criteria: {}", criteria);
        Page<CoreTaskDTO> page = coreTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /core-tasks/count} : count all the coreTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/core-tasks/count")
    public ResponseEntity<Long> countCoreTasks(CoreTaskCriteria criteria) {
        log.debug("REST request to count CoreTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(coreTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /core-tasks/:id} : get the "id" coreTask.
     *
     * @param id the id of the coreTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coreTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/core-tasks/{id}")
    public ResponseEntity<CoreTaskDTO> getCoreTask(@PathVariable Long id) {
        log.debug("REST request to get CoreTask : {}", id);
        Optional<CoreTaskDTO> coreTaskDTO = coreTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coreTaskDTO);
    }

    /**
     *
     * {@code DELETE  /core-tasks/:id} : delete the "id" coreTask.
     *
     * @param id the id of the coreTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/core-tasks/{id}")
    public ResponseEntity<Void> deleteCoreTask(@PathVariable Long id) {
        log.debug("REST request to delete CoreTask : {}", id);
        coreTaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
