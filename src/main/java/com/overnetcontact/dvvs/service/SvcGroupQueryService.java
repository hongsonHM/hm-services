package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcGroup;
import com.overnetcontact.dvvs.repository.SvcGroupRepository;
import com.overnetcontact.dvvs.service.criteria.SvcGroupCriteria;
import com.overnetcontact.dvvs.service.dto.SvcGroupDTO;
import com.overnetcontact.dvvs.service.mapper.SvcGroupMapper;
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
 * Service for executing complex queries for {@link SvcGroup} entities in the database.
 * The main input is a {@link SvcGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcGroupDTO} or a {@link Page} of {@link SvcGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcGroupQueryService extends QueryService<SvcGroup> {

    private final Logger log = LoggerFactory.getLogger(SvcGroupQueryService.class);

    private final SvcGroupRepository svcGroupRepository;

    private final SvcGroupMapper svcGroupMapper;

    public SvcGroupQueryService(SvcGroupRepository svcGroupRepository, SvcGroupMapper svcGroupMapper) {
        this.svcGroupRepository = svcGroupRepository;
        this.svcGroupMapper = svcGroupMapper;
    }

    /**
     * Return a {@link List} of {@link SvcGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcGroupDTO> findByCriteria(SvcGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcGroup> specification = createSpecification(criteria);
        return svcGroupMapper.toDto(svcGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcGroupDTO> findByCriteria(SvcGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcGroup> specification = createSpecification(criteria);
        return svcGroupRepository.findAll(specification, page).map(svcGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcGroup> specification = createSpecification(criteria);
        return svcGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcGroup> createSpecification(SvcGroupCriteria criteria) {
        Specification<SvcGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SvcGroup_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), SvcGroup_.description));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), SvcGroup_.address));
            }
        }
        return specification;
    }
}
