package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcPlanTaskMapperTest {

    private SvcPlanTaskMapper svcPlanTaskMapper;

    @BeforeEach
    public void setUp() {
        svcPlanTaskMapper = new SvcPlanTaskMapperImpl();
    }
}
