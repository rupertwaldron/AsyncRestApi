package com.ruppyrup.springclean.threading;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/threadjobs")
public class JobController {

  private ExecutorService executorService = Executors.newFixedThreadPool(10);

  @Autowired
  JobLaucher jobLaucher;


  @GetMapping("/{id}")
  public String getJobStatus(@PathVariable int id) {
    return jobLaucher.getExecutorById(id).getStatus();
  }

  @PostMapping("/async")
  public String startJobAsync(@RequestBody JobRequest request) {
    System.out.println("Working with thread -> " + Thread.currentThread().getName());
    jobLaucher.setExecutorService(executorService);
    jobLaucher.run(request.getJobId(), () -> {
      for (int i = 0; i < 10; i++) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        System.out.println("Hello from job id :: " + request.getJobId() + " on thread :: " + Thread.currentThread().getName());
      }
    });
    return "Job started for id :: " + request.getJobId();
  }

//  @PostMapping("/sync")
//  public String startJobSync(@RequestBody JobRequest request) {
//    System.out.println("Working with thread -> " + Thread.currentThread().getName());
//    jobLaucher.setExecutorService(executorService);
//    jobLaucher.run(request.getJobId(), () -> {
//      for (int i = 0; i < 10; i++) {
//        try {
//          Thread.sleep(1000);
//        } catch (InterruptedException e) {
//          throw new RuntimeException(e);
//        }
//        System.out.println("Hello from job id :: " + request.getJobId() + " on thread :: " + Thread.currentThread().getName());
//      }
//    });
//    return "Job started for id :: " + request;
//  }
}
