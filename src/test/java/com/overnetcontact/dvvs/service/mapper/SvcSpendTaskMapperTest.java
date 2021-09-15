package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcSpendTaskMapperTest {

    private SvcSpendTaskMapper svcSpendTaskMapper;

    @BeforeEach
    public void setUp() {
        svcSpendTaskMapper = new SvcSpendTaskMapperImpl();
    }
}
