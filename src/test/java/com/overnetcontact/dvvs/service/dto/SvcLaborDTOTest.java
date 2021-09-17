package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcLaborDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcLaborDTO.class);
        SvcLaborDTO svcLaborDTO1 = new SvcLaborDTO();
        svcLaborDTO1.setId(1L);
        SvcLaborDTO svcLaborDTO2 = new SvcLaborDTO();
        assertThat(svcLaborDTO1).isNotEqualTo(svcLaborDTO2);
        svcLaborDTO2.setId(svcLaborDTO1.getId());
        assertThat(svcLaborDTO1).isEqualTo(svcLaborDTO2);
        svcLaborDTO2.setId(2L);
        assertThat(svcLaborDTO1).isNotEqualTo(svcLaborDTO2);
        svcLaborDTO1.setId(null);
        assertThat(svcLaborDTO1).isNotEqualTo(svcLaborDTO2);
    }
}
