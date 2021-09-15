package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcAreaMapperTest {

    private SvcAreaMapper svcAreaMapper;

    @BeforeEach
    public void setUp() {
        svcAreaMapper = new SvcAreaMapperImpl();
    }
}
