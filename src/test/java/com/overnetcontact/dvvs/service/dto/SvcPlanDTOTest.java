package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcPlanDTO.class);
        SvcPlanDTO svcPlanDTO1 = new SvcPlanDTO();
        svcPlanDTO1.setId(1L);
        SvcPlanDTO svcPlanDTO2 = new SvcPlanDTO();
        assertThat(svcPlanDTO1).isNotEqualTo(svcPlanDTO2);
        svcPlanDTO2.setId(svcPlanDTO1.getId());
        assertThat(svcPlanDTO1).isEqualTo(svcPlanDTO2);
        svcPlanDTO2.setId(2L);
        assertThat(svcPlanDTO1).isNotEqualTo(svcPlanDTO2);
        svcPlanDTO1.setId(null);
        assertThat(svcPlanDTO1).isNotEqualTo(svcPlanDTO2);
    }
}
