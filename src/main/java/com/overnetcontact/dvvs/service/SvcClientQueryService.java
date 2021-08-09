package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcClient;
import com.overnetcontact.dvvs.repository.SvcClientRepository;
import com.overnetcontact.dvvs.service.criteria.SvcClientCriteria;
import com.overnetcontact.dvvs.service.dto.SvcClientDTO;
import com.overnetcontact.dvvs.service.mapper.SvcClientMapper;
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
 * Service for executing complex queries for {@link SvcClient} entities in the database.
 * The main input is a {@link SvcClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcClientDTO} or a {@link Page} of {@link SvcClientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcClientQueryService extends QueryService<SvcClient> {

    private final Logger log = LoggerFactory.getLogger(SvcClientQueryService.class);

    private final SvcClientRepository svcClientRepository;

    private final SvcClientMapper svcClientMapper;

    public SvcClientQueryService(SvcClientRepository svcClientRepository, SvcClientMapper svcClientMapper) {
        this.svcClientRepository = svcClientRepository;
        this.svcClientMapper = svcClientMapper;
    }

    /**
     * Return a {@link List} of {@link SvcClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcClientDTO> findByCriteria(SvcClientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcClient> specification = createSpecification(criteria);
        return svcClientMapper.toDto(svcClientRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcClientDTO> findByCriteria(SvcClientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcClient> specification = createSpecification(criteria);
        return svcClientRepository.findAll(specification, page).map(svcClientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcClientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcClient> specification = createSpecification(criteria);
        return svcClientRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcClient> createSpecification(SvcClientCriteria criteria) {
        Specification<SvcClient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcClient_.id));
            }
            if (criteria.getCustomerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerName(), SvcClient_.customerName));
            }
            if (criteria.getCustomerCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerCity(), SvcClient_.customerCity));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SvcClient_.phoneNumber));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), SvcClient_.type));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), SvcClient_.address));
            }
        }
        return specification;
    }
}
