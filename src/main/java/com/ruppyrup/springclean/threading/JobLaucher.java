package com.ruppyrup.springclean.threading;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class JobLaucher {

    private static final Map<Integer, JobExecutor> jobExecutors = new ConcurrentHashMap<>();

    private ExecutorService executorService;

    @Async
    public void runAsync(int id, Supplier<Integer> supplier) {
        JobExecutor jobExecutor = new JobExecutor(id, supplier, executorService);
        jobExecutors.putIfAbsent(id, jobExecutor);
        jobExecutor.runAsync();
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public JobExecutor getExecutorById(int id) {
        return jobExecutors.get(id);
    }

    public void removeExecutor(int id) {
        jobExecutors.remove(id);
    }

    public static class JobExecutor {
        private int id;
        private String status;
        private Supplier<Integer> supplier;
        private Future<Integer> cf1;
        private ExecutorService executorService;


        public JobExecutor(final int id, final Supplier<Integer> supplier, ExecutorService executorService) {
            this.id = id;
            this.supplier = supplier;
            this.executorService = executorService;
        }

        //    @Async
        public void runAsync() {
            status = "Started";
            Integer result = supplier.get();
            cf1 = new AsyncResult<>(result);
            //        cf1 = CompletableFuture.runAsync(runnable, executorService);
        }

        public int getId() {
            return id;
        }

        public String getStatus() {
            if (cf1 != null) {
                if (cf1.isDone()) {
                    Integer result;
                    //                cf1.join();
                    System.out.println("Status is finished :: " + Thread.currentThread().getName());
                    try {
                        result = cf1.get(10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                    status = "Job with id: " + id + " finished with result = " + result;
                    jobExecutors.remove(id);

                } else {
                    System.out.println("Status is running :: " + Thread.currentThread().getName());
                    status = "Job with id " + id + " is still running";
                }
            }

            return status;
        }
    }
}
