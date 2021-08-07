package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcClient;
import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.domain.SvcGroup;
import com.overnetcontact.dvvs.domain.SvcUnit;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
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
import org.apache.poi.ss.usermodel.Cell;
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
@Transactional
public class SvcContractServiceImpl implements SvcContractService {

    private final Logger log = LoggerFactory.getLogger(SvcContractServiceImpl.class);

    private final SvcContractRepository svcContractRepository;

    private final SvcContractMapper svcContractMapper;

    public SvcContractServiceImpl(SvcContractRepository svcContractRepository, SvcContractMapper svcContractMapper) {
        this.svcContractRepository = svcContractRepository;
        this.svcContractMapper = svcContractMapper;
    }

    @Override
    public SvcContractDTO save(SvcContractDTO svcContractDTO) {
        log.debug("Request to save SvcContract : {}", svcContractDTO);
        SvcContract svcContract = svcContractMapper.toEntity(svcContractDTO);
        svcContract = svcContractRepository.save(svcContract);
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
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                SvcGroup svcGroup = new SvcGroup();
                SvcUnit svcUnit = new SvcUnit();
                SvcClient svcClient = new SvcClient();
                SvcContract svcContract = new SvcContract();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            Double stt = currentCell.getNumericCellValue();
                            svcContract.setOrderNumber(stt.longValue());
                            break;
                        case 1:
                            String documentId = currentCell.getStringCellValue();
                            svcContract.setDocumentId(documentId);
                            break;
                        case 2:
                            String appendicesNumber = currentCell.getStringCellValue();
                            svcContract.setAppendicesNumber(appendicesNumber);
                            break;
                        case 3:
                            String customerName = currentCell.getStringCellValue();
                            svcClient.setCustomerName(customerName);
                            svcGroup.name(customerName);
                            svcUnit.setName(customerName);
                            break;
                        case 4:
                            String provinde = currentCell.getStringCellValue();
                            svcClient.setCustomerCity(provinde);
                            break;
                        case 5:
                            String address = currentCell.getStringCellValue();
                            svcClient.setAddress(address);
                            svcGroup.setAddress(address);
                            break;
                        case 6:
                            String type = currentCell.getStringCellValue();
                            svcClient.setType(type);
                            svcGroup.setDescription(type);
                            break;
                        case 7:
                            Date startDate = currentCell.getDateCellValue();
                            svcContract.setEffectiveTimeFrom(startDate.toInstant());
                            break;
                        case 8:
                            Date endDate = currentCell.getDateCellValue();
                            svcContract.setEffectiveTimeTo(endDate.toInstant());
                            break;
                        case 9:
                            Double duration = currentCell.getNumericCellValue();
                            svcContract.setDurationMonth(duration.intValue());
                            break;
                        case 10:
                            Double realValue = currentCell.getNumericCellValue();
                            if (realValue != null) svcContract.setValue(BigDecimal.valueOf(realValue));
                            break;
                        case 11:
                            Double value = currentCell.getNumericCellValue();
                            svcContract.setContractValue(BigDecimal.valueOf(value));
                            break;
                        case 12:
                            Double humanResource = currentCell.getNumericCellValue();
                            if (humanResource != null) svcContract.setHumanResources(humanResource.intValue());
                            break;
                        case 13:
                            Double humanResourceWeekend = currentCell.getNumericCellValue();
                            if (humanResourceWeekend != null) svcContract.setHumanResourcesWeekend(humanResourceWeekend.intValue());
                            break;
                        case 14:
                            Double subUnit = currentCell.getNumericCellValue();
                            break;
                        case 15:
                            String fileId = currentCell.getStringCellValue();
                            svcContract.setFileId(fileId);
                            break;
                        case 16:
                            String content = currentCell.getStringCellValue();
                            svcContract.setContent(content);
                            break;
                        case 17:
                            Double targetCount = currentCell.getNumericCellValue();
                            svcContract.subjectCount(targetCount.longValue());
                            break;
                        case 18:
                            Double pricePerHuman = currentCell.getNumericCellValue();
                            svcContract.setValuePerPerson(BigDecimal.valueOf(pricePerHuman));
                            break;
                        case 19:
                            Double year = currentCell.getNumericCellValue();
                            svcContract.setYear(year.intValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                groups.put(svcGroup.getName().toLowerCase(), svcGroup);
            }

            workbook.close();

            return contracts.values().stream().map(svcContractMapper::toDto).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
