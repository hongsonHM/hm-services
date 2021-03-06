package com.overnetcontact.dvvs.web.rest;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.repository.CoreTaskRepository;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
import com.overnetcontact.dvvs.service.*;
import com.overnetcontact.dvvs.service.criteria.SvcContractCriteria;
import com.overnetcontact.dvvs.service.dto.*;
import com.overnetcontact.dvvs.service.mapper.SvcAreaMapper;
import com.overnetcontact.dvvs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.overnetcontact.dvvs.domain.SvcContract}.
 */
@RestController
@RequestMapping("/api")
public class SvcContractResource {

    private final Logger log = LoggerFactory.getLogger(SvcContractResource.class);

    private static final String ENTITY_NAME = "svcContract";
    private static final String DAYS = "day";
    private static final String WEEKS = "week";
    private static final String MONTHS = "month";
    private static final String YEARS = "year";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoreTaskService coreTaskService;

    private final CoreTaskRepository coreTaskRepository;

    private final SvcAreaMapper svcAreaMapper;

    private final SvcContractService svcContractService;

    private final SvcAreaService svcAreaService;

    private final SvcContractRepository svcContractRepository;

    private final SvcGroupTaskService svcGroupTaskService;

    private final SvcSpendTaskService svcSpendTaskService;

    private final SvcContractQueryService svcContractQueryService;

    public SvcContractResource(
        CoreTaskService coreTaskService,
        CoreTaskRepository coreTaskRepository,
        SvcAreaMapper svcAreaMapper,
        SvcContractService svcContractService,
        SvcAreaService svcAreaService,
        SvcContractRepository svcContractRepository,
        SvcGroupTaskService svcGroupTaskService,
        SvcSpendTaskService svcSpendTaskService,
        SvcContractQueryService svcContractQueryService
    ) {
        this.coreTaskService = coreTaskService;
        this.coreTaskRepository = coreTaskRepository;
        this.svcAreaMapper = svcAreaMapper;
        this.svcContractService = svcContractService;
        this.svcAreaService = svcAreaService;
        this.svcContractRepository = svcContractRepository;
        this.svcGroupTaskService = svcGroupTaskService;
        this.svcSpendTaskService = svcSpendTaskService;
        this.svcContractQueryService = svcContractQueryService;
    }

    /**
     * {@code POST  /svc-contracts} : Create a new svcContract.
     *
     * @param svcContractDTO the svcContractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcContractDTO, or with status {@code 400 (Bad Request)} if the svcContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/svc-contracts")
    public ResponseEntity<SvcContractDTO> createSvcContract(@Valid @RequestBody SvcContractDTO svcContractDTO) throws URISyntaxException {
        log.debug("REST request to save SvcContract : {}", svcContractDTO);
        if (svcContractDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SvcContractDTO result = svcContractService.save(svcContractDTO);
        return ResponseEntity
            .created(new URI("/api/svc-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /svc-contracts} : Create a new svcContract.
     *
     * @param file the svcContract In excel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new svcContractDTO, or with status {@code 400 (Bad Request)} if the svcContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping(value = "/svc-contracts/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> importSvcContracts(@RequestPart("file") MultipartFile file) throws URISyntaxException {
        log.debug("REST request to save SvcContract by CSV");
        if (!ExcelHelper.hasExcelFormat(file)) {
            throw new BadRequestAlertException("Invalid excel type", ENTITY_NAME, "File format not support!");
        }

        Collection<SvcContractDTO> result = svcContractService.saveByExcel(file);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Excel imported!", String.valueOf(result.size())))
            .build();
    }

    /**
     * {@code PUT  /svc-contracts/:id} : Updates an existing svcContract.
     *
     * @param id the id of the svcContractDTO to save.
     * @param svcContractDTO the svcContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcContractDTO,
     * or with status {@code 400 (Bad Request)} if the svcContractDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the svcContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/svc-contracts/{id}")
    public ResponseEntity<SvcContractDTO> updateSvcContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SvcContractDTO svcContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SvcContract : {}, {}", id, svcContractDTO);
        if (svcContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SvcContractDTO result = svcContractService.save(svcContractDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcContractDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /svc-contracts/:id} : Partial updates given fields of an existing svcContract, field will ignore if it is null
     *
     * @param id the id of the svcContractDTO to save.
     * @param svcContractDTO the svcContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated svcContractDTO,
     * or with status {@code 400 (Bad Request)} if the svcContractDTO is not valid,
     * or with status {@code 404 (Not Found)} if the svcContractDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the svcContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/svc-contracts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SvcContractDTO> partialUpdateSvcContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SvcContractDTO svcContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SvcContract partially : {}, {}", id, svcContractDTO);
        if (svcContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, svcContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!svcContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SvcContractDTO> result = svcContractService.partialUpdate(svcContractDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, svcContractDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /svc-contracts} : get all the svcContracts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of svcContracts in body.
     */
    @GetMapping("/svc-contracts")
    public ResponseEntity<List<SvcContractDTO>> getAllSvcContracts(SvcContractCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SvcContracts by criteria: {}", criteria);
        Page<SvcContractDTO> page = svcContractQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /svc-contracts/count} : count all the svcContracts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/svc-contracts/count")
    public ResponseEntity<Long> countSvcContracts(SvcContractCriteria criteria) {
        log.debug("REST request to count SvcContracts by criteria: {}", criteria);
        return ResponseEntity.ok().body(svcContractQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /svc-contracts/:id} : get the "id" svcContract.
     *
     * @param id the id of the svcContractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcContractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/svc-contracts/{id}")
    public ResponseEntity<SvcContractDTO> getSvcContract(@PathVariable Long id) {
        log.debug("REST request to get SvcContract : {}", id);
        Optional<SvcContractDTO> svcContractDTO = svcContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(svcContractDTO);
    }

    /**
     * {@code DELETE  /svc-contracts/:id} : delete the "id" svcContract.
     *
     * @param id the id of the svcContractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/svc-contracts/{id}")
    public ResponseEntity<Void> deleteSvcContract(@PathVariable Long id) {
        log.debug("REST request to delete SvcContract : {}", id);
        svcContractService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PostMapping  /full-contract} : create contract with full sub-areas.
     *
     * @param svcFullContractsDTO the object to create
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PostMapping("/full-contract")
    public ResponseEntity<String> createFullContract(@RequestBody SvcFullContractsDTO svcFullContractsDTO) throws URISyntaxException {
        log.debug("REST request to create full contracts  : {}", svcFullContractsDTO);
        SvcContractDTO svcContractDTO = svcFullContractsDTO.getSvcContractDTO();

        if (svcContractDTO.getId() != null) {
            throw new BadRequestAlertException("A new svcContract cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (svcContractDTO.getOrderNumber() == null) {
            Long totalNumber = svcContractQueryService.countByCriteria(new SvcContractCriteria());
            totalNumber++;
            svcContractDTO.setOrderNumber(totalNumber);
        }

        SvcContractDTO resultContract = svcContractService.save(svcContractDTO);
        Set<SvcSpendTaskForAreaDTO> SvcSpendTaskForAreaDTOs = svcFullContractsDTO.getSvcSpendTaskForAreaDTOs();
        Long contractsId = resultContract.getId();

        for (SvcSpendTaskForAreaDTO svcSpendTaskForAreaDTO : SvcSpendTaskForAreaDTOs) {
            SvcAreaDTO svcAreaDTO = svcSpendTaskForAreaDTO.getSvcAreaDTO();
            svcAreaDTO.setContractsId(contractsId);
            SvcAreaDTO result = svcAreaService.save(svcAreaDTO);
            SvcGroupTaskDTO svcGroupTaskDTO = new SvcGroupTaskDTO();
            svcGroupTaskDTO.setSvcArea(result);
            svcGroupTaskDTO.setName("Group task of " + result.getName());
            SvcGroupTaskDTO rsGroupTask = svcGroupTaskService.save(svcGroupTaskDTO);

            for (SvcSpendTaskDTO svcSpendTaskDTO : svcSpendTaskForAreaDTO.getSvcSpendTaskDTOs()) {
                if (svcSpendTaskDTO.getId() != null || svcSpendTaskDTO.getSvcGroupTask() != null) {
                    throw new BadRequestAlertException("A new svcSpendTask cannot already have an ID", ENTITY_NAME, "idexists");
                } else {
                    svcSpendTaskDTO.setSvcGroupTask(rsGroupTask);
                    svcSpendTaskService.save(svcSpendTaskDTO);
                }
            }
        }

        return ResponseEntity
            .created(new URI("/full-contract"))
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, ""))
            .body("Create Successfully ");
    }

    /**
     * {@code GET  /svc-contracts/:id} : get the "id" svcContract.
     *
     * @param id the id of the svcContractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the svcContractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/full-contract/{id}")
    public ResponseEntity<SvcFullContractsDTO> getFullSvcContract(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to get SvcContract : {}", id);
        SvcFullContractsDTO svcFullContractsDTO = new SvcFullContractsDTO();
        Optional<SvcContractDTO> svcContractDTO = svcContractService.findOne(id);
        svcContractDTO.ifPresent(svcFullContractsDTO::setSvcContractDTO);
        List<SvcArea> svcAreas = svcAreaService.findByContractsId(id);
        Set<SvcSpendTaskForAreaDTO> svcSpendTaskForAreaDTOs = new HashSet<>();
        for (SvcArea svcArea : svcAreas) {
            SvcSpendTaskForAreaDTO svcSpendTaskForAreaDTO = new SvcSpendTaskForAreaDTO();
            Optional<SvcGroupTask> rsGroupTask = svcGroupTaskService.findBySvcArea(svcArea);
            Set<SvcSpendTaskDTO> svcSpendTaskDTOS = svcSpendTaskService.findBySvcGroupTask(rsGroupTask.get());
            svcSpendTaskForAreaDTO.setSvcAreaDTO(svcAreaMapper.toDto(svcArea));
            svcSpendTaskForAreaDTO.setSvcSpendTaskDTOs(svcSpendTaskDTOS);
            svcSpendTaskForAreaDTOs.add(svcSpendTaskForAreaDTO);
        }
        svcFullContractsDTO.setSvcSpendTaskForAreaDTOs(svcSpendTaskForAreaDTOs);
        return ResponseEntity
            .created(new URI("/full-contract"))
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, ""))
            .body(svcFullContractsDTO);
    }

    /**
     * {@code PostMapping  /full-contract} : create contract with full sub-areas.
     *
     * @param svcFullContractsDTO the object to create
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PostMapping("/preview-supplies")
    public ResponseEntity<Collection<CoreSuppliesDTO>> previewSupplies(@RequestBody SvcFullContractsDTO svcFullContractsDTO)
        throws URISyntaxException {
        log.debug("REST request to view previewSupplies : {}", svcFullContractsDTO);
        Map<Long, Long> taskMap = new HashMap<>();
        Map<String, CoreSuppliesDTO> suppliesMap = new HashMap<>();
        Set<Long> coreTaskids = new HashSet<>();
        Instant timeFrom = svcFullContractsDTO.getSvcContractDTO().getEffectiveTimeFrom();
        Instant timeTo = svcFullContractsDTO.getSvcContractDTO().getEffectiveTimeTo();
        LocalDateTime lcFrom = LocalDateTime.ofInstant(timeFrom, ZoneOffset.UTC);
        LocalDateTime lcTo = LocalDateTime.ofInstant(timeTo, ZoneOffset.UTC);
        long dayNums = ChronoUnit.DAYS.between(lcFrom, lcTo);
        long weekNums = ChronoUnit.WEEKS.between(lcFrom, lcTo);
        long monthNums = ChronoUnit.MONTHS.between(lcFrom, lcTo);
        long yearNums = ChronoUnit.YEARS.between(lcFrom, lcTo);

        for (SvcSpendTaskForAreaDTO svcSpendTaskForAreaDTO : svcFullContractsDTO.getSvcSpendTaskForAreaDTOs()) {
            Set<SvcSpendTaskDTO> svcSpendTaskDTOs = svcSpendTaskForAreaDTO.getSvcSpendTaskDTOs();
            for (SvcSpendTaskDTO svcSpendTaskDTO : svcSpendTaskDTOs) {
                String frequency = svcSpendTaskDTO.getFrequency();
                Long totalTimesUnit = this.getTotalTimes(frequency, dayNums, weekNums, monthNums, yearNums);
                Integer mass = Integer.valueOf(svcSpendTaskDTO.getMass());
                Long totalTimes = totalTimesUnit * mass;
                Long coreTaskId = svcSpendTaskDTO.getCoreTask().getId();
                coreTaskids.add(coreTaskId);
                if (!taskMap.containsKey(coreTaskId)) {
                    taskMap.put(coreTaskId, totalTimes);
                } else {
                    taskMap.put(coreTaskId, taskMap.get(coreTaskId) + totalTimes);
                }
            }
        }
        List<CoreTask> coreTasks = coreTaskService.findByIdIn(coreTaskids);
        for (CoreTask coreTaskIndex : coreTasks) {
            Set<CoreSupplies> coreSupplies = coreTaskIndex.getCoreSupplies();
            Long times = taskMap.get(coreTaskIndex.getId());
            for (CoreSupplies coreSupply : coreSupplies) {
                Double efforts = Double.parseDouble(coreSupply.getEffort()) * times;

                if (!suppliesMap.containsKey(coreSupply.getName())) {
                    CoreSuppliesDTO coreSuppliesDTO = new CoreSuppliesDTO();
                    coreSuppliesDTO.setEffort(String.valueOf(efforts));
                    coreSuppliesDTO.setName(coreSupply.getName());
                    coreSuppliesDTO.setUnit(coreSupply.getUnit());
                    suppliesMap.put(coreSupply.getName(), coreSuppliesDTO);
                } else {
                    CoreSuppliesDTO coreSuppliesDTO = suppliesMap.get(coreSupply.getName());
                    Double currentEffort = Double.valueOf(coreSuppliesDTO.getEffort());
                    coreSuppliesDTO.setEffort(String.valueOf(currentEffort + efforts));
                    suppliesMap.put(coreSupply.getName(), coreSuppliesDTO);
                }
            }
        }
        Collection<CoreSuppliesDTO> result = suppliesMap.values();
        return ResponseEntity
            .created(new URI("/preview-supplies"))
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, ""))
            .body(result);
    }

    public Long getTotalTimes(String frequency, long dayNums, long weekNums, long monthNums, long yearNums) {
        String[] parts = frequency.split("/");
        String numbers = parts[0];
        Integer times = Integer.valueOf(numbers);
        String unit = parts[1];
        long total = 0;
        switch (unit) {
            case DAYS:
                total = times * dayNums;
                break;
            case WEEKS:
                total = times * weekNums;
                break;
            case MONTHS:
                total = times * monthNums;
                break;
            case YEARS:
                total = times * yearNums;
                break;
            default:
                break;
        }
        return total;
    }
}
