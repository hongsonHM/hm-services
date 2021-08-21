package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.OrgGroup}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DashboardResource {

    private static final String ENTITY_NAME = "orgGroup";

    private final SvcContractRepository contractRepository;
    private final OrgUserRepository orgUserRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code GET  /org-groups} : get all the orgGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dashboard data in body.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashBoardDTO> getAllOrgGroupsQuater() {
        log.debug("REST request to get dashboard statics");
        // month
        List<SvcContract> contracts = contractRepository.findAll();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.of("+07:00"));
        DashBoardDTO board = new DashBoardDTO();
        board.setTotalContract(contracts.size());
        board.setTotalContractWillBeEnd(
            contractRepository.findWhatWillEnd(Instant.now().plus(Duration.ofDays(30)), SvcContractStatus.WORKING).size()
        );

        Map<String, Long> contractNewByMonths = new HashMap<>();
        Map<String, Long> contractNewByQuarter = new HashMap<>();
        Map<String, Long> contractExpiredByMonths = new HashMap<>();
        Map<String, Long> contractExpiredByQuarter = new HashMap<>();
        for (SvcContract contract : contracts) {
            String dayOfYearFrom = contract
                .getEffectiveTimeFrom()
                .atZone(ZoneOffset.of("+07:00"))
                .format(DateTimeFormatter.ofPattern("MM-yyyy"));
            long quarterFrom = contract.getEffectiveTimeFrom().atZone(ZoneOffset.of("+07:00")).get(IsoFields.QUARTER_OF_YEAR);
            long yearFrom = contract.getEffectiveTimeFrom().atZone(ZoneOffset.of("+07:00")).getYear();
            String quarterFromStr = quarterFrom + "-" + yearFrom;

            String dayOfYearTo = contract
                .getEffectiveTimeTo()
                .atZone(ZoneOffset.of("+07:00"))
                .format(DateTimeFormatter.ofPattern("MM-yyyy"));
            long quarterTo = contract.getEffectiveTimeTo().atZone(ZoneOffset.of("+07:00")).get(IsoFields.QUARTER_OF_YEAR);
            long yearTo = contract.getEffectiveTimeTo().atZone(ZoneOffset.of("+07:00")).getYear();
            String quarterToStr = quarterTo + "-" + yearTo;

            if (!contract.getStatus().equals(SvcContractStatus.TIMEOUT) && !contract.getStatus().equals(SvcContractStatus.UNREQUEST)) {
                // contract new
                if (contractNewByMonths.containsKey(dayOfYearFrom)) {
                    contractNewByMonths.replace(dayOfYearFrom, contractNewByMonths.get(dayOfYearFrom) + 1);
                } else {
                    contractNewByMonths.put(dayOfYearFrom, 1L);
                }
                if (contractNewByQuarter.containsKey(quarterFromStr)) {
                    contractNewByQuarter.replace(quarterFromStr, contractNewByQuarter.get(quarterFromStr) + 1);
                } else {
                    contractNewByQuarter.put(quarterFromStr, 1L);
                }
                // contract expired
                if (contractExpiredByMonths.containsKey(dayOfYearTo)) {
                    contractExpiredByMonths.replace(dayOfYearTo, contractExpiredByMonths.get(dayOfYearTo) + 1);
                } else {
                    contractExpiredByMonths.put(dayOfYearTo, 1L);
                }
                if (contractExpiredByQuarter.containsKey(quarterToStr)) {
                    contractExpiredByQuarter.replace(quarterToStr, contractExpiredByQuarter.get(quarterToStr) + 1);
                } else {
                    contractExpiredByQuarter.put(quarterToStr, 1L);
                }
            }

            if (contract.getStatus().equals(SvcContractStatus.SUCCESS)) {
                board.setContractOnHoldOrStopped(board.getContractOnHoldOrStopped() + 1);
                board.setRevenue(board.getRevenue() + contract.getValue().longValue());
            }
            if (contract.getStatus().equals(SvcContractStatus.WORKING)) {
                board.setTotalContractNew(board.getTotalContractNew() + 1);
                board.setTotalHumanResources(board.getTotalHumanResources() + contract.getHumanResources());
                board.setRevenue(board.getRevenue() + contract.getValue().longValue());
            }
        }
        board.setContractDoneByQuarter(contractExpiredByQuarter);
        board.setContractDoneByMonths(contractExpiredByMonths);
        board.setContractNewByQuarter(contractNewByQuarter);
        board.setContractNewByMonths(contractNewByMonths);

        return ResponseEntity.ok().body(board);
    }
}
