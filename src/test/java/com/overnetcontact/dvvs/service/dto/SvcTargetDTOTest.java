package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcTargetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcTargetDTO.class);
        SvcTargetDTO svcTargetDTO1 = new SvcTargetDTO();
        svcTargetDTO1.setId(1L);
        SvcTargetDTO svcTargetDTO2 = new SvcTargetDTO();
        assertThat(svcTargetDTO1).isNotEqualTo(svcTargetDTO2);
        svcTargetDTO2.setId(svcTargetDTO1.getId());
        assertThat(svcTargetDTO1).isEqualTo(svcTargetDTO2);
        svcTargetDTO2.setId(2L);
        assertThat(svcTargetDTO1).isNotEqualTo(svcTargetDTO2);
        svcTargetDTO1.setId(null);
        assertThat(svcTargetDTO1).isNotEqualTo(svcTargetDTO2);
    }
}
