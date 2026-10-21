package com.medtech.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Paciente getPacienteSample1() {
        return new Paciente()
            .id(1L)
            .nombres("nombres1")
            .apellidos("apellidos1")
            .identificacion("identificacion1")
            .telefono("telefono1")
            .tipoSangre("tipoSangre1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente()
            .id(2L)
            .nombres("nombres2")
            .apellidos("apellidos2")
            .identificacion("identificacion2")
            .telefono("telefono2")
            .tipoSangre("tipoSangre2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente()
            .id(longCount.incrementAndGet())
            .nombres(UUID.randomUUID().toString())
            .apellidos(UUID.randomUUID().toString())
            .identificacion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .tipoSangre(UUID.randomUUID().toString());
    }
}
