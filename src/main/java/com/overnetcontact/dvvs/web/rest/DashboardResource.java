package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import com.overnetcontact.dvvs.repository.OrgGroupRepository;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
import com.overnetcontact.dvvs.service.OrgGroupService;
import com.overnetcontact.dvvs.service.dto.OrgGroupDTO;
import com.overnetcontact.dvvs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.OrgGroup}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DashboardResource {

    private static final String ENTITY_NAME = "orgGroup";

    private SvcContractRepository contractRepository;
    private OrgUserRepository orgUserRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code GET  /org-groups} : get all the orgGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dashboard data in body.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashBoardDTO> getAllOrgGroups() {
        log.debug("REST request to get dashboard statics");
        List<SvcContract> contracts = contractRepository.findAll();
        DashBoardDTO boardDTO = new DashBoardDTO();
        // boardDTO.setTotalContract(contracts.size().to);
        boardDTO.setTotalContractNewOrStop(
            contracts
                .stream()
                .filter(c -> c.getStatus().equals(SvcContractStatus.PENDING) || c.getStatus().equals(SvcContractStatus.WORKING))
                .count()
        );
        boardDTO.setContractOnHoldOrStopped(
            contracts
                .stream()
                .filter(c -> c.getStatus().equals(SvcContractStatus.TIMEOUT) || c.getStatus().equals(SvcContractStatus.UNREQUEST))
                .count()
        );
        // boardDTO.setTotalContractWillBeEndIn3Month(contracts.stream().filter(c -> c.getEffectiveTimeTo().isAfter(Instant.now().minus(3))));
        return ResponseEntity.ok().body(boardDTO);
    }
}
