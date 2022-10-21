package com.ruppyrup.springclean.whatfactories;

public class LanguageNotRecognisedException extends RuntimeException {
  public LanguageNotRecognisedException(final String message) {
    super(message);
  }
}
