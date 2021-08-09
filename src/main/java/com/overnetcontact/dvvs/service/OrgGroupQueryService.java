package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.OrgGroup;
import com.overnetcontact.dvvs.repository.OrgGroupRepository;
import com.overnetcontact.dvvs.service.criteria.OrgGroupCriteria;
import com.overnetcontact.dvvs.service.dto.OrgGroupDTO;
import com.overnetcontact.dvvs.service.mapper.OrgGroupMapper;
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
 * Service for executing complex queries for {@link OrgGroup} entities in the database.
 * The main input is a {@link OrgGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrgGroupDTO} or a {@link Page} of {@link OrgGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrgGroupQueryService extends QueryService<OrgGroup> {

    private final Logger log = LoggerFactory.getLogger(OrgGroupQueryService.class);

    private final OrgGroupRepository orgGroupRepository;

    private final OrgGroupMapper orgGroupMapper;

    public OrgGroupQueryService(OrgGroupRepository orgGroupRepository, OrgGroupMapper orgGroupMapper) {
        this.orgGroupRepository = orgGroupRepository;
        this.orgGroupMapper = orgGroupMapper;
    }

    /**
     * Return a {@link List} of {@link OrgGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrgGroupDTO> findByCriteria(OrgGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrgGroup> specification = createSpecification(criteria);
        return orgGroupMapper.toDto(orgGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrgGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgGroupDTO> findByCriteria(OrgGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrgGroup> specification = createSpecification(criteria);
        return orgGroupRepository.findAll(specification, page).map(orgGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrgGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrgGroup> specification = createSpecification(criteria);
        return orgGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link OrgGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrgGroup> createSpecification(OrgGroupCriteria criteria) {
        Specification<OrgGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrgGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrgGroup_.name));
            }
        }
        return specification;
    }
}
