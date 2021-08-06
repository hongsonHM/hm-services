package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcTargetMapperTest {

    private SvcTargetMapper svcTargetMapper;

    @BeforeEach
    public void setUp() {
        svcTargetMapper = new SvcTargetMapperImpl();
    }
}
