package com.ruppyrup.springclean.threading;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/threadjobs")
public class JobController {

  private static Random random = new Random();

  private final JobLaucher jobLaucher;

  public JobController(JobLaucher jobLaucher) {
    this.jobLaucher = jobLaucher;
  }


  @GetMapping("/{id}")
  public String getJobStatus(@PathVariable int id) {
    return jobLaucher.getExecutorById(id).map(JobLaucher.JobExecutor::getStatus).orElse("Job with id: " + id + " not found");
  }

  @PostMapping("/async")
  public String startJobAsync(@RequestBody JobRequest request) {
    if (jobLaucher.getExecutorById(request.jobId()).isPresent())
      return "Already Processing job with id: " + request.jobId();

    jobLaucher.runAsync(request.jobId(), () -> longRunningJob(request));
    return "Job started for id :: " + request.jobId();
  }

  @PostMapping("/sync")
  public String startJobSync(@RequestBody JobRequest request) {
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
      Thread.sleep(random.nextInt(1000));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
