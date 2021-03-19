package com.quasarbyte.aparapi.aparapi_hello_world;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class MainSIMDInt {

    public static void main(String[] args) {

        //Core i7 4790K
        //https://ark.intel.com/content/www/ru/ru/ark/products/80807/intel-core-i7-4790k-processor-8m-cache-up-to-4-40-ghz.html
        final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_256;

        final int ARRAY_SIZE = 1024 * 1024 * 500;

        final int[] inA = new int[ARRAY_SIZE];
        final int[] inB = new int[ARRAY_SIZE];

        IntStream.range(0, ARRAY_SIZE).parallel().forEach(i -> {
            inA[i] = 1;
            inB[i] = 1;
        });

        final int[] result = new int[inA.length];

        LocalDateTime localDateTimeBegin = LocalDateTime.now();

        for (int i = 0; i < inA.length; i += SPECIES.length()) {
            var m = SPECIES.indexInRange(i, inA.length);
            // FloatVector va, vb, vc;
            var va = IntVector.fromArray(SPECIES, inA, i, m);
            var vb = IntVector.fromArray(SPECIES, inB, i, m);
            var vc = va.add(vb);
            vc.intoArray(result, i, m);
        }

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

}
