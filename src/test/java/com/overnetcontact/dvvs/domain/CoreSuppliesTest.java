package com.overnetcontact.dvvs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.overnetcontact.dvvs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoreSuppliesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoreSupplies.class);
        CoreSupplies coreSupplies1 = new CoreSupplies();
        coreSupplies1.setId(1L);
        CoreSupplies coreSupplies2 = new CoreSupplies();
        coreSupplies2.setId(coreSupplies1.getId());
        assertThat(coreSupplies1).isEqualTo(coreSupplies2);
        coreSupplies2.setId(2L);
        assertThat(coreSupplies1).isNotEqualTo(coreSupplies2);
        coreSupplies1.setId(null);
        assertThat(coreSupplies1).isNotEqualTo(coreSupplies2);
    }
}
