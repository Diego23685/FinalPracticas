package com.medtech.app.domain;

import static com.medtech.app.domain.InsumoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medtech.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsumoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Insumo.class);
        Insumo insumo1 = getInsumoSample1();
        Insumo insumo2 = new Insumo();
        assertThat(insumo1).isNotEqualTo(insumo2);

        insumo2.setId(insumo1.getId());
        assertThat(insumo1).isEqualTo(insumo2);

        insumo2 = getInsumoSample2();
        assertThat(insumo1).isNotEqualTo(insumo2);
    }
}
