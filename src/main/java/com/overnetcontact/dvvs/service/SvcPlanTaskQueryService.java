package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcPlanTask;
import com.overnetcontact.dvvs.repository.SvcPlanTaskRepository;
import com.overnetcontact.dvvs.service.criteria.SvcPlanTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanTaskMapper;
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
 * Service for executing complex queries for {@link SvcPlanTask} entities in the database.
 * The main input is a {@link SvcPlanTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcPlanTaskDTO} or a {@link Page} of {@link SvcPlanTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcPlanTaskQueryService extends QueryService<SvcPlanTask> {

    private final Logger log = LoggerFactory.getLogger(SvcPlanTaskQueryService.class);

    private final SvcPlanTaskRepository svcPlanTaskRepository;

    private final SvcPlanTaskMapper svcPlanTaskMapper;

    public SvcPlanTaskQueryService(SvcPlanTaskRepository svcPlanTaskRepository, SvcPlanTaskMapper svcPlanTaskMapper) {
        this.svcPlanTaskRepository = svcPlanTaskRepository;
        this.svcPlanTaskMapper = svcPlanTaskMapper;
    }

    /**
     * Return a {@link List} of {@link SvcPlanTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcPlanTaskDTO> findByCriteria(SvcPlanTaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcPlanTask> specification = createSpecification(criteria);
        return svcPlanTaskMapper.toDto(svcPlanTaskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcPlanTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcPlanTaskDTO> findByCriteria(SvcPlanTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcPlanTask> specification = createSpecification(criteria);
        return svcPlanTaskRepository.findAll(specification, page).map(svcPlanTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcPlanTaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcPlanTask> specification = createSpecification(criteria);
        return svcPlanTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcPlanTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcPlanTask> createSpecification(SvcPlanTaskCriteria criteria) {
        Specification<SvcPlanTask> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcPlanTask_.id));
            }
            if (criteria.getCoreTaskId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCoreTaskId(), SvcPlanTask_.coreTaskId));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), SvcPlanTask_.note));
            }
            if (criteria.getSvcPlanUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcPlanUnitId(),
                            root -> root.join(SvcPlanTask_.svcPlanUnit, JoinType.LEFT).get(SvcPlanUnit_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
