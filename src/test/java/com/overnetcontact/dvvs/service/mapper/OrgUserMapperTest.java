package com.overnetcontact.dvvs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrgUserMapperTest {

    private OrgUserMapper orgUserMapper;

    @BeforeEach
    public void setUp() {
        orgUserMapper = new OrgUserMapperImpl();
    }
}
