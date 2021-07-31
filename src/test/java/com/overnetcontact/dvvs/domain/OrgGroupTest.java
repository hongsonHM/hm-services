package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgGroup.class);
        OrgGroup orgGroup1 = new OrgGroup();
        orgGroup1.setId(1L);
        OrgGroup orgGroup2 = new OrgGroup();
        orgGroup2.setId(orgGroup1.getId());
        assertThat(orgGroup1).isEqualTo(orgGroup2);
        orgGroup2.setId(2L);
        assertThat(orgGroup1).isNotEqualTo(orgGroup2);
        orgGroup1.setId(null);
        assertThat(orgGroup1).isNotEqualTo(orgGroup2);
    }
}
