package com.example.capsule.errors;

import io.gemboot.annotations.Path;
import io.gemboot.GeminiResponse;
import io.gemboot.annotations.GeminiController;

@GeminiController
public class ErrorDemoController {

    @Path("/chaos")
    public GeminiResponse throwError() {
        throw new ExistentialDreadException();
    }
}
