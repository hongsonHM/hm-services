package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreTaskMapperTest {

    private CoreTaskMapper coreTaskMapper;

    @BeforeEach
    public void setUp() {
        coreTaskMapper = new CoreTaskMapperImpl();
    }
}
