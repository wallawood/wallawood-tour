package com.example.capsule.routing;

import io.gemboot.annotations.Path;
import io.gemboot.GeminiResponse;
import io.gemboot.annotations.GeminiController;

import java.util.concurrent.atomic.AtomicInteger;

@GeminiController
public class CounterController {

    private final AtomicInteger hits = new AtomicInteger(0);

    @Path("/counter")
    public GeminiResponse increment() {
        int n = hits.incrementAndGet();
        return GeminiResponse.success(
                "# Hit Counter\n\nVisit #" + n + "\n\n"
                + "=> /counter Again\n=> / ← Home\n\n"
                + "---\n"
                + "📂 Source: routing/CounterController.java — demos @Path and shared controller state.\n");
    }
}
