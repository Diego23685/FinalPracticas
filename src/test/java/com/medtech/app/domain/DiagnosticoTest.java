package com.medtech.app.domain;

import static com.medtech.app.domain.CitaTestSamples.*;
import static com.medtech.app.domain.DiagnosticoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.medtech.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiagnosticoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diagnostico.class);
        Diagnostico diagnostico1 = getDiagnosticoSample1();
        Diagnostico diagnostico2 = new Diagnostico();
        assertThat(diagnostico1).isNotEqualTo(diagnostico2);

        diagnostico2.setId(diagnostico1.getId());
        assertThat(diagnostico1).isEqualTo(diagnostico2);

        diagnostico2 = getDiagnosticoSample2();
        assertThat(diagnostico1).isNotEqualTo(diagnostico2);
    }

    @Test
    void citaTest() {
        Diagnostico diagnostico = getDiagnosticoRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        diagnostico.setCita(citaBack);
        assertThat(diagnostico.getCita()).isEqualTo(citaBack);

        diagnostico.cita(null);
        assertThat(diagnostico.getCita()).isNull();
    }
}
