package com.ruppyrup.springclean.integrationtests;


import com.ruppyrup.springclean.dto.TranslationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

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
    void translateInputToFrench() {
        TranslationRequest body = new TranslationRequest();
        body.setInput("Text to translate");
        body.setLanguage("french");

        HttpEntity<TranslationRequest> entity = new HttpEntity<>(body, headers);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("translate")
                .build();

        final ResponseEntity<String> response = restTemplate.postForEntity(uriComponents.toUriString(), entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), containsString("French translating Text to translate"));
    }

  @Test
  void translateInputToSpanish() {
    TranslationRequest body = new TranslationRequest();
    body.setInput("Text to translate");
    body.setLanguage("spanish");

    HttpEntity<TranslationRequest> entity = new HttpEntity<>(body, headers);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
                                      .scheme("http")
                                      .host("localhost")
                                      .port(port)
                                      .path("translate")
                                      .build();

    final ResponseEntity<String> response = restTemplate.postForEntity(uriComponents.toUriString(), entity, String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), containsString("Spanish translating Text to translate"));
  }

  @Test
  void errorWhenTranslatingInvalidLanguage() {
    TranslationRequest body = new TranslationRequest();
    body.setInput("Text to translate");
    body.setLanguage("german");

    HttpEntity<TranslationRequest> entity = new HttpEntity<>(body, headers);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
                                      .scheme("http")
                                      .host("localhost")
                                      .port(port)
                                      .path("translate")
                                      .build();

    final ResponseEntity<String> response = restTemplate.postForEntity(uriComponents.toUriString(), entity, String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    assertThat(response.getBody(), containsString("Invalid language :: german"));
  }
}
