package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcLabor;
import com.overnetcontact.dvvs.repository.SvcLaborRepository;
import com.overnetcontact.dvvs.service.criteria.SvcLaborCriteria;
import com.overnetcontact.dvvs.service.dto.SvcLaborDTO;
import com.overnetcontact.dvvs.service.mapper.SvcLaborMapper;
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
 * Service for executing complex queries for {@link SvcLabor} entities in the database.
 * The main input is a {@link SvcLaborCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcLaborDTO} or a {@link Page} of {@link SvcLaborDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcLaborQueryService extends QueryService<SvcLabor> {

    private final Logger log = LoggerFactory.getLogger(SvcLaborQueryService.class);

    private final SvcLaborRepository svcLaborRepository;

    private final SvcLaborMapper svcLaborMapper;

    public SvcLaborQueryService(SvcLaborRepository svcLaborRepository, SvcLaborMapper svcLaborMapper) {
        this.svcLaborRepository = svcLaborRepository;
        this.svcLaborMapper = svcLaborMapper;
    }

    /**
     * Return a {@link List} of {@link SvcLaborDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcLaborDTO> findByCriteria(SvcLaborCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcLabor> specification = createSpecification(criteria);
        return svcLaborMapper.toDto(svcLaborRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcLaborDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcLaborDTO> findByCriteria(SvcLaborCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcLabor> specification = createSpecification(criteria);
        return svcLaborRepository.findAll(specification, page).map(svcLaborMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcLaborCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcLabor> specification = createSpecification(criteria);
        return svcLaborRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcLaborCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcLabor> createSpecification(SvcLaborCriteria criteria) {
        Specification<SvcLabor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcLabor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SvcLabor_.name));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), SvcLabor_.phone));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), SvcLabor_.address));
            }
            if (criteria.getLaborCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLaborCode(), SvcLabor_.laborCode));
            }
            if (criteria.getSvcPlanUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcPlanUnitId(),
                            root -> root.join(SvcLabor_.svcPlanUnit, JoinType.LEFT).get(SvcPlanUnit_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
