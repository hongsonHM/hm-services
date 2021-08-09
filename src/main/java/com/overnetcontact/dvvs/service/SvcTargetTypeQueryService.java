package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcTargetType;
import com.overnetcontact.dvvs.repository.SvcTargetTypeRepository;
import com.overnetcontact.dvvs.service.criteria.SvcTargetTypeCriteria;
import com.overnetcontact.dvvs.service.dto.SvcTargetTypeDTO;
import com.overnetcontact.dvvs.service.mapper.SvcTargetTypeMapper;
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
 * Service for executing complex queries for {@link SvcTargetType} entities in the database.
 * The main input is a {@link SvcTargetTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcTargetTypeDTO} or a {@link Page} of {@link SvcTargetTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcTargetTypeQueryService extends QueryService<SvcTargetType> {

    private final Logger log = LoggerFactory.getLogger(SvcTargetTypeQueryService.class);

    private final SvcTargetTypeRepository svcTargetTypeRepository;

    private final SvcTargetTypeMapper svcTargetTypeMapper;

    public SvcTargetTypeQueryService(SvcTargetTypeRepository svcTargetTypeRepository, SvcTargetTypeMapper svcTargetTypeMapper) {
        this.svcTargetTypeRepository = svcTargetTypeRepository;
        this.svcTargetTypeMapper = svcTargetTypeMapper;
    }

    /**
     * Return a {@link List} of {@link SvcTargetTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcTargetTypeDTO> findByCriteria(SvcTargetTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcTargetType> specification = createSpecification(criteria);
        return svcTargetTypeMapper.toDto(svcTargetTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcTargetTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcTargetTypeDTO> findByCriteria(SvcTargetTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcTargetType> specification = createSpecification(criteria);
        return svcTargetTypeRepository.findAll(specification, page).map(svcTargetTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcTargetTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcTargetType> specification = createSpecification(criteria);
        return svcTargetTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcTargetTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcTargetType> createSpecification(SvcTargetTypeCriteria criteria) {
        Specification<SvcTargetType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcTargetType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SvcTargetType_.name));
            }
        }
        return specification;
    }
}
