package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcSpendTask;
import com.overnetcontact.dvvs.repository.SvcSpendTaskRepository;
import com.overnetcontact.dvvs.service.criteria.SvcSpendTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcSpendTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcSpendTaskMapper;
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
 * Service for executing complex queries for {@link SvcSpendTask} entities in the database.
 * The main input is a {@link SvcSpendTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcSpendTaskDTO} or a {@link Page} of {@link SvcSpendTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcSpendTaskQueryService extends QueryService<SvcSpendTask> {

    private final Logger log = LoggerFactory.getLogger(SvcSpendTaskQueryService.class);

    private final SvcSpendTaskRepository svcSpendTaskRepository;

    private final SvcSpendTaskMapper svcSpendTaskMapper;

    public SvcSpendTaskQueryService(SvcSpendTaskRepository svcSpendTaskRepository, SvcSpendTaskMapper svcSpendTaskMapper) {
        this.svcSpendTaskRepository = svcSpendTaskRepository;
        this.svcSpendTaskMapper = svcSpendTaskMapper;
    }

    /**
     * Return a {@link List} of {@link SvcSpendTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcSpendTaskDTO> findByCriteria(SvcSpendTaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcSpendTask> specification = createSpecification(criteria);
        return svcSpendTaskMapper.toDto(svcSpendTaskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcSpendTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcSpendTaskDTO> findByCriteria(SvcSpendTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcSpendTask> specification = createSpecification(criteria);
        return svcSpendTaskRepository.findAll(specification, page).map(svcSpendTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcSpendTaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcSpendTask> specification = createSpecification(criteria);
        return svcSpendTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcSpendTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcSpendTask> createSpecification(SvcSpendTaskCriteria criteria) {
        Specification<SvcSpendTask> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcSpendTask_.id));
            }
            if (criteria.getCoreTaskId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoreTaskId(), SvcSpendTask_.coreTaskId));
            }
            if (criteria.getMass() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMass(), SvcSpendTask_.mass));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), SvcSpendTask_.note));
            }
            if (criteria.getSvcGroupTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcGroupTaskId(),
                            root -> root.join(SvcSpendTask_.svcGroupTask, JoinType.LEFT).get(SvcGroupTask_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
