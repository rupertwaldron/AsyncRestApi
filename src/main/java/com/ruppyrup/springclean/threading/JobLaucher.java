package com.ruppyrup.springclean.threading;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class JobLaucher {

    private static final Map<Integer, JobExecutor> jobExecutors = new ConcurrentHashMap<>();

    private ExecutorService executorService;

    @Async
    public void runAsync(int id, Runnable runnable) {
        JobExecutor jobExecutor = new JobExecutor(id, runnable, executorService);
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
        private Runnable runnable;
        private Future<String> cf1;
        private ExecutorService executorService;

        public JobExecutor(final int id, final Runnable runnable, ExecutorService executorService) {
            this.id = id;
            this.runnable = runnable;
            this.executorService = executorService;
        }

        //    @Async
        public void runAsync() {
            status = "Started";
            runnable.run();
            cf1 = new AsyncResult<String>("Finished");
            //        cf1 = CompletableFuture.runAsync(runnable, executorService);
        }

        public int getId() {
            return id;
        }

        public String getStatus() {
            if (cf1 != null) {
                if (cf1.isDone()) {
                    //                cf1.join();
                    System.out.println("Status is finished :: " + Thread.currentThread().getName());
                    status = "Finished";
                    jobExecutors.remove(id);

                } else {
                    System.out.println("Status is running :: " + Thread.currentThread().getName());
                    status = "Running";
                }
            }

            return status;
        }
    }
}
