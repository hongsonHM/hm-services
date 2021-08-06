package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcTargetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcTarget.class);
        SvcTarget svcTarget1 = new SvcTarget();
        svcTarget1.setId(1L);
        SvcTarget svcTarget2 = new SvcTarget();
        svcTarget2.setId(svcTarget1.getId());
        assertThat(svcTarget1).isEqualTo(svcTarget2);
        svcTarget2.setId(2L);
        assertThat(svcTarget1).isNotEqualTo(svcTarget2);
        svcTarget1.setId(null);
        assertThat(svcTarget1).isNotEqualTo(svcTarget2);
    }
}
