package com.ruppyrup.springclean;

import com.ruppyrup.springclean.whatfactories.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class SpringCleanApplication{

  @Autowired
  private Reader reader;

  public static void main(String[] args) {
    SpringApplication.run(SpringCleanApplication.class, args);
  }

}
