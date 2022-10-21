package com.ruppyrup.springclean.controllers;

import com.ruppyrup.springclean.dto.TranslationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;


@Slf4j
@ControllerAdvice
public class ThreadLocalTranslationRequestAdvice extends RequestBodyAdviceAdapter {

  @Override
  public boolean supports(final MethodParameter methodParameter, final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object afterBodyRead(final Object body, final HttpInputMessage inputMessage, final MethodParameter parameter, final Type targetType, final Class<? extends HttpMessageConverter<?>> converterType) {
    if (body instanceof TranslationRequest translationRequest) {
      RequestContext.set(translationRequest);
    }
    return body;
  }
}
