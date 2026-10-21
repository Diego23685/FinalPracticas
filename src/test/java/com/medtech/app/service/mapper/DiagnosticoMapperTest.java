package com.medtech.app.service.mapper;

import static com.medtech.app.domain.DiagnosticoAsserts.*;
import static com.medtech.app.domain.DiagnosticoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiagnosticoMapperTest {

    private DiagnosticoMapper diagnosticoMapper;

    @BeforeEach
    void setUp() {
        diagnosticoMapper = new DiagnosticoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDiagnosticoSample1();
        var actual = diagnosticoMapper.toEntity(diagnosticoMapper.toDto(expected));
        assertDiagnosticoAllPropertiesEquals(expected, actual);
    }
}
