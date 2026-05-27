package com.example.capsule.errors;

import io.github.wallawood.GeminiResponse;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.Path;

@GeminiController
public class ErrorDemoController {

  @Path("/chaos")
  public GeminiResponse throwError() {
    throw new ExistentialDreadException();
  }
}
