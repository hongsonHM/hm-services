package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreSuppliesMapperTest {

    private CoreSuppliesMapper coreSuppliesMapper;

    @BeforeEach
    public void setUp() {
        coreSuppliesMapper = new CoreSuppliesMapperImpl();
    }
}
