package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUserDTO.class);
        OrgUserDTO orgUserDTO1 = new OrgUserDTO();
        orgUserDTO1.setId(1L);
        OrgUserDTO orgUserDTO2 = new OrgUserDTO();
        assertThat(orgUserDTO1).isNotEqualTo(orgUserDTO2);
        orgUserDTO2.setId(orgUserDTO1.getId());
        assertThat(orgUserDTO1).isEqualTo(orgUserDTO2);
        orgUserDTO2.setId(2L);
        assertThat(orgUserDTO1).isNotEqualTo(orgUserDTO2);
        orgUserDTO1.setId(null);
        assertThat(orgUserDTO1).isNotEqualTo(orgUserDTO2);
    }
}
