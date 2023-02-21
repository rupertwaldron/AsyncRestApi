package com.ruppyrup.springclean.threading;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class JobLaucher {

    private static final Map<Integer, JobExecutor> jobExecutors = new ConcurrentHashMap<>();

    @Async
    public void runAsync(int id, Supplier<Integer> supplier) {
        JobExecutor jobExecutor = new JobExecutor(id, supplier);
        jobExecutors.putIfAbsent(id, jobExecutor);
        jobExecutor.runAsync();
    }

    public Optional<JobExecutor> getExecutorById(int id) {
        return Optional.ofNullable(jobExecutors.get(id));
    }

    public void removeExecutor(int id) {
        jobExecutors.remove(id);
    }

    public static class JobExecutor {
        private int id;
        private String status;
        private Supplier<Integer> supplier;
        private Future<Integer> cf1;


        public JobExecutor(final int id, final Supplier<Integer> supplier) {
            this.id = id;
            this.supplier = supplier;
        }

        public void runAsync() {
            status = "Started";
            Integer result = supplier.get();
            cf1 = new AsyncResult<>(result);
        }

        public int getId() {
            return id;
        }

        public String getStatus() {
            if (cf1 != null) {
                if (cf1.isDone()) {
                    Integer result;
                    System.out.println("Status is finished :: " + Thread.currentThread().getName());
                    result = safeGetFuture();
                    status = "Job with id: " + id + " finished with result = " + result;
                    jobExecutors.remove(id);

                } else {
                    System.out.println("Status is running :: " + Thread.currentThread().getName());
                    status = "Job with id " + id + " is still running";
                }
            }

            return status;
        }

        private Integer safeGetFuture() {
            Integer result;
            try {
                result = cf1.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return result;
        }
    }
}
