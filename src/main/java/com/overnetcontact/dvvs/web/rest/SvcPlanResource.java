package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcPlanRepository;
import com.overnetcontact.dvvs.service.*;
import com.overnetcontact.dvvs.service.criteria.SvcPlanCriteria;
import com.overnetcontact.dvvs.service.dto.SvcFullPlanDTO;
import com.overnetcontact.dvvs.service.dto.SvcPlanDTO;
import com.overnetcontact.dvvs.service.dto.SvcPlanPartDTO;
import com.overnetcontact.dvvs.service.dto.SvcPlanUnitDTO;
import com.overnetcontact.dvvs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcPlan}.
 */
@RestController
@RequestMapping("/api")
public class SvcPlanResource {

    private final Logger log = LoggerFactory.getLogger(SvcPlanResource.class);

    private static final String ENTITY_NAME = "svcPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcPlanService svcPlanService;

    private final SvcPlanUnitService svcPlanUnitService;

    private final SvcPlanPartService svcPlanPartService;

    private final SvcPlanRepository svcPlanRepository;

    private final SvcPlanQueryService svcPlanQueryService;

    public SvcPlanResource(
        SvcPlanService svcPlanService,
        SvcPlanUnitService svcPlanUnitService,
        SvcPlanPartService svcPlanPartService,
        SvcPlanRepository svcPlanRepository,
        SvcPlanQueryService svcPlanQueryService
    ) {
        this.svcPlanService = svcPlanService;
        this.svcPlanUnitService = svcPlanUnitService;
        this.svcPlanPartService = svcPlanPartService;
        this.svcPlanRepository = svcPlanRepository;
        this.svcPlanQueryService = svcPlanQueryService;
    }

    /**
     * {@code POST  /svc-plans} : Create a new svcPlan.
     *
     * @param svcPlanDTO the svcPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcPlanDTO, or with status {@code 400 (Bad Request)} if the svcPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-plans")
    public ResponseEntity<SvcPlanDTO> createSvcPlan(@Valid @RequestBody SvcPlanDTO svcPlanDTO) throws URISyntaxException {
        log.debug("REST request to save SvcPlan : {}", svcPlanDTO);
        if (svcPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcPlanDTO result = svcPlanService.save(svcPlanDTO);
        return ResponseEntity
            .created(new URI("/api/svc-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-plans/:id} : Updates an existing svcPlan.
     *
     * @param id the id of the svcPlanDTO to save.
     * @param svcPlanDTO the svcPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcPlanDTO,
     * or with status {@code 400 (Bad Request)} if the svcPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-plans/{id}")
    public ResponseEntity<SvcPlanDTO> updateSvcPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcPlanDTO svcPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcPlan : {}, {}", id, svcPlanDTO);
        if (svcPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcPlanDTO result = svcPlanService.save(svcPlanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcPlanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-plans/:id} : Partial updates given fields of an existing svcPlan, field will ignore if it is null
     *
     * @param id the id of the svcPlanDTO to save.
     * @param svcPlanDTO the svcPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcPlanDTO,
     * or with status {@code 400 (Bad Request)} if the svcPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-plans/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcPlanDTO> partialUpdateSvcPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcPlanDTO svcPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcPlan partially : {}, {}", id, svcPlanDTO);
        if (svcPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcPlanDTO> result = svcPlanService.partialUpdate(svcPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-plans} : get all the svcPlans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcPlans in body.
     */
    @GetMapping("/svc-plans")
    public ResponseEntity<List<SvcPlanDTO>> getAllSvcPlans(SvcPlanCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcPlans by criteria: {}", criteria);
        Page<SvcPlanDTO> page = svcPlanQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-plans/count} : count all the svcPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-plans/count")
    public ResponseEntity<Long> countSvcPlans(SvcPlanCriteria criteria) {
        log.debug("REST request to count SvcPlans by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcPlanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-plans/:id} : get the "id" svcPlan.
     *
     * @param id the id of the svcPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-plans/{id}")
    public ResponseEntity<SvcPlanDTO> getSvcPlan(@PathVariable Long id) {
        log.debug("REST request to get SvcPlan : {}", id);
        Optional<SvcPlanDTO> svcPlanDTO = svcPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcPlanDTO);
    }

    /**
     * {@code DELETE  /svc-plans/:id} : delete the "id" svcPlan.
     *
     * @param id the id of the svcPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-plans/{id}")
    public ResponseEntity<Void> deleteSvcPlan(@PathVariable Long id) {
        log.debug("REST request to delete SvcPlan : {}", id);
        svcPlanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /svc-plans} : get all the svcPlans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcPlans in body.
     */
    @GetMapping("/svc-plans-details")
    public ResponseEntity<List<SvcPlanDTO>> getAllSvcPlansDetail(SvcPlanCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcPlans by criteria: {}", criteria);
        Page<SvcPlanDTO> page = svcPlanQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code POST  /svc-plans} : Create a new svcPlan.
     *
     * @param svcFullPlanDTO the svcPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcPlanDTO, or with status {@code 400 (Bad Request)} if the svcPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-full-plans")
    public ResponseEntity<String> createFullSvcPlan(@Valid @RequestBody SvcFullPlanDTO svcFullPlanDTO) throws URISyntaxException {
        log.debug("REST request to save full SvcPlan : {}", svcFullPlanDTO);
        if (svcFullPlanDTO.getSvcPlanDTO().getId() != null) {
            throw new BadRequestAlertException("A new svcPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }

        for (SvcPlanUnitDTO svcPlanUnitDTO : svcFullPlanDTO.getSvcPlanUnitDTOList()) {
            if (svcPlanUnitDTO.getId() != null) {
                throw new BadRequestAlertException("A new svcPlanUnit cannot already have an ID", ENTITY_NAME, "idexists");
            }

            for (SvcPlanPartDTO svcPlanPartDTO : svcPlanUnitDTO.getSvcPlanPartDTOList()) {
                if (svcPlanPartDTO.getId() != null) {
                    throw new BadRequestAlertException("A new svcPlanPart cannot already have an ID", ENTITY_NAME, "idexists");
                }
            }
        }

        SvcPlanDTO plan = svcPlanService.save(svcFullPlanDTO.getSvcPlanDTO());
        for (SvcPlanUnitDTO svcPlanUnitDTO : svcFullPlanDTO.getSvcPlanUnitDTOList()) {
            svcPlanUnitDTO.setSvcPlan(plan);
            SvcPlanUnitDTO planUnit = svcPlanUnitService.save(svcPlanUnitDTO);

            for (SvcPlanPartDTO svcPlanPartDTO : svcPlanUnitDTO.getSvcPlanPartDTOList()) {
                svcPlanPartDTO.setPlanUnitID(plan.getId());
                svcPlanPartDTO.setPlanUnitID(planUnit.getId());
                svcPlanPartService.save(svcPlanPartDTO);
            }
        }

        return ResponseEntity
            .created(new URI("/svc-full-plans"))
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, ""))
            .body("Create Successfully ");
    }

    /**
     * {@code POST  /svc-plans} : Create a new svcPlan.
     *
     * @param id the svcPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcPlanDTO, or with status {@code 400 (Bad Request)} if the svcPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @GetMapping("/svc-full-plans/{id}")
    public ResponseEntity<SvcFullPlanDTO> getFullSvcPlan(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to get full SvcPlan : {}", id);
        SvcFullPlanDTO svcFullPlanDTO = new SvcFullPlanDTO();
        Optional<SvcPlanDTO> planDTO = svcPlanService.findOne(id);
        List<SvcPlanUnitDTO> svcPlanUnitDTOS = svcPlanUnitService.findBySvcPlan(planDTO.get());
        svcFullPlanDTO.setSvcPlanDTO(planDTO.get());

        for (SvcPlanUnitDTO svcPlanUnitDTO : svcPlanUnitDTOS) {
            List<SvcPlanPartDTO> svcPlanPartDTOList = svcPlanPartService.findByPlanUnitID(svcPlanUnitDTO.getId());
            svcPlanUnitDTO.setSvcPlanPartDTOList(svcPlanPartDTOList);
        }

        svcFullPlanDTO.setSvcPlanUnitDTOList(svcPlanUnitDTOS);
        return ResponseEntity
            .created(new URI("/svc-full-plans"))
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, ""))
            .body(svcFullPlanDTO);
    }
}
