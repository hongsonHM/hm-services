package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcPlanUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcPlanUnitDTO.class);
        SvcPlanUnitDTO svcPlanUnitDTO1 = new SvcPlanUnitDTO();
        svcPlanUnitDTO1.setId(1L);
        SvcPlanUnitDTO svcPlanUnitDTO2 = new SvcPlanUnitDTO();
        assertThat(svcPlanUnitDTO1).isNotEqualTo(svcPlanUnitDTO2);
        svcPlanUnitDTO2.setId(svcPlanUnitDTO1.getId());
        assertThat(svcPlanUnitDTO1).isEqualTo(svcPlanUnitDTO2);
        svcPlanUnitDTO2.setId(2L);
        assertThat(svcPlanUnitDTO1).isNotEqualTo(svcPlanUnitDTO2);
        svcPlanUnitDTO1.setId(null);
        assertThat(svcPlanUnitDTO1).isNotEqualTo(svcPlanUnitDTO2);
    }
}
