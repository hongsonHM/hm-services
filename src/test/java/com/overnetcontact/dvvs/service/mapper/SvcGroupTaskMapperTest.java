package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcGroupTaskMapperTest {

    private SvcGroupTaskMapper svcGroupTaskMapper;

    @BeforeEach
    public void setUp() {
        svcGroupTaskMapper = new SvcGroupTaskMapperImpl();
    }
}
