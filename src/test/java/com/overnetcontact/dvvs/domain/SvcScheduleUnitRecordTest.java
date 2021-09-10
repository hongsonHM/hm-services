package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcScheduleUnitRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcScheduleUnitRecord.class);
        SvcScheduleUnitRecord svcScheduleUnitRecord1 = new SvcScheduleUnitRecord();
        svcScheduleUnitRecord1.setId(1L);
        SvcScheduleUnitRecord svcScheduleUnitRecord2 = new SvcScheduleUnitRecord();
        svcScheduleUnitRecord2.setId(svcScheduleUnitRecord1.getId());
        assertThat(svcScheduleUnitRecord1).isEqualTo(svcScheduleUnitRecord2);
        svcScheduleUnitRecord2.setId(2L);
        assertThat(svcScheduleUnitRecord1).isNotEqualTo(svcScheduleUnitRecord2);
        svcScheduleUnitRecord1.setId(null);
        assertThat(svcScheduleUnitRecord1).isNotEqualTo(svcScheduleUnitRecord2);
    }
}
