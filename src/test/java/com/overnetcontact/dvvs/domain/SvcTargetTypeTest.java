package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcTargetTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcTargetType.class);
        SvcTargetType svcTargetType1 = new SvcTargetType();
        svcTargetType1.setId(1L);
        SvcTargetType svcTargetType2 = new SvcTargetType();
        svcTargetType2.setId(svcTargetType1.getId());
        assertThat(svcTargetType1).isEqualTo(svcTargetType2);
        svcTargetType2.setId(2L);
        assertThat(svcTargetType1).isNotEqualTo(svcTargetType2);
        svcTargetType1.setId(null);
        assertThat(svcTargetType1).isNotEqualTo(svcTargetType2);
    }
}
