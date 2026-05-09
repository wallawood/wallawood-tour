package com.example.capsule.context;

import io.gemboot.annotations.Context;
import io.gemboot.annotations.GeminiController;
import io.gemboot.annotations.Path;
import io.gemboot.GeminiResponse;

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
                + "=> /whoami 🪪 Who am I? (your certificate)\n"
                + "=> / ← Home\n\n"
                + "---\n"
                + "📂 Source: context/ServerInfoController.java — demos @Context with a custom object.\n"
                + "📂 The ServerInfo is added by context/ContextPreprocessor.java — a @Preprocessor that enriches every request.\n");
    }
}
