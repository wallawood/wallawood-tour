package com.example.capsule.context;

import io.github.wallawood.annotations.Context;
import io.github.wallawood.annotations.GeminiController;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.GeminiResponse;

@GeminiController
public class ServerInfoController {

    @Path("/whoareyou")
    public GeminiResponse whoareyou(@Context ServerInfo server) {
        return GeminiResponse.success(
                "# Who Are You?\n\n"
                + "Good question. I'm a server. I serve pages. That's my whole deal.\n\n"
                + "## The Basics\n"
                + "Name: " + server.name() + "\n"
                + "Version: " + server.version() + "\n"
                + "Up since: " + server.startedAt() + "\n\n"
                + "## A Note\n"
                + "This page exists because a @Preprocessor injects a ServerInfo object\n"
                + "into every request's context. The controller just asks for it with @Context.\n"
                + "It's custom plumbing, not protocol plumbing.\n\n"
                + "=> /whoami ðŸªª Who am I? (your certificate)\n"
                + "=> / â† Home\n\n"
                + "---\n"
                + "ðŸ“‚ Source: context/ServerInfoController.java â€” demos @Context with a custom object.\n"
                + "ðŸ“‚ The ServerInfo is added by context/ContextPreprocessor.java â€” a @Preprocessor that enriches every request.\n");
    }
}
