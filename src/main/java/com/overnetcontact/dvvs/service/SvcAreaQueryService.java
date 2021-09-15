package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcArea;
import com.overnetcontact.dvvs.repository.SvcAreaRepository;
import com.overnetcontact.dvvs.service.criteria.SvcAreaCriteria;
import com.overnetcontact.dvvs.service.dto.SvcAreaDTO;
import com.overnetcontact.dvvs.service.mapper.SvcAreaMapper;
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
 * Service for executing complex queries for {@link SvcArea} entities in the database.
 * The main input is a {@link SvcAreaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcAreaDTO} or a {@link Page} of {@link SvcAreaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcAreaQueryService extends QueryService<SvcArea> {

    private final Logger log = LoggerFactory.getLogger(SvcAreaQueryService.class);

    private final SvcAreaRepository svcAreaRepository;

    private final SvcAreaMapper svcAreaMapper;

    public SvcAreaQueryService(SvcAreaRepository svcAreaRepository, SvcAreaMapper svcAreaMapper) {
        this.svcAreaRepository = svcAreaRepository;
        this.svcAreaMapper = svcAreaMapper;
    }

    /**
     * Return a {@link List} of {@link SvcAreaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcAreaDTO> findByCriteria(SvcAreaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcArea> specification = createSpecification(criteria);
        return svcAreaMapper.toDto(svcAreaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcAreaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcAreaDTO> findByCriteria(SvcAreaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcArea> specification = createSpecification(criteria);
        return svcAreaRepository.findAll(specification, page).map(svcAreaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcAreaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcArea> specification = createSpecification(criteria);
        return svcAreaRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcAreaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcArea> createSpecification(SvcAreaCriteria criteria) {
        Specification<SvcArea> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcArea_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SvcArea_.name));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), SvcArea_.location));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), SvcArea_.type));
            }
            if (criteria.getContractsId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContractsId(), SvcArea_.contractsId));
            }
            if (criteria.getSvcGroupTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcGroupTaskId(),
                            root -> root.join(SvcArea_.svcGroupTask, JoinType.LEFT).get(SvcGroupTask_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
