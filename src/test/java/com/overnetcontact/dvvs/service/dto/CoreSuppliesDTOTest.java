package com.overnetcontact.dvvs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoreSuppliesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoreSuppliesDTO.class);
        CoreSuppliesDTO coreSuppliesDTO1 = new CoreSuppliesDTO();
        coreSuppliesDTO1.setId(1L);
        CoreSuppliesDTO coreSuppliesDTO2 = new CoreSuppliesDTO();
        assertThat(coreSuppliesDTO1).isNotEqualTo(coreSuppliesDTO2);
        coreSuppliesDTO2.setId(coreSuppliesDTO1.getId());
        assertThat(coreSuppliesDTO1).isEqualTo(coreSuppliesDTO2);
        coreSuppliesDTO2.setId(2L);
        assertThat(coreSuppliesDTO1).isNotEqualTo(coreSuppliesDTO2);
        coreSuppliesDTO1.setId(null);
        assertThat(coreSuppliesDTO1).isNotEqualTo(coreSuppliesDTO2);
    }
}
