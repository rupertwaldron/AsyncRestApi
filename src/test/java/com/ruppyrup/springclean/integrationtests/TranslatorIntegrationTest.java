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

@Import(IntegrationTestConfig.class)
@ExtendWith(LoggingExtension.class)
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
    void submitMultipleAsyncJobs() {
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

//    @Test
//    void translateInputToFrench() {
//        TranslationRequest body = new TranslationRequest();
//        body.setInput("Text to translate");
//        body.setLanguage("french");
//
//        HttpEntity<TranslationRequest> entity = new HttpEntity<>(body, headers);
//        UriComponents uriComponents = UriComponentsBuilder.newInstance()
//                .scheme("http")
//                .host("localhost")
//                .port(port)
//                .path("translate")
//                .build();
//
//        final ResponseEntity<String> response = restTemplate.postForEntity(uriComponents.toUriString(), entity, String.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//        assertThat(response.getBody(), containsString("French translating Text to translate"));
//    }
//
//  @Test
//  void translateInputToSpanish() {
//    TranslationRequest body = new TranslationRequest();
//    body.setInput("Text to translate");
//    body.setLanguage("spanish");
//
//    HttpEntity<TranslationRequest> entity = new HttpEntity<>(body, headers);
//    UriComponents uriComponents = UriComponentsBuilder.newInstance()
//                                      .scheme("http")
//                                      .host("localhost")
//                                      .port(port)
//                                      .path("translate")
//                                      .build();
//
//    final ResponseEntity<String> response = restTemplate.postForEntity(uriComponents.toUriString(), entity, String.class);
//    assertThat(response.getStatusCode(), is(HttpStatus.OK));
//    assertThat(response.getBody(), containsString("Spanish translating Text to translate"));
//  }
//
//  @Test
//  void errorWhenTranslatingInvalidLanguage() {
//    TranslationRequest body = new TranslationRequest();
//    body.setInput("Text to translate");
//    body.setLanguage("german");
//
//    HttpEntity<TranslationRequest> entity = new HttpEntity<>(body, headers);
//    UriComponents uriComponents = UriComponentsBuilder.newInstance()
//                                      .scheme("http")
//                                      .host("localhost")
//                                      .port(port)
//                                      .path("translate")
//                                      .build();
//
//    final ResponseEntity<String> response = restTemplate.postForEntity(uriComponents.toUriString(), entity, String.class);
//    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
//    assertThat(response.getBody(), containsString("Invalid language :: german"));
//  }
}
