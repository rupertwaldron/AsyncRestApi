package com.ruppyrup.springclean.controllers;

import com.ruppyrup.springclean.whatfactories.LanguageNotRecognisedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LanguageNotRecognisedAdvice {

  @ResponseBody
  @ExceptionHandler(LanguageNotRecognisedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String employeeNotFoundHandler(LanguageNotRecognisedException ex) {
    return ex.getMessage();
  }
}
