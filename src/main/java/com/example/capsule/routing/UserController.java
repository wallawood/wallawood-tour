package com.example.capsule.routing;

import io.gemboot.annotations.Path;
import io.gemboot.annotations.PathParam;
import io.gemboot.GeminiResponse;
import io.gemboot.annotations.GeminiController;
import io.gemboot.annotations.QueryString;
import io.gemboot.annotations.RequireInput;

@GeminiController
public class UserController {

    @Path("/user/{id}")
    @RequireInput("Enter your name:")
    public GeminiResponse showProfile(@PathParam("id") int id, @QueryString String name) {
        return GeminiResponse.success(
                "# User Profile\n\n"
                + "ID: " + id + " (@PathParam)\n"
                + "Name: " + name + " (@QueryString)\n\n"
                + "=> / ← Home\n\n"
                + "---\n"
                + "📂 Source: routing/UserController.java — demos @PathParam, @QueryString, and @RequireInput.\n");
    }
}
