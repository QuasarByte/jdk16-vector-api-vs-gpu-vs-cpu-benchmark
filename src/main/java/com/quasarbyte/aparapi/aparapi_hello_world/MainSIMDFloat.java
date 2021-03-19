package com.quasarbyte.aparapi.aparapi_hello_world;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class MainSIMDFloat {

    //Core i7 4790K
    //https://ark.intel.com/content/www/ru/ru/ark/products/80807/intel-core-i7-4790k-processor-8m-cache-up-to-4-40-ghz.html
    final static VectorSpecies<Float> SPECIES = FloatVector.SPECIES_256;

    public static void main(String[] args) {

        final int ARRAY_SIZE = 1024 * 1024 * 500;

        final float[] inA = new float[ARRAY_SIZE];
        final float[] inB = new float[ARRAY_SIZE];

        IntStream.range(0, ARRAY_SIZE).parallel().forEach(i -> {
            inA[i] = 1.0f;
            inB[i] = 1.0f;
        });

        final float[] result = new float[inA.length];

        LocalDateTime localDateTimeBegin = LocalDateTime.now();

        vectorCalc(inA, inB, result);

        LocalDateTime localDateTimeEnd = LocalDateTime.now();

        Duration duration = Duration.between(localDateTimeBegin, localDateTimeEnd);

        System.out.println(
                String.format(
                        "Time spent: Hours: %d, Minutes: %d, Seconds: %d, Milliseconds: %d",
                        duration.toHoursPart(),
                        duration.toMinutesPart(),
                        duration.toSecondsPart(),
                        duration.toMillisPart())
        );

    }

    private static void vectorAdd(float[] a, float[] b, float[] c) {
        // It is assumed array arguments are of the same size
        for (int i = 0; i < SPECIES.loopBound(a.length); i += SPECIES.length()) {
            FloatVector va = FloatVector.fromArray(SPECIES, a, i);
            FloatVector vb = FloatVector.fromArray(SPECIES, b, i);
            FloatVector vc = va.add(vb);
            vc.intoArray(c, i);
        }
    }

    private static void vectorCalc(float[] a, float[] b, float[] c) {
        // It is assumed array arguments are of the same size
        for (int i = 0; i < SPECIES.loopBound(a.length); i += SPECIES.length()) {
            FloatVector va = FloatVector.fromArray(SPECIES, a, i);
            FloatVector vb = FloatVector.fromArray(SPECIES, b, i);
            FloatVector vc = va.add(vb).sqrt().abs().mul(va).neg().pow(2);
            vc.intoArray(c, i);
        }
    }

}
