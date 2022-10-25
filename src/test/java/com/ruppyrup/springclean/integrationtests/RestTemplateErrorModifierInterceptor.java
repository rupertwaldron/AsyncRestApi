package com.ruppyrup.springclean.integrationtests;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RestTemplateErrorModifierInterceptor
    implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request,
      byte[] body,
      ClientHttpRequestExecution execution) throws IOException {

    ClientHttpResponse response = execution.execute(request, body);

    String responseString = new String(response.getBody().readAllBytes());
    String errorResponse = response.getStatusCode() + response.getStatusText();

    ClientHttpResponse newResponse = new ClientHttpResponse() {

      @Override
      public HttpHeaders getHeaders() {
        return response.getHeaders();
      }

      @Override
      public InputStream getBody() throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
          String out = responseString + errorResponse;
          return new ByteArrayInputStream(out.getBytes());
        }
        return new ByteArrayInputStream(responseString.getBytes());
      }

      @Override
      public HttpStatus getStatusCode() throws IOException {
        return response.getStatusCode();
      }

      @Override
      public int getRawStatusCode() throws IOException {
        return response.getRawStatusCode();
      }

      @Override
      public String getStatusText() throws IOException {
        return response.getStatusText();
      }

      @Override
      public void close() {

      }
    };

    return newResponse;
  }
}
