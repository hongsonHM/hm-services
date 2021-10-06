package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcPlanPartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcPlanPart.class);
        SvcPlanPart svcPlanPart1 = new SvcPlanPart();
        svcPlanPart1.setId(1L);
        SvcPlanPart svcPlanPart2 = new SvcPlanPart();
        svcPlanPart2.setId(svcPlanPart1.getId());
        assertThat(svcPlanPart1).isEqualTo(svcPlanPart2);
        svcPlanPart2.setId(2L);
        assertThat(svcPlanPart1).isNotEqualTo(svcPlanPart2);
        svcPlanPart1.setId(null);
        assertThat(svcPlanPart1).isNotEqualTo(svcPlanPart2);
    }
}
