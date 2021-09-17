package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcPlan.class);
        SvcPlan svcPlan1 = new SvcPlan();
        svcPlan1.setId(1L);
        SvcPlan svcPlan2 = new SvcPlan();
        svcPlan2.setId(svcPlan1.getId());
        assertThat(svcPlan1).isEqualTo(svcPlan2);
        svcPlan2.setId(2L);
        assertThat(svcPlan1).isNotEqualTo(svcPlan2);
        svcPlan1.setId(null);
        assertThat(svcPlan1).isNotEqualTo(svcPlan2);
    }
}
