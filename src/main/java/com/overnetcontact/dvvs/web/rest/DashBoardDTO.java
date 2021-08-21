package com.overnetcontact.dvvs.web.rest;

import java.util.Map;
import lombok.Data;

@Data
public class DashBoardDTO {

    private int totalContract;
    private int totalContractWillBeEnd = 0;
    private Long contractInDept = 0L;
    private Long contractOnHoldOrStopped = 0L;
    private Long totalUser = 0L;
    private Long totalContractNew = 0L;
    private Long revenue = 0L;
    private Long totalHumanResources = 0L;

    private int totalContractLastMonth;
    private int totalContractWillBeEndLastMonth = 0;
    private Long contractInDeptLastMonth = 0L;
    private Long contractOnHoldOrStoppedLastMonth = 0L;
    private Long totalUserLastMonth = 0L;
    private Long totalContractNewLastMonth = 0L;
    private Long revenueLastMonth = 0L;
    private Long totalHumanResourcesLastMonth = 0L;

    private int totalContractLastQuarter;
    private int totalContractWillBeEndLastQuarter = 0;
    private Long contractInDeptLastQuarter = 0L;
    private Long contractOnHoldOrStoppedLastQuarter = 0L;
    private Long totalUserLastQuarter = 0L;
    private Long totalContractNewLastQuarter = 0L;
    private Long revenueLastQuarter = 0L;
    private Long totalHumanResourcesLastQuarter = 0L;

    private Map<String, Long> contractNewByMonths;
    private Map<String, Long> contractNewByQuarter;
    private Map<String, Long> contractDoneByMonths;
    private Map<String, Long> contractDoneByQuarter;
}
