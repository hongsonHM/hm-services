package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcSchedulePlanRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcSchedulePlanRecord.class);
        SvcSchedulePlanRecord svcSchedulePlanRecord1 = new SvcSchedulePlanRecord();
        svcSchedulePlanRecord1.setId(1L);
        SvcSchedulePlanRecord svcSchedulePlanRecord2 = new SvcSchedulePlanRecord();
        svcSchedulePlanRecord2.setId(svcSchedulePlanRecord1.getId());
        assertThat(svcSchedulePlanRecord1).isEqualTo(svcSchedulePlanRecord2);
        svcSchedulePlanRecord2.setId(2L);
        assertThat(svcSchedulePlanRecord1).isNotEqualTo(svcSchedulePlanRecord2);
        svcSchedulePlanRecord1.setId(null);
        assertThat(svcSchedulePlanRecord1).isNotEqualTo(svcSchedulePlanRecord2);
    }
}
