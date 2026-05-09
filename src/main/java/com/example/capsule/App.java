package com.example.capsule;

import io.gemboot.GembootConfig;
import io.gemboot.GembootServer;

public class App {
    public static void main(String[] args) {
        var config = GembootConfig.fromProperties();
        GembootServer.start(App.class, config);
    }
}
