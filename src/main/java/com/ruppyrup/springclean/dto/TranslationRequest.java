package com.ruppyrup.springclean.dto;


import lombok.Data;

@Data
public class TranslationRequest {
  private String input;
  private String language;
}
