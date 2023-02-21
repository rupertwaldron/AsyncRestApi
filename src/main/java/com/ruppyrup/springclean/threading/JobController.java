package com.ruppyrup.springclean.threading;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/threadjobs")
public class JobController {

  private final JobLaucher jobLaucher;

  public JobController(JobLaucher jobLaucher) {
    this.jobLaucher = jobLaucher;
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    this.jobLaucher.setExecutorService(executorService);
  }


  @GetMapping("/{id}")
  public String getJobStatus(@PathVariable int id) {
    return jobLaucher.getExecutorById(id).getStatus();
  }

  @PostMapping("/async")
  public String startJobAsync(@RequestBody JobRequest request) {
    System.out.println("Working with thread -> " + Thread.currentThread().getName());
    if (jobLaucher.getExecutorById(request.jobId()) != null) return "Already Processing job with id: " + request.jobId();
    jobLaucher.runAsync(request.jobId(), () -> longRunningJob(request));
    return "Job started for id :: " + request.jobId();
  }

  @PostMapping("/sync")
  public String startJobSync(@RequestBody JobRequest request) {
    System.out.println("Working with thread -> " + Thread.currentThread().getName());
    jobLaucher.runAsync(request.jobId(), () -> longRunningJob(request));
    return "Job started for id :: " + request;
  }

  private static int longRunningJob(JobRequest request) {
    int sum = 0;
    for (int i = 0; i < 10; i++) {
      safeSleep();
      sum++;
      System.out.println("Hello from job id :: " + request.jobId() + " on thread :: " + Thread.currentThread().getName() + " current result = " + sum);
    }
    return sum;
  }

  private static void safeSleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
