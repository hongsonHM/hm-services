package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcGroupTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcGroupTaskDTO.class);
        SvcGroupTaskDTO svcGroupTaskDTO1 = new SvcGroupTaskDTO();
        svcGroupTaskDTO1.setId(1L);
        SvcGroupTaskDTO svcGroupTaskDTO2 = new SvcGroupTaskDTO();
        assertThat(svcGroupTaskDTO1).isNotEqualTo(svcGroupTaskDTO2);
        svcGroupTaskDTO2.setId(svcGroupTaskDTO1.getId());
        assertThat(svcGroupTaskDTO1).isEqualTo(svcGroupTaskDTO2);
        svcGroupTaskDTO2.setId(2L);
        assertThat(svcGroupTaskDTO1).isNotEqualTo(svcGroupTaskDTO2);
        svcGroupTaskDTO1.setId(null);
        assertThat(svcGroupTaskDTO1).isNotEqualTo(svcGroupTaskDTO2);
    }
}
