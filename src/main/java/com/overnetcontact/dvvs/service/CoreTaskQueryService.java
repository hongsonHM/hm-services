package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.CoreTask;
import com.overnetcontact.dvvs.repository.CoreTaskRepository;
import com.overnetcontact.dvvs.service.criteria.CoreTaskCriteria;
import com.overnetcontact.dvvs.service.dto.CoreTaskDTO;
import com.overnetcontact.dvvs.service.mapper.CoreTaskMapper;
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
 * Service for executing complex queries for {@link CoreTask} entities in the database.
 * The main input is a {@link CoreTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CoreTaskDTO} or a {@link Page} of {@link CoreTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CoreTaskQueryService extends QueryService<CoreTask> {

    private final Logger log = LoggerFactory.getLogger(CoreTaskQueryService.class);

    private final CoreTaskRepository coreTaskRepository;

    private final CoreTaskMapper coreTaskMapper;

    public CoreTaskQueryService(CoreTaskRepository coreTaskRepository, CoreTaskMapper coreTaskMapper) {
        this.coreTaskRepository = coreTaskRepository;
        this.coreTaskMapper = coreTaskMapper;
    }

    /**
     * Return a {@link List} of {@link CoreTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CoreTaskDTO> findByCriteria(CoreTaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CoreTask> specification = createSpecification(criteria);
        return coreTaskMapper.toDto(coreTaskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CoreTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CoreTaskDTO> findByCriteria(CoreTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CoreTask> specification = createSpecification(criteria);
        return coreTaskRepository.findAll(specification, page).map(coreTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CoreTaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CoreTask> specification = createSpecification(criteria);
        return coreTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link CoreTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CoreTask> createSpecification(CoreTaskCriteria criteria) {
        Specification<CoreTask> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CoreTask_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CoreTask_.name));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), CoreTask_.unit));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), CoreTask_.category));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), CoreTask_.note));
            }
            if (criteria.getCoreSuppliesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCoreSuppliesId(),
                            root -> root.join(CoreTask_.coreSupplies, JoinType.LEFT).get(CoreSupplies_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
