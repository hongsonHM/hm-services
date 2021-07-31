package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcTargetTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcTargetTypeDTO.class);
        SvcTargetTypeDTO svcTargetTypeDTO1 = new SvcTargetTypeDTO();
        svcTargetTypeDTO1.setId(1L);
        SvcTargetTypeDTO svcTargetTypeDTO2 = new SvcTargetTypeDTO();
        assertThat(svcTargetTypeDTO1).isNotEqualTo(svcTargetTypeDTO2);
        svcTargetTypeDTO2.setId(svcTargetTypeDTO1.getId());
        assertThat(svcTargetTypeDTO1).isEqualTo(svcTargetTypeDTO2);
        svcTargetTypeDTO2.setId(2L);
        assertThat(svcTargetTypeDTO1).isNotEqualTo(svcTargetTypeDTO2);
        svcTargetTypeDTO1.setId(null);
        assertThat(svcTargetTypeDTO1).isNotEqualTo(svcTargetTypeDTO2);
    }
}
