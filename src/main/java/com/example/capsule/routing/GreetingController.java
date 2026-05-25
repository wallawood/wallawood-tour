package com.example.capsule.routing;

import io.github.wallawood.annotations.QueryString;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.GeminiResponse;
import io.github.wallawood.annotations.GeminiController;

@GeminiController
public class GreetingController {

    @Path("/greet")
    public GeminiResponse greet(@QueryString String name) {
        return GeminiResponse.success(
                "# Greeting\n\n"
                        + "Hello, " + name + "\n\n"
                        + "=> / Home\n\n"
                        + "---\n"
                        + "Source: routing/GreetingController.java -- demos @QueryParam for named parameters.\n");
    }
}
