package com.ruppyrup.springclean.whatfactories;


import org.springframework.stereotype.Component;

@Component
public class Reader {

  private final Translator translator;

  public Reader(final Translator translator) {
    this.translator = translator;
  }

  public void translateInput(final String input) {
    translator.translate(input);
  }
}
