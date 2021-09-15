package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.CoreSuppliesRepository;
import com.overnetcontact.dvvs.service.CoreSuppliesQueryService;
import com.overnetcontact.dvvs.service.CoreSuppliesService;
import com.overnetcontact.dvvs.service.criteria.CoreSuppliesCriteria;
import com.overnetcontact.dvvs.service.dto.CoreSuppliesDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.CoreSupplies}.
 */
@RestController
@RequestMapping("/api")
public class CoreSuppliesResource {

    private final Logger log = LoggerFactory.getLogger(CoreSuppliesResource.class);

    private static final String ENTITY_NAME = "coreSupplies";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoreSuppliesService coreSuppliesService;

    private final CoreSuppliesRepository coreSuppliesRepository;

    private final CoreSuppliesQueryService coreSuppliesQueryService;

    public CoreSuppliesResource(
        CoreSuppliesService coreSuppliesService,
        CoreSuppliesRepository coreSuppliesRepository,
        CoreSuppliesQueryService coreSuppliesQueryService
    ) {
        this.coreSuppliesService = coreSuppliesService;
        this.coreSuppliesRepository = coreSuppliesRepository;
        this.coreSuppliesQueryService = coreSuppliesQueryService;
    }

    /**
     * {@code POST  /core-supplies} : Create a new coreSupplies.
     *
     * @param coreSuppliesDTO the coreSuppliesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coreSuppliesDTO, or with status {@code 400 (Bad Request)} if the coreSupplies has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/core-supplies")
    public ResponseEntity<CoreSuppliesDTO> createCoreSupplies(@RequestBody CoreSuppliesDTO coreSuppliesDTO) throws URISyntaxException {
        log.debug("REST request to save CoreSupplies : {}", coreSuppliesDTO);
        if (coreSuppliesDTO.getId() != null) {
            throw new BadRequestAlertException("A new coreSupplies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoreSuppliesDTO result = coreSuppliesService.save(coreSuppliesDTO);
        return ResponseEntity
            .created(new URI("/api/core-supplies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /core-supplies/:id} : Updates an existing coreSupplies.
     *
     * @param id the id of the coreSuppliesDTO to save.
     * @param coreSuppliesDTO the coreSuppliesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coreSuppliesDTO,
     * or with status {@code 400 (Bad Request)} if the coreSuppliesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coreSuppliesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/core-supplies/{id}")
    public ResponseEntity<CoreSuppliesDTO> updateCoreSupplies(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoreSuppliesDTO coreSuppliesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CoreSupplies : {}, {}", id, coreSuppliesDTO);
        if (coreSuppliesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coreSuppliesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coreSuppliesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CoreSuppliesDTO result = coreSuppliesService.save(coreSuppliesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coreSuppliesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /core-supplies/:id} : Partial updates given fields of an existing coreSupplies, field will ignore if it is null
     *
     * @param id the id of the coreSuppliesDTO to save.
     * @param coreSuppliesDTO the coreSuppliesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coreSuppliesDTO,
     * or with status {@code 400 (Bad Request)} if the coreSuppliesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coreSuppliesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coreSuppliesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/core-supplies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CoreSuppliesDTO> partialUpdateCoreSupplies(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoreSuppliesDTO coreSuppliesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CoreSupplies partially : {}, {}", id, coreSuppliesDTO);
        if (coreSuppliesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coreSuppliesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coreSuppliesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoreSuppliesDTO> result = coreSuppliesService.partialUpdate(coreSuppliesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coreSuppliesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /core-supplies} : get all the coreSupplies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coreSupplies in body.
     */
    @GetMapping("/core-supplies")
    public ResponseEntity<List<CoreSuppliesDTO>> getAllCoreSupplies(CoreSuppliesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CoreSupplies by criteria: {}", criteria);
        Page<CoreSuppliesDTO> page = coreSuppliesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /core-supplies/count} : count all the coreSupplies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/core-supplies/count")
    public ResponseEntity<Long> countCoreSupplies(CoreSuppliesCriteria criteria) {
        log.debug("REST request to count CoreSupplies by criteria: {}", criteria);
        return ResponseEntity.ok().body(coreSuppliesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /core-supplies/:id} : get the "id" coreSupplies.
     *
     * @param id the id of the coreSuppliesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coreSuppliesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/core-supplies/{id}")
    public ResponseEntity<CoreSuppliesDTO> getCoreSupplies(@PathVariable Long id) {
        log.debug("REST request to get CoreSupplies : {}", id);
        Optional<CoreSuppliesDTO> coreSuppliesDTO = coreSuppliesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coreSuppliesDTO);
    }

    /**
     * {@code DELETE  /core-supplies/:id} : delete the "id" coreSupplies.
     *
     * @param id the id of the coreSuppliesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/core-supplies/{id}")
    public ResponseEntity<Void> deleteCoreSupplies(@PathVariable Long id) {
        log.debug("REST request to delete CoreSupplies : {}", id);
        coreSuppliesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
