package com.ruppyrup.springclean.threading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class JobService {

    private static final Random random = new Random();

    int longRunningJob(JobRequest request) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            safeSleep();
            sum++;
            log.info("Hello from job id :: " + request.jobId() +
                    " on thread :: " + Thread.currentThread().getName() +
                    " current result = " + sum);
        }
        return sum;
    }

    private void safeSleep() {
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            log.error("Sleep interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
