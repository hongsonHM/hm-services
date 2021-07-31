package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcContractMapperTest {

    private SvcContractMapper svcContractMapper;

    @BeforeEach
    public void setUp() {
        svcContractMapper = new SvcContractMapperImpl();
    }
}
