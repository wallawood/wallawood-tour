package com.example.capsule.routing;

import io.gemboot.annotations.QueryString;
import io.gemboot.annotations.Path;
import io.gemboot.GeminiResponse;
import io.gemboot.annotations.GeminiController;

@GeminiController
public class GreetingController {

    @Path("/greet")
    public GeminiResponse greet(@QueryString String name) {
        return GeminiResponse.success(
                "# Greeting\n\n"
                + "Hello, " + name + "\n\n"
                + "=> / ← Home\n\n"
                + "---\n"
                + "📂 Source: routing/GreetingController.java — demos @QueryParam for named parameters.\n");
    }
}
