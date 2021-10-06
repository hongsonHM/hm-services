package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcPlanPartMapperTest {

    private SvcPlanPartMapper svcPlanPartMapper;

    @BeforeEach
    public void setUp() {
        svcPlanPartMapper = new SvcPlanPartMapperImpl();
    }
}
