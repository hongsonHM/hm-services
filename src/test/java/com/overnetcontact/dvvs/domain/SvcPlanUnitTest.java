package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcPlanUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcPlanUnit.class);
        SvcPlanUnit svcPlanUnit1 = new SvcPlanUnit();
        svcPlanUnit1.setId(1L);
        SvcPlanUnit svcPlanUnit2 = new SvcPlanUnit();
        svcPlanUnit2.setId(svcPlanUnit1.getId());
        assertThat(svcPlanUnit1).isEqualTo(svcPlanUnit2);
        svcPlanUnit2.setId(2L);
        assertThat(svcPlanUnit1).isNotEqualTo(svcPlanUnit2);
        svcPlanUnit1.setId(null);
        assertThat(svcPlanUnit1).isNotEqualTo(svcPlanUnit2);
    }
}
