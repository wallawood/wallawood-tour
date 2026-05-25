package com.example.capsule.routing;

import io.github.wallawood.annotations.Path;
import io.github.wallawood.GeminiResponse;
import io.github.wallawood.annotations.GeminiController;

import java.util.concurrent.atomic.AtomicInteger;

@GeminiController
public class CounterController {

    private final AtomicInteger hits = new AtomicInteger(0);

    @Path("/counter")
    public GeminiResponse increment() {
        int n = hits.incrementAndGet();
        return GeminiResponse.success(
                "# Hit Counter\n\nVisit #" + n + "\n\n"
                        + "=> /counter Again\n=> / Home\n\n"
                        + "---\n"
                        + "Source: routing/CounterController.java -- demos @Path and shared controller state.\n");
    }
}
