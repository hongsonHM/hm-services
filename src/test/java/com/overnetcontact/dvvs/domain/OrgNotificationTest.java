package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgNotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgNotification.class);
        OrgNotification orgNotification1 = new OrgNotification();
        orgNotification1.setId(1L);
        OrgNotification orgNotification2 = new OrgNotification();
        orgNotification2.setId(orgNotification1.getId());
        assertThat(orgNotification1).isEqualTo(orgNotification2);
        orgNotification2.setId(2L);
        assertThat(orgNotification1).isNotEqualTo(orgNotification2);
        orgNotification1.setId(null);
        assertThat(orgNotification1).isNotEqualTo(orgNotification2);
    }
}
