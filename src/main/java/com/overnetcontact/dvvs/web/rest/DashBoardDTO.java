package com.overnetcontact.dvvs.web.rest;

import java.util.List;
import lombok.Data;

@Data
public class DashBoardDTO {

    private Long totalContract;
    private Long totalContractWillBeEndIn3Month;
    private Long contractInDept;
    private Long contractOnHoldOrStopped;
    private Long totalUser;
    private Long totalContractNewOrStop;
    private List<Long> contractByMonth;
    private List<Long> contractByDays;
}
