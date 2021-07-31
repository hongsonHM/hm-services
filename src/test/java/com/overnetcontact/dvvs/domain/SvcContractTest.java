package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcContract.class);
        SvcContract svcContract1 = new SvcContract();
        svcContract1.setId(1L);
        SvcContract svcContract2 = new SvcContract();
        svcContract2.setId(svcContract1.getId());
        assertThat(svcContract1).isEqualTo(svcContract2);
        svcContract2.setId(2L);
        assertThat(svcContract1).isNotEqualTo(svcContract2);
        svcContract1.setId(null);
        assertThat(svcContract1).isNotEqualTo(svcContract2);
    }
}
