package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcTarget;
import com.overnetcontact.dvvs.repository.SvcTargetRepository;
import com.overnetcontact.dvvs.service.criteria.SvcTargetCriteria;
import com.overnetcontact.dvvs.service.dto.SvcTargetDTO;
import com.overnetcontact.dvvs.service.mapper.SvcTargetMapper;
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
 * Service for executing complex queries for {@link SvcTarget} entities in the database.
 * The main input is a {@link SvcTargetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcTargetDTO} or a {@link Page} of {@link SvcTargetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcTargetQueryService extends QueryService<SvcTarget> {

    private final Logger log = LoggerFactory.getLogger(SvcTargetQueryService.class);

    private final SvcTargetRepository svcTargetRepository;

    private final SvcTargetMapper svcTargetMapper;

    public SvcTargetQueryService(SvcTargetRepository svcTargetRepository, SvcTargetMapper svcTargetMapper) {
        this.svcTargetRepository = svcTargetRepository;
        this.svcTargetMapper = svcTargetMapper;
    }

    /**
     * Return a {@link List} of {@link SvcTargetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcTargetDTO> findByCriteria(SvcTargetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcTarget> specification = createSpecification(criteria);
        return svcTargetMapper.toDto(svcTargetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcTargetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcTargetDTO> findByCriteria(SvcTargetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcTarget> specification = createSpecification(criteria);
        return svcTargetRepository.findAll(specification, page).map(svcTargetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcTargetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcTarget> specification = createSpecification(criteria);
        return svcTargetRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcTargetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcTarget> createSpecification(SvcTargetCriteria criteria) {
        Specification<SvcTarget> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcTarget_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SvcTarget_.name));
            }
            if (criteria.getChildsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getChildsId(), root -> root.join(SvcTarget_.childs, JoinType.LEFT).get(SvcTarget_.id))
                    );
            }
            if (criteria.getTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTypeId(), root -> root.join(SvcTarget_.type, JoinType.LEFT).get(SvcTargetType_.id))
                    );
            }
            if (criteria.getSvcTargetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcTargetId(),
                            root -> root.join(SvcTarget_.svcTarget, JoinType.LEFT).get(SvcTarget_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
