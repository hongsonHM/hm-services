package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcPlanPartDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcPlanPartDTO.class);
        SvcPlanPartDTO svcPlanPartDTO1 = new SvcPlanPartDTO();
        svcPlanPartDTO1.setId(1L);
        SvcPlanPartDTO svcPlanPartDTO2 = new SvcPlanPartDTO();
        assertThat(svcPlanPartDTO1).isNotEqualTo(svcPlanPartDTO2);
        svcPlanPartDTO2.setId(svcPlanPartDTO1.getId());
        assertThat(svcPlanPartDTO1).isEqualTo(svcPlanPartDTO2);
        svcPlanPartDTO2.setId(2L);
        assertThat(svcPlanPartDTO1).isNotEqualTo(svcPlanPartDTO2);
        svcPlanPartDTO1.setId(null);
        assertThat(svcPlanPartDTO1).isNotEqualTo(svcPlanPartDTO2);
    }
}
