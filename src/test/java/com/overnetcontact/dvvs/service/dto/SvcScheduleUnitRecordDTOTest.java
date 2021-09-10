package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcScheduleUnitRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcScheduleUnitRecordDTO.class);
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO1 = new SvcScheduleUnitRecordDTO();
        svcScheduleUnitRecordDTO1.setId(1L);
        SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO2 = new SvcScheduleUnitRecordDTO();
        assertThat(svcScheduleUnitRecordDTO1).isNotEqualTo(svcScheduleUnitRecordDTO2);
        svcScheduleUnitRecordDTO2.setId(svcScheduleUnitRecordDTO1.getId());
        assertThat(svcScheduleUnitRecordDTO1).isEqualTo(svcScheduleUnitRecordDTO2);
        svcScheduleUnitRecordDTO2.setId(2L);
        assertThat(svcScheduleUnitRecordDTO1).isNotEqualTo(svcScheduleUnitRecordDTO2);
        svcScheduleUnitRecordDTO1.setId(null);
        assertThat(svcScheduleUnitRecordDTO1).isNotEqualTo(svcScheduleUnitRecordDTO2);
    }
}
