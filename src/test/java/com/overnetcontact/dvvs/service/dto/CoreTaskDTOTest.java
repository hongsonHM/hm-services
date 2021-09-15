package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoreTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoreTaskDTO.class);
        CoreTaskDTO coreTaskDTO1 = new CoreTaskDTO();
        coreTaskDTO1.setId(1L);
        CoreTaskDTO coreTaskDTO2 = new CoreTaskDTO();
        assertThat(coreTaskDTO1).isNotEqualTo(coreTaskDTO2);
        coreTaskDTO2.setId(coreTaskDTO1.getId());
        assertThat(coreTaskDTO1).isEqualTo(coreTaskDTO2);
        coreTaskDTO2.setId(2L);
        assertThat(coreTaskDTO1).isNotEqualTo(coreTaskDTO2);
        coreTaskDTO1.setId(null);
        assertThat(coreTaskDTO1).isNotEqualTo(coreTaskDTO2);
    }
}
