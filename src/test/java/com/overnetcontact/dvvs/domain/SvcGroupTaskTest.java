package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcGroupTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcGroupTask.class);
        SvcGroupTask svcGroupTask1 = new SvcGroupTask();
        svcGroupTask1.setId(1L);
        SvcGroupTask svcGroupTask2 = new SvcGroupTask();
        svcGroupTask2.setId(svcGroupTask1.getId());
        assertThat(svcGroupTask1).isEqualTo(svcGroupTask2);
        svcGroupTask2.setId(2L);
        assertThat(svcGroupTask1).isNotEqualTo(svcGroupTask2);
        svcGroupTask1.setId(null);
        assertThat(svcGroupTask1).isNotEqualTo(svcGroupTask2);
    }
}
