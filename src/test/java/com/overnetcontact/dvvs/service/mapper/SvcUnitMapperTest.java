package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcUnitMapperTest {

    private SvcUnitMapper svcUnitMapper;

    @BeforeEach
    public void setUp() {
        svcUnitMapper = new SvcUnitMapperImpl();
    }
}
