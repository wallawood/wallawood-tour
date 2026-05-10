package com.example.capsule.errors;

import io.github.wallawood.GeminiResponse;
import io.github.wallawood.annotations.GeminiExceptionHandler;

@GeminiExceptionHandler
public class GlobalExceptionHandler {

    public GeminiResponse handle(ExistentialDreadException e) {
        return GeminiResponse.success(
                "# ðŸŽª Caught!\n\n"
                + "The handler threw: " + e.getMessage() + "\n\n"
                + "But the @GeminiExceptionHandler turned it into this page.\n\n"
                + "=> /chaos Again\n=> / â† Home\n\n"
                + "---\n"
                + "ðŸ“‚ Source: errors/GlobalExceptionHandler.java â€” demos @GeminiExceptionHandler.\n");
    }

    public GeminiResponse handle(Exception e) {
        return GeminiResponse.success(
                "# Something broke\n\n" + e.getClass().getSimpleName() + ": " + e.getMessage()
                + "\n\n=> / â† Home\n");
    }
}
