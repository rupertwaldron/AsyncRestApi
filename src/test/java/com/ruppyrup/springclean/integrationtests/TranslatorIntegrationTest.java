package com.ruppyrup.springclean.integrationtests;


import com.ruppyrup.springclean.threading.JobRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TranslatorIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;


    @BeforeEach
    void beforeEach() {
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        headers = new HttpHeaders();
        headers.setAccept(mediaTypes);
    }


    @Test
    void submitAsyncJob() {
        int jobId = 1;
        final ResponseEntity<String> response = getStringResponseForAsyncJobRequest(jobId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Job started for id :: " + jobId);
        await().atMost(Duration.ofSeconds(20))
                .with()
                .pollInterval(Duration.ofSeconds(2))
                .until(() -> getJobStatus(jobId).getBody(), equalTo("Job with id: 1 finished with result = 10"));
    }

    @Test
    void getStatusNotFoundIfIdNotPresent() {
        int jobId = 1;

        await().atMost(Duration.ofSeconds(20))
                .with()
                .pollInterval(Duration.ofSeconds(2))
                .until(() -> getJobStatus(jobId).getBody(), equalTo("Job with id: 1 not found"));
    }

    @Test
    void submitMultipleAsyncJobsWithDifferentJobIds() {
        int jobId1 = 1;
        int jobId2 = 2;
        ResponseEntity<String> response1 = getStringResponseForAsyncJobRequest(jobId1);
        ResponseEntity<String> response2 = getStringResponseForAsyncJobRequest(jobId2);

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response1.getBody()).contains("Job started for id :: " + jobId1);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody()).contains("Job started for id :: " + jobId2);


        await().atMost(Duration.ofSeconds(20))
                .with()
                .pollInterval(Duration.ofSeconds(2))
                .until(() -> getJobStatus(jobId1).getBody(), equalTo("Job with id: 1 finished with result = 10"));

        await().atMost(Duration.ofSeconds(20))
                .with()
                .pollInterval(Duration.ofSeconds(2))
                .until(() -> getJobStatus(jobId2).getBody(), equalTo("Job with id: 2 finished with result = 10"));
    }

    @Test
    void submitMultipleAsyncJobsWithTheSameJobId() {
        int jobId = 1;
        ResponseEntity<String> response = getStringResponseForAsyncJobRequest(jobId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Job started for id :: " + jobId);

        response = getStringResponseForAsyncJobRequest(jobId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Already Processing job with id: 1");

        ResponseEntity<String> jobStatus = getJobStatus(jobId);
        System.out.println(jobStatus.getBody());

        await().atMost(Duration.ofSeconds(20))
                .with()
                .pollInterval(Duration.ofSeconds(2))
                .until(() -> getJobStatus(jobId).getBody(), equalTo("Job with id: 1 finished with result = 10"));
    }

    private ResponseEntity<String> getStringResponseForAsyncJobRequest(int jobId) {
        JobRequest jobRequest = new JobRequest(jobId);
        HttpEntity<JobRequest> entity = new HttpEntity<>(jobRequest, headers);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("threadjobs/async")
                .build();

        final ResponseEntity<String> response = restTemplate.postForEntity(uriComponents.toUriString(), entity, String.class);
        return response;
    }

    private ResponseEntity<String> getJobStatus(int jobId) {
        HttpEntity<JobRequest> entity = new HttpEntity<>(null, headers);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("threadjobs/" + jobId)
                .build();

        final ResponseEntity<String> response = restTemplate.getForEntity(uriComponents.toUriString(), String.class);
        return response;
    }
}
