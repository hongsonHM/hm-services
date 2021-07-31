package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcScheduleUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcScheduleUnit.class);
        SvcScheduleUnit svcScheduleUnit1 = new SvcScheduleUnit();
        svcScheduleUnit1.setId(1L);
        SvcScheduleUnit svcScheduleUnit2 = new SvcScheduleUnit();
        svcScheduleUnit2.setId(svcScheduleUnit1.getId());
        assertThat(svcScheduleUnit1).isEqualTo(svcScheduleUnit2);
        svcScheduleUnit2.setId(2L);
        assertThat(svcScheduleUnit1).isNotEqualTo(svcScheduleUnit2);
        svcScheduleUnit1.setId(null);
        assertThat(svcScheduleUnit1).isNotEqualTo(svcScheduleUnit2);
    }
}
