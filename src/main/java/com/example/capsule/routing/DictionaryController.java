package com.example.capsule.routing;

import io.github.wallawood.annotations.DefaultValue;
import io.github.wallawood.annotations.Path;
import io.github.wallawood.annotations.QueryParam;
import io.github.wallawood.GeminiResponse;
import io.github.wallawood.annotations.GeminiController;

import java.util.List;

@GeminiController
public class DictionaryController {

    private static final List<String> WORDS = List.of(
            "gemini", "capsule", "protocol", "constellation", "nebula",
            "quasar", "pulsar", "asteroid", "comet", "eclipse",
            "orbit", "gravity", "photon", "spectrum", "cosmos",
            "supernova", "wormhole", "singularity", "antimatter", "parallax");

    @Path("/dictionary")
    public GeminiResponse search(@QueryParam("search") @DefaultValue("") String query) {
        List<String> matches = WORDS.stream()
                .filter(word -> word.contains(query.toLowerCase()))
                .toList();

        StringBuilder sb = new StringBuilder("# Dictionary Search\n\nQuery: \"" + query + "\"\n\n");
        if (matches.isEmpty()) {
            sb.append("No matches.\n");
        } else {
            matches.forEach(word -> sb.append("* ").append(word).append("\n"));
        }
        sb.append("\n=> / â† Home\n\n");
        sb.append("---\n");
        sb.append("ðŸ“‚ Source: routing/DictionaryController.java â€” demos @QueryParam and @DefaultValue.\n");
        sb.append("Try editing the URL: /dictionary?search=star or /dictionary?search=co\n");
        return GeminiResponse.success(sb.toString());
    }
}
