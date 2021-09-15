package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcAreaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcAreaDTO.class);
        SvcAreaDTO svcAreaDTO1 = new SvcAreaDTO();
        svcAreaDTO1.setId(1L);
        SvcAreaDTO svcAreaDTO2 = new SvcAreaDTO();
        assertThat(svcAreaDTO1).isNotEqualTo(svcAreaDTO2);
        svcAreaDTO2.setId(svcAreaDTO1.getId());
        assertThat(svcAreaDTO1).isEqualTo(svcAreaDTO2);
        svcAreaDTO2.setId(2L);
        assertThat(svcAreaDTO1).isNotEqualTo(svcAreaDTO2);
        svcAreaDTO1.setId(null);
        assertThat(svcAreaDTO1).isNotEqualTo(svcAreaDTO2);
    }
}
