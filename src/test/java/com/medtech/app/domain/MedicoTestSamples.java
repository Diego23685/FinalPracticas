package com.medtech.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Medico getMedicoSample1() {
        return new Medico()
            .id(1L)
            .nombres("nombres1")
            .apellidos("apellidos1")
            .especialidad("especialidad1")
            .licenciaMedica("licenciaMedica1")
            .telefono("telefono1");
    }

    public static Medico getMedicoSample2() {
        return new Medico()
            .id(2L)
            .nombres("nombres2")
            .apellidos("apellidos2")
            .especialidad("especialidad2")
            .licenciaMedica("licenciaMedica2")
            .telefono("telefono2");
    }

    public static Medico getMedicoRandomSampleGenerator() {
        return new Medico()
            .id(longCount.incrementAndGet())
            .nombres(UUID.randomUUID().toString())
            .apellidos(UUID.randomUUID().toString())
            .especialidad(UUID.randomUUID().toString())
            .licenciaMedica(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
