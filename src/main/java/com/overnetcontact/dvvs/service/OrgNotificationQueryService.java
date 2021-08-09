package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.OrgNotification;
import com.overnetcontact.dvvs.repository.OrgNotificationRepository;
import com.overnetcontact.dvvs.service.criteria.OrgNotificationCriteria;
import com.overnetcontact.dvvs.service.dto.OrgNotificationDTO;
import com.overnetcontact.dvvs.service.mapper.OrgNotificationMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OrgNotification} entities in the database.
 * The main input is a {@link OrgNotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrgNotificationDTO} or a {@link Page} of {@link OrgNotificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrgNotificationQueryService extends QueryService<OrgNotification> {

    private final Logger log = LoggerFactory.getLogger(OrgNotificationQueryService.class);

    private final OrgNotificationRepository orgNotificationRepository;

    private final OrgNotificationMapper orgNotificationMapper;

    public OrgNotificationQueryService(OrgNotificationRepository orgNotificationRepository, OrgNotificationMapper orgNotificationMapper) {
        this.orgNotificationRepository = orgNotificationRepository;
        this.orgNotificationMapper = orgNotificationMapper;
    }

    /**
     * Return a {@link List} of {@link OrgNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrgNotificationDTO> findByCriteria(OrgNotificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrgNotification> specification = createSpecification(criteria);
        return orgNotificationMapper.toDto(orgNotificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrgNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgNotificationDTO> findByCriteria(OrgNotificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrgNotification> specification = createSpecification(criteria);
        return orgNotificationRepository.findAll(specification, page).map(orgNotificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrgNotificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrgNotification> specification = createSpecification(criteria);
        return orgNotificationRepository.count(specification);
    }

    /**
     * Function to convert {@link OrgNotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrgNotification> createSpecification(OrgNotificationCriteria criteria) {
        Specification<OrgNotification> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrgNotification_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), OrgNotification_.title));
            }
            if (criteria.getDesc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesc(), OrgNotification_.desc));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData(), OrgNotification_.data));
            }
            if (criteria.getIsRead() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRead(), OrgNotification_.isRead));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrgNotification_.status));
            }
            if (criteria.getOrgUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrgUserId(),
                            root -> root.join(OrgNotification_.orgUser, JoinType.LEFT).get(OrgUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
