package com.ruppyrup.springclean.integrationtests;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class IntegrationTestConfig {

  @Bean
  TestRestTemplate testRestTemplate() {
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    List<ClientHttpRequestInterceptor> interceptors = testRestTemplate.getRestTemplate().getInterceptors();
    if (CollectionUtils.isEmpty(interceptors)) {
      interceptors = new ArrayList<>();
    }
    interceptors.add(new RestTemplateErrorModifierInterceptor());
    interceptors.add(new RestTemplateHeaderModifierInterceptor());
    testRestTemplate.getRestTemplate().setInterceptors(interceptors);
    return testRestTemplate;
  }
}
