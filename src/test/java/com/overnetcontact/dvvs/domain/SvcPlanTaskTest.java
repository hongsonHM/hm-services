package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcPlanTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcPlanTask.class);
        SvcPlanTask svcPlanTask1 = new SvcPlanTask();
        svcPlanTask1.setId(1L);
        SvcPlanTask svcPlanTask2 = new SvcPlanTask();
        svcPlanTask2.setId(svcPlanTask1.getId());
        assertThat(svcPlanTask1).isEqualTo(svcPlanTask2);
        svcPlanTask2.setId(2L);
        assertThat(svcPlanTask1).isNotEqualTo(svcPlanTask2);
        svcPlanTask1.setId(null);
        assertThat(svcPlanTask1).isNotEqualTo(svcPlanTask2);
    }
}
