package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgNotificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgNotificationDTO.class);
        OrgNotificationDTO orgNotificationDTO1 = new OrgNotificationDTO();
        orgNotificationDTO1.setId(1L);
        OrgNotificationDTO orgNotificationDTO2 = new OrgNotificationDTO();
        assertThat(orgNotificationDTO1).isNotEqualTo(orgNotificationDTO2);
        orgNotificationDTO2.setId(orgNotificationDTO1.getId());
        assertThat(orgNotificationDTO1).isEqualTo(orgNotificationDTO2);
        orgNotificationDTO2.setId(2L);
        assertThat(orgNotificationDTO1).isNotEqualTo(orgNotificationDTO2);
        orgNotificationDTO1.setId(null);
        assertThat(orgNotificationDTO1).isNotEqualTo(orgNotificationDTO2);
    }
}
