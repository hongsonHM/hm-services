package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcUnit;
import com.overnetcontact.dvvs.repository.SvcUnitRepository;
import com.overnetcontact.dvvs.service.criteria.SvcUnitCriteria;
import com.overnetcontact.dvvs.service.dto.SvcUnitDTO;
import com.overnetcontact.dvvs.service.mapper.SvcUnitMapper;
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
 * Service for executing complex queries for {@link SvcUnit} entities in the database.
 * The main input is a {@link SvcUnitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcUnitDTO} or a {@link Page} of {@link SvcUnitDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcUnitQueryService extends QueryService<SvcUnit> {

    private final Logger log = LoggerFactory.getLogger(SvcUnitQueryService.class);

    private final SvcUnitRepository svcUnitRepository;

    private final SvcUnitMapper svcUnitMapper;

    public SvcUnitQueryService(SvcUnitRepository svcUnitRepository, SvcUnitMapper svcUnitMapper) {
        this.svcUnitRepository = svcUnitRepository;
        this.svcUnitMapper = svcUnitMapper;
    }

    /**
     * Return a {@link List} of {@link SvcUnitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcUnitDTO> findByCriteria(SvcUnitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcUnit> specification = createSpecification(criteria);
        return svcUnitMapper.toDto(svcUnitRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcUnitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcUnitDTO> findByCriteria(SvcUnitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcUnit> specification = createSpecification(criteria);
        return svcUnitRepository.findAll(specification, page).map(svcUnitMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcUnitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcUnit> specification = createSpecification(criteria);
        return svcUnitRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcUnitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcUnit> createSpecification(SvcUnitCriteria criteria) {
        Specification<SvcUnit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcUnit_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SvcUnit_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), SvcUnit_.description));
            }
            if (criteria.getGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGroupId(), root -> root.join(SvcUnit_.group, JoinType.LEFT).get(SvcGroup_.id))
                    );
            }
        }
        return specification;
    }
}
