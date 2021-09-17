package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcPlanMapperTest {

    private SvcPlanMapper svcPlanMapper;

    @BeforeEach
    public void setUp() {
        svcPlanMapper = new SvcPlanMapperImpl();
    }
}
