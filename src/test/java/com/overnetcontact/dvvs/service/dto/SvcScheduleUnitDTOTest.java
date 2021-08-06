package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcScheduleUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcScheduleUnitDTO.class);
        SvcScheduleUnitDTO svcScheduleUnitDTO1 = new SvcScheduleUnitDTO();
        svcScheduleUnitDTO1.setId(1L);
        SvcScheduleUnitDTO svcScheduleUnitDTO2 = new SvcScheduleUnitDTO();
        assertThat(svcScheduleUnitDTO1).isNotEqualTo(svcScheduleUnitDTO2);
        svcScheduleUnitDTO2.setId(svcScheduleUnitDTO1.getId());
        assertThat(svcScheduleUnitDTO1).isEqualTo(svcScheduleUnitDTO2);
        svcScheduleUnitDTO2.setId(2L);
        assertThat(svcScheduleUnitDTO1).isNotEqualTo(svcScheduleUnitDTO2);
        svcScheduleUnitDTO1.setId(null);
        assertThat(svcScheduleUnitDTO1).isNotEqualTo(svcScheduleUnitDTO2);
    }
}
