package com.ruppyrup.springclean.whatfactories;

import com.ruppyrup.springclean.controllers.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;

@Slf4j
public class TranslatorFactoryBean implements FactoryBean<Translator>, BeanFactoryAware {

  private BeanFactory beanFactory;

  @Override
  public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public Translator getObject() {
    log.info("Request body = " + RequestContext.get());

    return switch (RequestContext.get().getLanguage().toLowerCase()) {
      case "spanish" -> beanFactory.getBean(SpanishTranslator.class);
      case "french" -> beanFactory.getBean(FrenchTranslator.class);
      default -> throw new LanguageNotRecognisedException("Invalid language :: " + RequestContext.get().getLanguage());
    };
  }

  @Override
  public Class<?> getObjectType() {
    return Translator.class;
  }
}
