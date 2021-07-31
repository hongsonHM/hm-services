package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUser.class);
        OrgUser orgUser1 = new OrgUser();
        orgUser1.setId(1L);
        OrgUser orgUser2 = new OrgUser();
        orgUser2.setId(orgUser1.getId());
        assertThat(orgUser1).isEqualTo(orgUser2);
        orgUser2.setId(2L);
        assertThat(orgUser1).isNotEqualTo(orgUser2);
        orgUser1.setId(null);
        assertThat(orgUser1).isNotEqualTo(orgUser2);
    }
}
