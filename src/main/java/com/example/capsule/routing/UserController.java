package com.example.capsule.routing;

import io.github.wallawood.annotations.Path;
import io.github.wallawood.annotations.PathParam;
import io.github.wallawood.GeminiResponse;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.QueryString;
import io.github.wallawood.annotations.RequireInput;

@GeminiController
public class UserController {

    @Path("/user/{id}")
    @RequireInput("Enter your name:")
    public GeminiResponse showProfile(@PathParam("id") int id, @QueryString String name) {
        return GeminiResponse.success(
                "# User Profile\n\n"
                        + "ID: " + id + " (@PathParam)\n"
                        + "Name: " + name + " (@QueryString)\n\n"
                        + "=> / Home\n\n"
                        + "---\n"
                        + "Source: routing/UserController.java -- demos @PathParam, @QueryString, and @RequireInput.\n");
    }
}
