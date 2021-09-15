package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcAreaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcArea.class);
        SvcArea svcArea1 = new SvcArea();
        svcArea1.setId(1L);
        SvcArea svcArea2 = new SvcArea();
        svcArea2.setId(svcArea1.getId());
        assertThat(svcArea1).isEqualTo(svcArea2);
        svcArea2.setId(2L);
        assertThat(svcArea1).isNotEqualTo(svcArea2);
        svcArea1.setId(null);
        assertThat(svcArea1).isNotEqualTo(svcArea2);
    }
}
