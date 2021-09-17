package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcLaborMapperTest {

    private SvcLaborMapper svcLaborMapper;

    @BeforeEach
    public void setUp() {
        svcLaborMapper = new SvcLaborMapperImpl();
    }
}
