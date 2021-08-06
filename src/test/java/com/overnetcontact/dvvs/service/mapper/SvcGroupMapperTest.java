package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcGroupMapperTest {

    private SvcGroupMapper svcGroupMapper;

    @BeforeEach
    public void setUp() {
        svcGroupMapper = new SvcGroupMapperImpl();
    }
}
