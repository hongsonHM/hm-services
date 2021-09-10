package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcSchedulePlanRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcSchedulePlanRecordDTO.class);
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO1 = new SvcSchedulePlanRecordDTO();
        svcSchedulePlanRecordDTO1.setId(1L);
        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO2 = new SvcSchedulePlanRecordDTO();
        assertThat(svcSchedulePlanRecordDTO1).isNotEqualTo(svcSchedulePlanRecordDTO2);
        svcSchedulePlanRecordDTO2.setId(svcSchedulePlanRecordDTO1.getId());
        assertThat(svcSchedulePlanRecordDTO1).isEqualTo(svcSchedulePlanRecordDTO2);
        svcSchedulePlanRecordDTO2.setId(2L);
        assertThat(svcSchedulePlanRecordDTO1).isNotEqualTo(svcSchedulePlanRecordDTO2);
        svcSchedulePlanRecordDTO1.setId(null);
        assertThat(svcSchedulePlanRecordDTO1).isNotEqualTo(svcSchedulePlanRecordDTO2);
    }
}
