package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcGroupTask;
import com.overnetcontact.dvvs.repository.SvcGroupTaskRepository;
import com.overnetcontact.dvvs.service.criteria.SvcGroupTaskCriteria;
import com.overnetcontact.dvvs.service.dto.SvcGroupTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcGroupTaskMapper;
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
 * Service for executing complex queries for {@link SvcGroupTask} entities in the database.
 * The main input is a {@link SvcGroupTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcGroupTaskDTO} or a {@link Page} of {@link SvcGroupTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcGroupTaskQueryService extends QueryService<SvcGroupTask> {

    private final Logger log = LoggerFactory.getLogger(SvcGroupTaskQueryService.class);

    private final SvcGroupTaskRepository svcGroupTaskRepository;

    private final SvcGroupTaskMapper svcGroupTaskMapper;

    public SvcGroupTaskQueryService(SvcGroupTaskRepository svcGroupTaskRepository, SvcGroupTaskMapper svcGroupTaskMapper) {
        this.svcGroupTaskRepository = svcGroupTaskRepository;
        this.svcGroupTaskMapper = svcGroupTaskMapper;
    }

    /**
     * Return a {@link List} of {@link SvcGroupTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcGroupTaskDTO> findByCriteria(SvcGroupTaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcGroupTask> specification = createSpecification(criteria);
        return svcGroupTaskMapper.toDto(svcGroupTaskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcGroupTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcGroupTaskDTO> findByCriteria(SvcGroupTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcGroupTask> specification = createSpecification(criteria);
        return svcGroupTaskRepository.findAll(specification, page).map(svcGroupTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcGroupTaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcGroupTask> specification = createSpecification(criteria);
        return svcGroupTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcGroupTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcGroupTask> createSpecification(SvcGroupTaskCriteria criteria) {
        Specification<SvcGroupTask> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcGroupTask_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SvcGroupTask_.name));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SvcGroupTask_.createDate));
            }
            if (criteria.getCreateBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreateBy(), SvcGroupTask_.createBy));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), SvcGroupTask_.category));
            }
            if (criteria.getSvcAreaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcAreaId(),
                            root -> root.join(SvcGroupTask_.svcArea, JoinType.LEFT).get(SvcArea_.id)
                        )
                    );
            }
            if (criteria.getSvcSpendTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcSpendTaskId(),
                            root -> root.join(SvcGroupTask_.svcSpendTasks, JoinType.LEFT).get(SvcSpendTask_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
