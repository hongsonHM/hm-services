package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoreTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoreTask.class);
        CoreTask coreTask1 = new CoreTask();
        coreTask1.setId(1L);
        CoreTask coreTask2 = new CoreTask();
        coreTask2.setId(coreTask1.getId());
        assertThat(coreTask1).isEqualTo(coreTask2);
        coreTask2.setId(2L);
        assertThat(coreTask1).isNotEqualTo(coreTask2);
        coreTask1.setId(null);
        assertThat(coreTask1).isNotEqualTo(coreTask2);
    }
}
