package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.domain.Authority;
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.repository.OrgNotificationRepository;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.security.AuthoritiesConstants;
import com.overnetcontact.dvvs.security.SecurityUtils;
import com.overnetcontact.dvvs.service.OrgNotificationQueryService;
import com.overnetcontact.dvvs.service.OrgNotificationService;
import com.overnetcontact.dvvs.service.criteria.OrgNotificationCriteria;
import com.overnetcontact.dvvs.service.dto.OrgNotificationDTO;
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.OrgNotification}.
 */
@RestController
@RequestMapping("/api")
public class OrgNotificationResource {

    private final Logger log = LoggerFactory.getLogger(OrgNotificationResource.class);

    private static final String ENTITY_NAME = "orgNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgNotificationService orgNotificationService;

    private final OrgNotificationRepository orgNotificationRepository;

    private final OrgNotificationQueryService orgNotificationQueryService;

    private final OrgUserRepository orgUserRepository;

    public OrgNotificationResource(
        OrgNotificationService orgNotificationService,
        OrgNotificationRepository orgNotificationRepository,
        OrgNotificationQueryService orgNotificationQueryService,
        OrgUserRepository orgUserRepository
    ) {
        this.orgNotificationService = orgNotificationService;
        this.orgNotificationRepository = orgNotificationRepository;
        this.orgNotificationQueryService = orgNotificationQueryService;
        this.orgUserRepository = orgUserRepository;
    }

    /**
     * {@code POST  /org-notifications} : Create a new orgNotification.
     *
     * @param orgNotificationDTO the orgNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgNotificationDTO, or with status {@code 400 (Bad Request)} if the orgNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/org-notifications")
    public ResponseEntity<OrgNotificationDTO> createOrgNotification(@Valid @RequestBody OrgNotificationDTO orgNotificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save OrgNotification : {}", orgNotificationDTO);
        if (orgNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new orgNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgNotificationDTO result = orgNotificationService.save(orgNotificationDTO);
        return ResponseEntity
            .created(new URI("/api/org-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /org-notifications/:id} : Updates an existing orgNotification.
     *
     * @param id the id of the orgNotificationDTO to save.
     * @param orgNotificationDTO the orgNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the orgNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/org-notifications/{id}")
    public ResponseEntity<OrgNotificationDTO> updateOrgNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrgNotificationDTO orgNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrgNotification : {}, {}", id, orgNotificationDTO);
        if (orgNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrgNotificationDTO result = orgNotificationService.save(orgNotificationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgNotificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /org-notifications/:id} : Partial updates given fields of an existing orgNotification, field will ignore if it is null
     *
     * @param id the id of the orgNotificationDTO to save.
     * @param orgNotificationDTO the orgNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the orgNotificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orgNotificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/org-notifications/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrgNotificationDTO> partialUpdateOrgNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrgNotificationDTO orgNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgNotification partially : {}, {}", id, orgNotificationDTO);
        if (orgNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgNotificationDTO> result = orgNotificationService.partialUpdate(orgNotificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgNotificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /org-notifications} : get all the orgNotifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgNotifications in body.
     */
    @GetMapping("/org-notifications")
    public ResponseEntity<List<OrgNotificationDTO>> getAllOrgNotifications(OrgNotificationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrgNotifications by criteria: {}", criteria);
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.BUSINESS_MANAGER)) {
            String username = SecurityUtils.getCurrentUserLogin().orElseThrow();
            OrgUser userEntity = orgUserRepository.findByInternalUser_Login(username).orElseThrow();
            LongFilter userFilter = new LongFilter();
            userFilter.setEquals(userEntity.getId());
            criteria.setOrgUserId(userFilter);
        }
        Page<OrgNotificationDTO> page = orgNotificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /org-notifications/count} : count all the orgNotifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/org-notifications/count")
    public ResponseEntity<Long> countOrgNotifications(OrgNotificationCriteria criteria) {
        log.debug("REST request to count OrgNotifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(orgNotificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /org-notifications/:id} : get the "id" orgNotification.
     *
     * @param id the id of the orgNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/org-notifications/{id}")
    public ResponseEntity<OrgNotificationDTO> getOrgNotification(@PathVariable Long id) {
        log.debug("REST request to get OrgNotification : {}", id);
        Optional<OrgNotificationDTO> orgNotificationDTO = orgNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgNotificationDTO);
    }

    /**
     * {@code DELETE  /org-notifications/:id} : delete the "id" orgNotification.
     *
     * @param id the id of the orgNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/org-notifications/{id}")
    public ResponseEntity<Void> deleteOrgNotification(@PathVariable Long id) {
        log.debug("REST request to delete OrgNotification : {}", id);
        orgNotificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
