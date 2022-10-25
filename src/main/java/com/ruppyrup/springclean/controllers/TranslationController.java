package com.ruppyrup.springclean.controllers;


import com.ruppyrup.springclean.dto.TranslationRequest;
import com.ruppyrup.springclean.whatfactories.Translator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/translate")
public class TranslationController {

  @Autowired
  private Translator translator;

  @PostMapping
  public String translate(@RequestHeader Map<String, String> headers, @RequestBody TranslationRequest input) {
    String inputToTranslate = input.getInput();
    String language = input.getLanguage();
//    RequestContext.set(input);
    log.info("Translation request " + input);
    return translator.translate(inputToTranslate);
  }

}
