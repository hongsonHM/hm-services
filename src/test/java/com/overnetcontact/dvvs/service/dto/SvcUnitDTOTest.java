package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcUnitDTO.class);
        SvcUnitDTO svcUnitDTO1 = new SvcUnitDTO();
        svcUnitDTO1.setId(1L);
        SvcUnitDTO svcUnitDTO2 = new SvcUnitDTO();
        assertThat(svcUnitDTO1).isNotEqualTo(svcUnitDTO2);
        svcUnitDTO2.setId(svcUnitDTO1.getId());
        assertThat(svcUnitDTO1).isEqualTo(svcUnitDTO2);
        svcUnitDTO2.setId(2L);
        assertThat(svcUnitDTO1).isNotEqualTo(svcUnitDTO2);
        svcUnitDTO1.setId(null);
        assertThat(svcUnitDTO1).isNotEqualTo(svcUnitDTO2);
    }
}
