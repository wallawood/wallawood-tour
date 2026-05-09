package com.example.capsule.context;

import io.github.wallawood.GeminiResponse;
import io.github.wallawood.RequestContext;
import io.github.wallawood.RequestInterceptor;
import io.github.wallawood.annotations.Preprocessor;

import java.time.Instant;
import java.util.Optional;

@Preprocessor(priority = -100)
public class ContextPreprocessor implements RequestInterceptor {

    private static final ServerInfo SERVER_INFO = new ServerInfo(
            "Gemini Capsule", "1.0.0", Instant.now());

    @Override
    public Optional<GeminiResponse> intercept(RequestContext context) {
        context.add(SERVER_INFO);
        return Optional.empty();
    }
}
