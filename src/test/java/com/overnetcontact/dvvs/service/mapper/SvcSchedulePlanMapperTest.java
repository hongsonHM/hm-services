package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SvcSchedulePlanMapperTest {

    private SvcSchedulePlanMapper svcSchedulePlanMapper;

    @BeforeEach
    public void setUp() {
        svcSchedulePlanMapper = new SvcSchedulePlanMapperImpl();
    }
}
