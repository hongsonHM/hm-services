package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.CoreSupplies;
import com.overnetcontact.dvvs.repository.CoreSuppliesRepository;
import com.overnetcontact.dvvs.service.criteria.CoreSuppliesCriteria;
import com.overnetcontact.dvvs.service.dto.CoreSuppliesDTO;
import com.overnetcontact.dvvs.service.mapper.CoreSuppliesMapper;
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
 * Service for executing complex queries for {@link CoreSupplies} entities in the database.
 * The main input is a {@link CoreSuppliesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CoreSuppliesDTO} or a {@link Page} of {@link CoreSuppliesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CoreSuppliesQueryService extends QueryService<CoreSupplies> {

    private final Logger log = LoggerFactory.getLogger(CoreSuppliesQueryService.class);

    private final CoreSuppliesRepository coreSuppliesRepository;

    private final CoreSuppliesMapper coreSuppliesMapper;

    public CoreSuppliesQueryService(CoreSuppliesRepository coreSuppliesRepository, CoreSuppliesMapper coreSuppliesMapper) {
        this.coreSuppliesRepository = coreSuppliesRepository;
        this.coreSuppliesMapper = coreSuppliesMapper;
    }

    /**
     * Return a {@link List} of {@link CoreSuppliesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CoreSuppliesDTO> findByCriteria(CoreSuppliesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CoreSupplies> specification = createSpecification(criteria);
        return coreSuppliesMapper.toDto(coreSuppliesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CoreSuppliesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CoreSuppliesDTO> findByCriteria(CoreSuppliesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CoreSupplies> specification = createSpecification(criteria);
        return coreSuppliesRepository.findAll(specification, page).map(coreSuppliesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CoreSuppliesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CoreSupplies> specification = createSpecification(criteria);
        return coreSuppliesRepository.count(specification);
    }

    /**
     * Function to convert {@link CoreSuppliesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CoreSupplies> createSpecification(CoreSuppliesCriteria criteria) {
        Specification<CoreSupplies> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CoreSupplies_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CoreSupplies_.name));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), CoreSupplies_.unit));
            }
            if (criteria.getEffort() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEffort(), CoreSupplies_.effort));
            }
            if (criteria.getCoreTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCoreTaskId(),
                            root -> root.join(CoreSupplies_.coreTasks, JoinType.LEFT).get(CoreTask_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
