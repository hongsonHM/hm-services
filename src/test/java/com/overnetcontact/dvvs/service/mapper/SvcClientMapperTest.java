package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcClientMapperTest {

    private SvcClientMapper svcClientMapper;

    @BeforeEach
    public void setUp() {
        svcClientMapper = new SvcClientMapperImpl();
    }
}
