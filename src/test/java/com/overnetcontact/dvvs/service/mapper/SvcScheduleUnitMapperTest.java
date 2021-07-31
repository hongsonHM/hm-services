package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcScheduleUnitMapperTest {

    private SvcScheduleUnitMapper svcScheduleUnitMapper;

    @BeforeEach
    public void setUp() {
        svcScheduleUnitMapper = new SvcScheduleUnitMapperImpl();
    }
}
