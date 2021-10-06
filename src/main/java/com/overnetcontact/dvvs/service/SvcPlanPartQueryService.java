package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcPlanPart;
import com.overnetcontact.dvvs.repository.SvcPlanPartRepository;
import com.overnetcontact.dvvs.service.criteria.SvcPlanPartCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanPartDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanPartMapper;
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
 * Service for executing complex queries for {@link SvcPlanPart} entities in the database.
 * The main input is a {@link SvcPlanPartCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcPlanPartDTO} or a {@link Page} of {@link SvcPlanPartDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcPlanPartQueryService extends QueryService<SvcPlanPart> {

    private final Logger log = LoggerFactory.getLogger(SvcPlanPartQueryService.class);

    private final SvcPlanPartRepository svcPlanPartRepository;

    private final SvcPlanPartMapper svcPlanPartMapper;

    public SvcPlanPartQueryService(SvcPlanPartRepository svcPlanPartRepository, SvcPlanPartMapper svcPlanPartMapper) {
        this.svcPlanPartRepository = svcPlanPartRepository;
        this.svcPlanPartMapper = svcPlanPartMapper;
    }

    /**
     * Return a {@link List} of {@link SvcPlanPartDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcPlanPartDTO> findByCriteria(SvcPlanPartCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcPlanPart> specification = createSpecification(criteria);
        return svcPlanPartMapper.toDto(svcPlanPartRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcPlanPartDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcPlanPartDTO> findByCriteria(SvcPlanPartCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcPlanPart> specification = createSpecification(criteria);
        return svcPlanPartRepository.findAll(specification, page).map(svcPlanPartMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcPlanPartCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcPlanPart> specification = createSpecification(criteria);
        return svcPlanPartRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcPlanPartCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcPlanPart> createSpecification(SvcPlanPartCriteria criteria) {
        Specification<SvcPlanPart> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcPlanPart_.id));
            }
            if (criteria.getPlanUnitID() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlanUnitID(), SvcPlanPart_.planUnitID));
            }
            if (criteria.getSpendTaskID() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSpendTaskID(), SvcPlanPart_.spendTaskID));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), SvcPlanPart_.location));
            }
            if (criteria.getStartAt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStartAt(), SvcPlanPart_.startAt));
            }
            if (criteria.getEndAt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndAt(), SvcPlanPart_.endAt));
            }
            if (criteria.getFrequency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFrequency(), SvcPlanPart_.frequency));
            }
            if (criteria.getPeriodic() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodic(), SvcPlanPart_.periodic));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), SvcPlanPart_.note));
            }
            if (criteria.getWorkOnDays() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWorkOnDays(), SvcPlanPart_.workOnDays));
            }
        }
        return specification;
    }
}
