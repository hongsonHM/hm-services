package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcSpendTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcSpendTaskDTO.class);
        SvcSpendTaskDTO svcSpendTaskDTO1 = new SvcSpendTaskDTO();
        svcSpendTaskDTO1.setId(1L);
        SvcSpendTaskDTO svcSpendTaskDTO2 = new SvcSpendTaskDTO();
        assertThat(svcSpendTaskDTO1).isNotEqualTo(svcSpendTaskDTO2);
        svcSpendTaskDTO2.setId(svcSpendTaskDTO1.getId());
        assertThat(svcSpendTaskDTO1).isEqualTo(svcSpendTaskDTO2);
        svcSpendTaskDTO2.setId(2L);
        assertThat(svcSpendTaskDTO1).isNotEqualTo(svcSpendTaskDTO2);
        svcSpendTaskDTO1.setId(null);
        assertThat(svcSpendTaskDTO1).isNotEqualTo(svcSpendTaskDTO2);
    }
}
