package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcScheduleUnitRecordMapperTest {

    private SvcScheduleUnitRecordMapper svcScheduleUnitRecordMapper;

    @BeforeEach
    public void setUp() {
        svcScheduleUnitRecordMapper = new SvcScheduleUnitRecordMapperImpl();
    }
}
