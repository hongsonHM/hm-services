package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcGroup.class);
        SvcGroup svcGroup1 = new SvcGroup();
        svcGroup1.setId(1L);
        SvcGroup svcGroup2 = new SvcGroup();
        svcGroup2.setId(svcGroup1.getId());
        assertThat(svcGroup1).isEqualTo(svcGroup2);
        svcGroup2.setId(2L);
        assertThat(svcGroup1).isNotEqualTo(svcGroup2);
        svcGroup1.setId(null);
        assertThat(svcGroup1).isNotEqualTo(svcGroup2);
    }
}
