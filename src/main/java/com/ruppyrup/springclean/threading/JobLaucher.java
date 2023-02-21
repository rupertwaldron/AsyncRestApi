package com.ruppyrup.springclean.threading;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class JobLaucher {

  private Map<Integer, JobExecutor> jobExecutors = new ConcurrentHashMap<>();

  private ExecutorService executorService;

  public JobExecutor runAsync(int id, Runnable runnable) {
    JobExecutor jobExecutor = new JobExecutor(id, runnable, executorService);
    jobExecutors.putIfAbsent(id, jobExecutor);
    jobExecutor.runAsync();
    return jobExecutor;
  }

  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  public JobExecutor getExecutorById(int id) {
    return jobExecutors.get(id);
  }

  public static class JobExecutor {
    private int id;
    private String status;
    private Runnable runnable;
    private CompletableFuture<Void> cf1;
    private ExecutorService executorService;

    public JobExecutor(final int id, final Runnable runnable, ExecutorService executorService) {
      this.id = id;
      this.runnable = runnable;
      this.executorService = executorService;
    }

    public void runAsync() {
      status = "Started";
      cf1 = CompletableFuture.runAsync(runnable, executorService);
    }

    public int getId() {
      return id;
    }

    public String getStatus() {
      if (cf1 != null) {
        if (cf1.isDone()) {
          cf1.join();
          System.out.println("Joining thread + " + Thread.currentThread().getName());
          status = "Finished";
        } else {
          status = "Running";
        }
      }

      return status;
    }
  }
}
