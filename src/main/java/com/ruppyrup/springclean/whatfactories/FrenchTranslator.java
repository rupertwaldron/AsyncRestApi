package com.ruppyrup.springclean.whatfactories;


public class FrenchTranslator implements Translator {
  @Override
  public String translate(final String input) {
    return "French translating " + input + " class = " + this;
  }
}
