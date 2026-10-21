package com.medtech.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medtech.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsumoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsumoDTO.class);
        InsumoDTO insumoDTO1 = new InsumoDTO();
        insumoDTO1.setId(1L);
        InsumoDTO insumoDTO2 = new InsumoDTO();
        assertThat(insumoDTO1).isNotEqualTo(insumoDTO2);
        insumoDTO2.setId(insumoDTO1.getId());
        assertThat(insumoDTO1).isEqualTo(insumoDTO2);
        insumoDTO2.setId(2L);
        assertThat(insumoDTO1).isNotEqualTo(insumoDTO2);
        insumoDTO1.setId(null);
        assertThat(insumoDTO1).isNotEqualTo(insumoDTO2);
    }
}
