package com.ruppyrup.springclean.controllers;


import com.ruppyrup.springclean.dto.TranslationRequest;
import com.ruppyrup.springclean.whatfactories.Translator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/translate")
public class TranslationController {

  @Autowired
  private Translator translator;

  @PostMapping
  public String translate(@RequestBody TranslationRequest input) {
    String inputToTranslate = input.getInput();
    String language = input.getLanguage();
//    RequestContext.set(input);
    log.info("Language = " + language);
    return translator.translate(inputToTranslate);
  }

}
