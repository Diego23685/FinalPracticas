package com.medtech.app.service.mapper;

import static com.medtech.app.domain.InsumoAsserts.*;
import static com.medtech.app.domain.InsumoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsumoMapperTest {

    private InsumoMapper insumoMapper;

    @BeforeEach
    void setUp() {
        insumoMapper = new InsumoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInsumoSample1();
        var actual = insumoMapper.toEntity(insumoMapper.toDto(expected));
        assertInsumoAllPropertiesEquals(expected, actual);
    }
}
