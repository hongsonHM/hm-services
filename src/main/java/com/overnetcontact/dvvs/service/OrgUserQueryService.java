package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.service.criteria.OrgUserCriteria;
import com.overnetcontact.dvvs.service.dto.OrgUserDTO;
import com.overnetcontact.dvvs.service.mapper.OrgUserMapper;
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
 * Service for executing complex queries for {@link OrgUser} entities in the database.
 * The main input is a {@link OrgUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrgUserDTO} or a {@link Page} of {@link OrgUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrgUserQueryService extends QueryService<OrgUser> {

    private final Logger log = LoggerFactory.getLogger(OrgUserQueryService.class);

    private final OrgUserRepository orgUserRepository;

    private final OrgUserMapper orgUserMapper;

    public OrgUserQueryService(OrgUserRepository orgUserRepository, OrgUserMapper orgUserMapper) {
        this.orgUserRepository = orgUserRepository;
        this.orgUserMapper = orgUserMapper;
    }

    /**
     * Return a {@link List} of {@link OrgUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrgUserDTO> findByCriteria(OrgUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrgUser> specification = createSpecification(criteria);
        return orgUserMapper.toDto(orgUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrgUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgUserDTO> findByCriteria(OrgUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrgUser> specification = createSpecification(criteria);
        return orgUserRepository.findAll(specification, page).map(orgUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrgUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrgUser> specification = createSpecification(criteria);
        return orgUserRepository.count(specification);
    }

    /**
     * Function to convert {@link OrgUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrgUser> createSpecification(OrgUserCriteria criteria) {
        Specification<OrgUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrgUser_.id));
            }
            if (criteria.getDeviceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceId(), OrgUser_.deviceId));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), OrgUser_.phone));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildSpecification(criteria.getRole(), OrgUser_.role));
            }
            if (criteria.getInternalUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInternalUserId(),
                            root -> root.join(OrgUser_.internalUser, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getNotificationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotificationsId(),
                            root -> root.join(OrgUser_.notifications, JoinType.LEFT).get(OrgNotification_.id)
                        )
                    );
            }
            if (criteria.getGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGroupId(), root -> root.join(OrgUser_.group, JoinType.LEFT).get(OrgGroup_.id))
                    );
            }
        }
        return specification;
    }
}
