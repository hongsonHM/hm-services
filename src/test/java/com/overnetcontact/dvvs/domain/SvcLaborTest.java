package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcLaborTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcLabor.class);
        SvcLabor svcLabor1 = new SvcLabor();
        svcLabor1.setId(1L);
        SvcLabor svcLabor2 = new SvcLabor();
        svcLabor2.setId(svcLabor1.getId());
        assertThat(svcLabor1).isEqualTo(svcLabor2);
        svcLabor2.setId(2L);
        assertThat(svcLabor1).isNotEqualTo(svcLabor2);
        svcLabor1.setId(null);
        assertThat(svcLabor1).isNotEqualTo(svcLabor2);
    }
}
