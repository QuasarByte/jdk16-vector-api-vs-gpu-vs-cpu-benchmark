package com.quasarbyte.aparapi.aparapi_hello_world;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class MainCPU {

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

        for (int i = 0; i < inA.length; i++) {
            result[i] = -(inA[i] + inB[i] + (float)Math.exp(1.0) * (float)Math.exp(1.0))/inA[i];
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
