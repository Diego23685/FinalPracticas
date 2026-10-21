package com.medtech.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InsumoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Insumo getInsumoSample1() {
        return new Insumo().id(1L).nombre("nombre1").codigo("codigo1").stock(1);
    }

    public static Insumo getInsumoSample2() {
        return new Insumo().id(2L).nombre("nombre2").codigo("codigo2").stock(2);
    }

    public static Insumo getInsumoRandomSampleGenerator() {
        return new Insumo()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .codigo(UUID.randomUUID().toString())
            .stock(intCount.incrementAndGet());
    }
}
