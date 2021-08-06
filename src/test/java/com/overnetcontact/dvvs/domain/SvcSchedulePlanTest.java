package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcSchedulePlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcSchedulePlan.class);
        SvcSchedulePlan svcSchedulePlan1 = new SvcSchedulePlan();
        svcSchedulePlan1.setId(1L);
        SvcSchedulePlan svcSchedulePlan2 = new SvcSchedulePlan();
        svcSchedulePlan2.setId(svcSchedulePlan1.getId());
        assertThat(svcSchedulePlan1).isEqualTo(svcSchedulePlan2);
        svcSchedulePlan2.setId(2L);
        assertThat(svcSchedulePlan1).isNotEqualTo(svcSchedulePlan2);
        svcSchedulePlan1.setId(null);
        assertThat(svcSchedulePlan1).isNotEqualTo(svcSchedulePlan2);
    }
}
