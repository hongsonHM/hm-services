package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SvcContractDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SvcContractDTO.class);
        SvcContractDTO svcContractDTO1 = new SvcContractDTO();
        svcContractDTO1.setId(1L);
        SvcContractDTO svcContractDTO2 = new SvcContractDTO();
        assertThat(svcContractDTO1).isNotEqualTo(svcContractDTO2);
        svcContractDTO2.setId(svcContractDTO1.getId());
        assertThat(svcContractDTO1).isEqualTo(svcContractDTO2);
        svcContractDTO2.setId(2L);
        assertThat(svcContractDTO1).isNotEqualTo(svcContractDTO2);
        svcContractDTO1.setId(null);
        assertThat(svcContractDTO1).isNotEqualTo(svcContractDTO2);
    }
}
