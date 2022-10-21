package com.ruppyrup.springclean.controllers;

import com.ruppyrup.springclean.dto.TranslationRequest;

public class RequestContext {

  private static final InheritableThreadLocal<TranslationRequest> REQUEST_THREAD_LOCAL = new InheritableThreadLocal<>();

  public static void set(TranslationRequest translationRequest) {
    REQUEST_THREAD_LOCAL.set(translationRequest);
  }

  public static TranslationRequest get() {
    return REQUEST_THREAD_LOCAL.get();
  }

  public static void clear() {
    REQUEST_THREAD_LOCAL.remove();
  }
}
