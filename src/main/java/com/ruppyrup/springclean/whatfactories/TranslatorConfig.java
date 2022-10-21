package com.ruppyrup.springclean.whatfactories;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class TranslatorConfig {

  @Bean
  @Primary
  @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
  public FactoryBean<Translator> sessionScopedTranslatorFactory() {
    return new TranslatorFactoryBean();
  }

  @Bean
  public Translator spanishTranslator() {
    return new SpanishTranslator();
  }

  @Bean
  public Translator frenchTranslator() {
    return new FrenchTranslator();
  }
}
