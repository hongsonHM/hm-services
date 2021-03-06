package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.repository.SvcClientRepository;
import com.overnetcontact.dvvs.service.SvcClientQueryService;
import com.overnetcontact.dvvs.service.SvcClientService;
import com.overnetcontact.dvvs.service.criteria.SvcClientCriteria;
import com.overnetcontact.dvvs.service.dto.SvcClientDTO;
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
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcClient}.
 */
@RestController
@RequestMapping("/api")
public class SvcClientResource {

    private final Logger log = LoggerFactory.getLogger(SvcClientResource.class);

    private static final String ENTITY_NAME = "svcClient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SvcClientService svcClientService;

    private final SvcClientRepository svcClientRepository;

    private final SvcClientQueryService svcClientQueryService;

    public SvcClientResource(
        SvcClientService svcClientService,
        SvcClientRepository svcClientRepository,
        SvcClientQueryService svcClientQueryService
    ) {
        this.svcClientService = svcClientService;
        this.svcClientRepository = svcClientRepository;
        this.svcClientQueryService = svcClientQueryService;
    }

    /**
     * {@code POST  /svc-clients} : Create a new svcClient.
     *
     * @param svcClientDTO the svcClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcClientDTO, or with status {@code 400 (Bad Request)} if the svcClient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-clients")
    public ResponseEntity<SvcClientDTO> createSvcClient(@Valid @RequestBody SvcClientDTO svcClientDTO) throws URISyntaxException {
        log.debug("REST request to save SvcClient : {}", svcClientDTO);
        if (svcClientDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcClientDTO result = svcClientService.save(svcClientDTO);
        return ResponseEntity
            .created(new URI("/api/svc-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /svc-clients/:id} : Updates an existing svcClient.
     *
     * @param id the id of the svcClientDTO to save.
     * @param svcClientDTO the svcClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcClientDTO,
     * or with status {@code 400 (Bad Request)} if the svcClientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-clients/{id}")
    public ResponseEntity<SvcClientDTO> updateSvcClient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcClientDTO svcClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcClient : {}, {}", id, svcClientDTO);
        if (svcClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcClientDTO result = svcClientService.save(svcClientDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcClientDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-clients/:id} : Partial updates given fields of an existing svcClient, field will ignore if it is null
     *
     * @param id the id of the svcClientDTO to save.
     * @param svcClientDTO the svcClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcClientDTO,
     * or with status {@code 400 (Bad Request)} if the svcClientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcClientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-clients/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcClientDTO> partialUpdateSvcClient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcClientDTO svcClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcClient partially : {}, {}", id, svcClientDTO);
        if (svcClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcClientDTO> result = svcClientService.partialUpdate(svcClientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcClientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-clients} : get all the svcClients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcClients in body.
     */
    @GetMapping("/svc-clients")
    public ResponseEntity<List<SvcClientDTO>> getAllSvcClients(SvcClientCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcClients by criteria: {}", criteria);
        Page<SvcClientDTO> page = svcClientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-clients/count} : count all the svcClients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-clients/count")
    public ResponseEntity<Long> countSvcClients(SvcClientCriteria criteria) {
        log.debug("REST request to count SvcClients by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcClientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-clients/:id} : get the "id" svcClient.
     *
     * @param id the id of the svcClientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcClientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-clients/{id}")
    public ResponseEntity<SvcClientDTO> getSvcClient(@PathVariable Long id) {
        log.debug("REST request to get SvcClient : {}", id);
        Optional<SvcClientDTO> svcClientDTO = svcClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcClientDTO);
    }

    /**
     * {@code DELETE  /svc-clients/:id} : delete the "id" svcClient.
     *
     * @param id the id of the svcClientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-clients/{id}")
    public ResponseEntity<Void> deleteSvcClient(@PathVariable Long id) {
        log.debug("REST request to delete SvcClient : {}", id);
        svcClientService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
