package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcSchedulePlanRecordMapperTest {

    private SvcSchedulePlanRecordMapper svcSchedulePlanRecordMapper;

    @BeforeEach
    public void setUp() {
        svcSchedulePlanRecordMapper = new SvcSchedulePlanRecordMapperImpl();
    }
}
