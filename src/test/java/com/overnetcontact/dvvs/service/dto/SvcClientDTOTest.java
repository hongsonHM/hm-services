package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcClientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcClientDTO.class);
        SvcClientDTO svcClientDTO1 = new SvcClientDTO();
        svcClientDTO1.setId(1L);
        SvcClientDTO svcClientDTO2 = new SvcClientDTO();
        assertThat(svcClientDTO1).isNotEqualTo(svcClientDTO2);
        svcClientDTO2.setId(svcClientDTO1.getId());
        assertThat(svcClientDTO1).isEqualTo(svcClientDTO2);
        svcClientDTO2.setId(2L);
        assertThat(svcClientDTO1).isNotEqualTo(svcClientDTO2);
        svcClientDTO1.setId(null);
        assertThat(svcClientDTO1).isNotEqualTo(svcClientDTO2);
    }
}
