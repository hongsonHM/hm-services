package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.domain.enumeration.NotificationStatus;
import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import com.overnetcontact.dvvs.repository.*;
import com.overnetcontact.dvvs.security.SecurityUtils;
import com.overnetcontact.dvvs.service.OrgNotificationService;
import com.overnetcontact.dvvs.service.SvcContractService;
import com.overnetcontact.dvvs.service.dto.SvcContractDTO;
import com.overnetcontact.dvvs.service.mapper.SvcContractMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link SvcContract}.
 */
@Service
@RequiredArgsConstructor
public class SvcContractServiceImpl implements SvcContractService {

    private final Logger log = LoggerFactory.getLogger(SvcContractServiceImpl.class);

    private final SvcContractRepository svcContractRepository;

    private final SvcContractMapper svcContractMapper;

    private final SvcUnitRepository svcUnitRepository;

    private final SvcGroupRepository svcGroupRepository;

    private final SvcClientRepository svcClientRepository;

    private final OrgNotificationRepository orgNotificationRepository;

    private final OrgUserRepository orgUserRepository;

    @Override
    public SvcContractDTO save(SvcContractDTO svcContractDTO) {
        log.debug("Request to save SvcContract : {}", svcContractDTO);
        SvcContract svcContract = svcContractMapper.toEntity(svcContractDTO);
        svcContract = svcContractRepository.save(svcContract);
        if (svcContract.getId() != null) {
            String username = SecurityUtils.getCurrentUserLogin().orElseThrow();
            OrgUser userEntity = orgUserRepository.findByInternalUser_Login(username).orElseThrow();
            svcContract.setOwnerBy(userEntity.getInternalUser());
        }
        if (svcContract.getStatus().equals(SvcContractStatus.PENDING)) {
            if (svcContract.getApprovedBy() != null && svcContract.getApprovedBy().size() > 0) {
                for (User approvedByDTO : svcContract.getApprovedBy()) {
                    OrgUser approvedBy = orgUserRepository.findByInternalUser_Id(approvedByDTO.getId()).orElseThrow();
                    OrgNotification orgNotification = new OrgNotification();
                    orgNotification.setStatus(NotificationStatus.PROCESS);
                    orgNotification.setOrgUser(approvedBy);
                    orgNotification.setIsRead(false);
                    orgNotification.setTitle("Hợp đồng cần được phê duyệt");
                    orgNotification.setDesc("Hợp đồng số \"" + svcContract.getDocumentId() + "\" cần được phê duyệt, hãy kiểm tra ngay!");
                    orgNotification.setData(
                        "{\n" + "        contract_id: '" + svcContract.getId() + "',\n" + "        type: 'contract'\n" + "      }"
                    );
                    orgNotificationRepository.save(orgNotification);
                }
            }
        }
        return svcContractMapper.toDto(svcContract);
    }

    @Override
    public Optional<SvcContractDTO> partialUpdate(SvcContractDTO svcContractDTO) {
        log.debug("Request to partially update SvcContract : {}", svcContractDTO);

        return svcContractRepository
            .findById(svcContractDTO.getId())
            .map(
                existingSvcContract -> {
                    svcContractMapper.partialUpdate(existingSvcContract, svcContractDTO);

                    return existingSvcContract;
                }
            )
            .map(svcContractRepository::save)
            .map(svcContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcContracts");
        return svcContractRepository.findAll(pageable).map(svcContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcContractDTO> findOne(Long id) {
        log.debug("Request to get SvcContract : {}", id);
        return svcContractRepository.findById(id).map(svcContractMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcContract : {}", id);
        svcContractRepository.deleteById(id);
    }

    @Override
    public Collection<SvcContractDTO> saveByExcel(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            Map<String, SvcGroup> groups = new HashMap<>();
            Map<String, SvcUnit> units = new HashMap<>();
            Map<String, SvcClient> clients = new HashMap<>();
            Map<String, SvcContract> contracts = new HashMap<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                } else {
                    rowNumber++;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                if (!cellsInRow.hasNext()) {
                    break;
                }

                SvcGroup svcGroup = new SvcGroup();
                svcGroup.setDescription("");
                SvcUnit svcUnit = new SvcUnit();
                svcUnit.setDescription("");
                SvcClient svcClient = new SvcClient();
                SvcContract svcContract = new SvcContract();
                svcContract.setValue(BigDecimal.ZERO);
                svcContract.setContractValue(BigDecimal.ZERO);
                svcContract.setValuePerPerson(BigDecimal.ZERO);
                svcContract.setStatus(SvcContractStatus.PENDING);

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    if (
                        cellIdx == 0 &&
                        currentCell.getCellType().equals(CellType.STRING) &&
                        StringUtils.isBlank(currentCell.getStringCellValue())
                    ) {
                        break;
                    }
                    switch (cellIdx) {
                        case 0:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double stt = currentCell.getNumericCellValue();
                                svcContract.setOrderNumber(stt.longValue());
                            }
                            break;
                        case 1:
                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                String documentId = currentCell.getStringCellValue();
                                svcContract.setDocumentId(documentId);
                            }
                            break;
                        case 2:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double appendicesNumber = currentCell.getNumericCellValue();
                                svcContract.setAppendicesNumber(appendicesNumber.toString());
                            }
                            break;
                        case 3:
                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                String customerName = currentCell.getStringCellValue();
                                svcClient.setCustomerName(customerName);
                                svcGroup.setName(customerName);
                                svcUnit.setName(customerName);
                            }
                            break;
                        case 4:
                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                String provinde = WordUtils.capitalizeFully(currentCell.getStringCellValue(), ' ');
                                svcClient.setCustomerCity(provinde);
                            }
                            break;
                        case 5:
                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                String address = currentCell.getStringCellValue();
                                svcClient.setAddress(address);
                                svcGroup.setAddress(address);
                            }
                            break;
                        case 6:
                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                String type = currentCell.getStringCellValue();
                                svcClient.setType(type);
                                svcGroup.setDescription(type);
                            }
                            break;
                        case 7:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Date startDate = currentCell.getDateCellValue();
                                svcContract.setEffectiveTimeFrom(startDate.toInstant());
                            }
                            break;
                        case 8:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Date endDate = currentCell.getDateCellValue();
                                svcContract.setEffectiveTimeTo(endDate.toInstant());
                            }
                            break;
                        case 9:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double duration = currentCell.getNumericCellValue();
                                svcContract.setDurationMonth(duration.intValue());
                            }
                            break;
                        case 10:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double realValue = currentCell.getNumericCellValue();
                                if (realValue != null) svcContract.setValue(BigDecimal.valueOf(realValue));
                            }
                            break;
                        case 11:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double value = currentCell.getNumericCellValue();
                                svcContract.setContractValue(BigDecimal.valueOf(value));
                            }
                            break;
                        case 12:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double humanResource = currentCell.getNumericCellValue();
                                if (humanResource != null) svcContract.setHumanResources(humanResource.intValue());
                            }
                            break;
                        case 13:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double humanResourceWeekend = currentCell.getNumericCellValue();
                                if (humanResourceWeekend != null) svcContract.setHumanResourcesWeekend(humanResourceWeekend.intValue());
                            }
                            break;
                        case 14:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double subUnit = currentCell.getNumericCellValue();
                            }
                            break;
                        case 15:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double fileId = currentCell.getNumericCellValue();
                                svcContract.setFileId(fileId.toString());
                            }
                            break;
                        case 16:
                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                String content = currentCell.getStringCellValue();
                                svcContract.setContent(content);
                            }
                            break;
                        case 17:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double targetCount = currentCell.getNumericCellValue();
                                svcContract.subjectCount(targetCount.longValue());
                            }
                            break;
                        case 18:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double pricePerHuman = currentCell.getNumericCellValue();
                                svcContract.setValuePerPerson(BigDecimal.valueOf(pricePerHuman));
                            }
                            break;
                        case 19:
                            if (currentCell.getCellType().equals(CellType.NUMERIC)) {
                                Double year = currentCell.getNumericCellValue();
                                svcContract.setYear(year.intValue());
                            }
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                SvcGroup svcGroupDB;
                if (groups.containsKey(svcGroup.getName().toLowerCase())) {
                    svcGroupDB = groups.get(svcGroup.getName().toLowerCase());
                } else {
                    svcGroupDB = svcGroupRepository.findOneByNameIgnoreCase(svcGroup.getName().toLowerCase()).orElse(svcGroup);
                    svcGroupDB.name(svcGroup.getName()).address(svcGroup.getAddress()).description(svcGroup.getDescription());
                    svcGroupRepository.saveAndFlush(svcGroupDB);
                    groups.put(svcGroup.getName().toLowerCase(), svcGroupDB);
                }

                SvcUnit svcUnitDB;
                if (units.containsKey(svcUnit.getName().toLowerCase())) {
                    svcUnitDB = units.get(svcUnit.getName().toLowerCase());
                } else {
                    svcUnitDB = svcUnitRepository.findOneByNameIgnoreCase(svcUnit.getName().toLowerCase()).orElse(svcUnit);
                    svcUnitDB.name(svcUnit.getName()).description(svcUnit.getDescription()).group(svcGroupDB);
                    svcUnitRepository.saveAndFlush(svcUnitDB);
                    units.put(svcUnit.getName().toLowerCase(), svcUnitDB);
                }

                SvcClient svcClientDB;
                if (clients.containsKey(svcClient.getCustomerName().toLowerCase() + svcClient.getAddress().toLowerCase())) {
                    svcClientDB = clients.get(svcClient.getCustomerName().toLowerCase() + svcClient.getAddress().toLowerCase());
                } else {
                    svcClientDB =
                        svcClientRepository
                            .findOneByCustomerNameIgnoreCaseAndAddressIgnoreCase(
                                svcClient.getCustomerName().toLowerCase(),
                                svcClient.getAddress().toLowerCase()
                            )
                            .orElse(svcClient);
                    svcClientDB
                        .customerName(svcClient.getCustomerName())
                        .customerCity(svcClient.getCustomerCity())
                        .address(svcClient.getAddress())
                        .phoneNumber(svcClient.getPhoneNumber() == null ? "-----------" : svcClient.getPhoneNumber())
                        .type(svcClient.getType());
                    svcClientRepository.saveAndFlush(svcClientDB);
                    clients.put(svcClient.getCustomerName().toLowerCase() + svcClient.getAddress().toLowerCase(), svcClientDB);
                }

                SvcContract svcContractDB;
                if (contracts.containsKey(svcContract.getDocumentId().toLowerCase())) {
                    svcContractDB = contracts.get(svcContract.getDocumentId().toLowerCase());
                } else {
                    svcContractDB =
                        svcContractRepository.findOneByDocumentIdIgnoreCase(svcContract.getDocumentId().toLowerCase()).orElse(svcContract);
                    svcContractDB
                        .appendicesNumber(svcContract.getAppendicesNumber())
                        .content(svcContract.getContent())
                        .contractValue(svcContract.getContractValue())
                        .documentId(svcContract.getDocumentId())
                        .durationMonth(svcContract.getDurationMonth())
                        .effectiveTimeFrom(svcContract.getEffectiveTimeFrom())
                        .effectiveTimeTo(svcContract.getEffectiveTimeTo())
                        .fileId(svcContract.getFileId())
                        .orderNumber(svcContract.getOrderNumber())
                        .humanResources(svcContract.getHumanResources())
                        .humanResourcesWeekend(svcContract.getHumanResourcesWeekend())
                        .year(svcContract.getYear())
                        .unit(svcContract.getUnit())
                        .client(svcClientDB)
                        .unit(svcUnitDB);
                    svcContractRepository.save(svcContractDB);
                    contracts.put(svcContract.getDocumentId().toLowerCase(), svcContract);
                }
            }

            workbook.close();

            return contracts.values().stream().map(svcContractMapper::toDto).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
