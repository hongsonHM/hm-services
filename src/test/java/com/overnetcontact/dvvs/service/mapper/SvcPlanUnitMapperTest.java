package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcPlanUnitMapperTest {

    private SvcPlanUnitMapper svcPlanUnitMapper;

    @BeforeEach
    public void setUp() {
        svcPlanUnitMapper = new SvcPlanUnitMapperImpl();
    }
}
