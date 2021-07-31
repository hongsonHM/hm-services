package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcTargetTypeRepository;
import com.overnetcontact.dvvs.service.SvcTargetTypeService;
import com.overnetcontact.dvvs.service.dto.SvcTargetTypeDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcTargetType}.
 */
@RestController
@RequestMapping("/api")
public class SvcTargetTypeResource {

    private final Logger log = LoggerFactory.getLogger(SvcTargetTypeResource.class);

    private static final String ENTITY_NAME = "svcTargetType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcTargetTypeService svcTargetTypeService;

    private final SvcTargetTypeRepository svcTargetTypeRepository;

    public SvcTargetTypeResource(SvcTargetTypeService svcTargetTypeService, SvcTargetTypeRepository svcTargetTypeRepository) {
        this.svcTargetTypeService = svcTargetTypeService;
        this.svcTargetTypeRepository = svcTargetTypeRepository;
    }

    /**
     * {@code POST  /svc-target-types} : Create a new svcTargetType.
     *
     * @param svcTargetTypeDTO the svcTargetTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcTargetTypeDTO, or with status {@code 400 (Bad Request)} if the svcTargetType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-target-types")
    public ResponseEntity<SvcTargetTypeDTO> createSvcTargetType(@Valid @RequestBody SvcTargetTypeDTO svcTargetTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save SvcTargetType : {}", svcTargetTypeDTO);
        if (svcTargetTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcTargetType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcTargetTypeDTO result = svcTargetTypeService.save(svcTargetTypeDTO);
        return ResponseEntity
            .created(new URI("/api/svc-target-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-target-types/:id} : Updates an existing svcTargetType.
     *
     * @param id the id of the svcTargetTypeDTO to save.
     * @param svcTargetTypeDTO the svcTargetTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcTargetTypeDTO,
     * or with status {@code 400 (Bad Request)} if the svcTargetTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcTargetTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-target-types/{id}")
    public ResponseEntity<SvcTargetTypeDTO> updateSvcTargetType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcTargetTypeDTO svcTargetTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcTargetType : {}, {}", id, svcTargetTypeDTO);
        if (svcTargetTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcTargetTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcTargetTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcTargetTypeDTO result = svcTargetTypeService.save(svcTargetTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcTargetTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-target-types/:id} : Partial updates given fields of an existing svcTargetType, field will ignore if it is null
     *
     * @param id the id of the svcTargetTypeDTO to save.
     * @param svcTargetTypeDTO the svcTargetTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcTargetTypeDTO,
     * or with status {@code 400 (Bad Request)} if the svcTargetTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcTargetTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcTargetTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-target-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcTargetTypeDTO> partialUpdateSvcTargetType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcTargetTypeDTO svcTargetTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcTargetType partially : {}, {}", id, svcTargetTypeDTO);
        if (svcTargetTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcTargetTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcTargetTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcTargetTypeDTO> result = svcTargetTypeService.partialUpdate(svcTargetTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcTargetTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-target-types} : get all the svcTargetTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcTargetTypes in body.
     */
    @GetMapping("/svc-target-types")
    public ResponseEntity<List<SvcTargetTypeDTO>> getAllSvcTargetTypes(Pageable pageable) {
        log.debug("REST request to get a page of SvcTargetTypes");
        Page<SvcTargetTypeDTO> page = svcTargetTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-target-types/:id} : get the "id" svcTargetType.
     *
     * @param id the id of the svcTargetTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcTargetTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-target-types/{id}")
    public ResponseEntity<SvcTargetTypeDTO> getSvcTargetType(@PathVariable Long id) {
        log.debug("REST request to get SvcTargetType : {}", id);
        Optional<SvcTargetTypeDTO> svcTargetTypeDTO = svcTargetTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcTargetTypeDTO);
    }

    /**
     * {@code DELETE  /svc-target-types/:id} : delete the "id" svcTargetType.
     *
     * @param id the id of the svcTargetTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-target-types/{id}")
    public ResponseEntity<Void> deleteSvcTargetType(@PathVariable Long id) {
        log.debug("REST request to delete SvcTargetType : {}", id);
        svcTargetTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
