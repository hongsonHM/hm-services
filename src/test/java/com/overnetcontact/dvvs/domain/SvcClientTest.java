package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcClient.class);
        SvcClient svcClient1 = new SvcClient();
        svcClient1.setId(1L);
        SvcClient svcClient2 = new SvcClient();
        svcClient2.setId(svcClient1.getId());
        assertThat(svcClient1).isEqualTo(svcClient2);
        svcClient2.setId(2L);
        assertThat(svcClient1).isNotEqualTo(svcClient2);
        svcClient1.setId(null);
        assertThat(svcClient1).isNotEqualTo(svcClient2);
    }
}
