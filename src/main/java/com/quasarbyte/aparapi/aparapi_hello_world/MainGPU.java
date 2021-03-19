package com.quasarbyte.aparapi.aparapi_hello_world;

import com.aparapi.Kernel;
import com.aparapi.Range;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class MainGPU {

    public static void main(String[] args) {

        final int ARRAY_SIZE = 1024 * 1024 * 500;

        final float[] inA = new float[ARRAY_SIZE];
        final float[] inB = new float[ARRAY_SIZE];

        IntStream.range(0, ARRAY_SIZE).forEach(i -> {
            inA[i] = i;
            inB[i] = i;
        });

        final float[] result = new float[inA.length];

        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int i = getGlobalId();
                //result[i] = inA[i] + inB[i];
                result[i] = -(inA[i] + inB[i] + (float)Math.exp(1.0) * (float)Math.exp(1.0))/inA[i];
            }
        };

        Range range = Range.create(result.length);

        LocalDateTime localDateTimeBegin = LocalDateTime.now();

        kernel.execute(range);

        LocalDateTime localDateTimeEnd = LocalDateTime.now();

        Duration duration = Duration.between(localDateTimeBegin, localDateTimeEnd);

        kernel.dispose();

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
