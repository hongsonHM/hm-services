package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcSchedulePlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcSchedulePlanDTO.class);
        SvcSchedulePlanDTO svcSchedulePlanDTO1 = new SvcSchedulePlanDTO();
        svcSchedulePlanDTO1.setId(1L);
        SvcSchedulePlanDTO svcSchedulePlanDTO2 = new SvcSchedulePlanDTO();
        assertThat(svcSchedulePlanDTO1).isNotEqualTo(svcSchedulePlanDTO2);
        svcSchedulePlanDTO2.setId(svcSchedulePlanDTO1.getId());
        assertThat(svcSchedulePlanDTO1).isEqualTo(svcSchedulePlanDTO2);
        svcSchedulePlanDTO2.setId(2L);
        assertThat(svcSchedulePlanDTO1).isNotEqualTo(svcSchedulePlanDTO2);
        svcSchedulePlanDTO1.setId(null);
        assertThat(svcSchedulePlanDTO1).isNotEqualTo(svcSchedulePlanDTO2);
    }
}
