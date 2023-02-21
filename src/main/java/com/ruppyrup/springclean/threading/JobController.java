package com.ruppyrup.springclean.threading;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/threadjobs")
public class JobController {
  private final JobService jobService;
  private final JobLaucher jobLaucher;

  public JobController(JobService jobService, JobLaucher jobLaucher) {
    this.jobService = jobService;
    this.jobLaucher = jobLaucher;
  }

  @GetMapping("/{id}")
  public String getJobStatus(@PathVariable int id) {
    return jobLaucher.getExecutorById(id).map(JobLaucher.JobExecutor::getStatus)
            .orElse("Job with id: " + id + " not found");
  }

  @PostMapping("/async")
  public String startJobAsync(@RequestBody JobRequest request) {
    int id = request.jobId();

    if (jobLaucher.getExecutorById(id).isPresent())
      return alreadyProcessingMessage(id);

    jobLaucher.runAsync(id, () -> jobService.longRunningJob(request));
    return jobStartedMessage(id);
  }

  private static String jobStartedMessage(int id) {
    return "Job started for id :: " + id;
  }

  private static String alreadyProcessingMessage(int id) {
    return "Already Processing job with id: " + id;
  }

}
