package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcUnit.class);
        SvcUnit svcUnit1 = new SvcUnit();
        svcUnit1.setId(1L);
        SvcUnit svcUnit2 = new SvcUnit();
        svcUnit2.setId(svcUnit1.getId());
        assertThat(svcUnit1).isEqualTo(svcUnit2);
        svcUnit2.setId(2L);
        assertThat(svcUnit1).isNotEqualTo(svcUnit2);
        svcUnit1.setId(null);
        assertThat(svcUnit1).isNotEqualTo(svcUnit2);
    }
}
