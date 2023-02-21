package com.ruppyrup.springclean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class SpringCleanApplication{

  public static void main(String[] args) {
    SpringApplication.run(SpringCleanApplication.class, args);
  }

}
