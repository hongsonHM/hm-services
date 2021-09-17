package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcPlanTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcPlanTaskDTO.class);
        SvcPlanTaskDTO svcPlanTaskDTO1 = new SvcPlanTaskDTO();
        svcPlanTaskDTO1.setId(1L);
        SvcPlanTaskDTO svcPlanTaskDTO2 = new SvcPlanTaskDTO();
        assertThat(svcPlanTaskDTO1).isNotEqualTo(svcPlanTaskDTO2);
        svcPlanTaskDTO2.setId(svcPlanTaskDTO1.getId());
        assertThat(svcPlanTaskDTO1).isEqualTo(svcPlanTaskDTO2);
        svcPlanTaskDTO2.setId(2L);
        assertThat(svcPlanTaskDTO1).isNotEqualTo(svcPlanTaskDTO2);
        svcPlanTaskDTO1.setId(null);
        assertThat(svcPlanTaskDTO1).isNotEqualTo(svcPlanTaskDTO2);
    }
}
