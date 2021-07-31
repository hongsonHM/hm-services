package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcGroupDTO.class);
        SvcGroupDTO svcGroupDTO1 = new SvcGroupDTO();
        svcGroupDTO1.setId(1L);
        SvcGroupDTO svcGroupDTO2 = new SvcGroupDTO();
        assertThat(svcGroupDTO1).isNotEqualTo(svcGroupDTO2);
        svcGroupDTO2.setId(svcGroupDTO1.getId());
        assertThat(svcGroupDTO1).isEqualTo(svcGroupDTO2);
        svcGroupDTO2.setId(2L);
        assertThat(svcGroupDTO1).isNotEqualTo(svcGroupDTO2);
        svcGroupDTO1.setId(null);
        assertThat(svcGroupDTO1).isNotEqualTo(svcGroupDTO2);
    }
}
