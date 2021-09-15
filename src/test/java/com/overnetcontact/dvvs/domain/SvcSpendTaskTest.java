package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcSpendTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcSpendTask.class);
        SvcSpendTask svcSpendTask1 = new SvcSpendTask();
        svcSpendTask1.setId(1L);
        SvcSpendTask svcSpendTask2 = new SvcSpendTask();
        svcSpendTask2.setId(svcSpendTask1.getId());
        assertThat(svcSpendTask1).isEqualTo(svcSpendTask2);
        svcSpendTask2.setId(2L);
        assertThat(svcSpendTask1).isNotEqualTo(svcSpendTask2);
        svcSpendTask1.setId(null);
        assertThat(svcSpendTask1).isNotEqualTo(svcSpendTask2);
    }
}
